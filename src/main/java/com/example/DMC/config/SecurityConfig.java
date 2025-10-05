package com.example.DMC.config;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

import java.util.Map;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) // ← habilita @PreAuthorize / @PostAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService uds;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(uds);
        provider.setPasswordEncoder(encoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * (Opcional pero útil) Jerarquía:
     * ADMINISTRADOR > CAJERO
     * ADMINISTRADOR > ALMACENISTA
     * Esto permite que hasRole('CAJERO') sea true para un ADMIN, etc.
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl rh = new RoleHierarchyImpl();
        rh.setHierarchy("""
            ROLE_ADMINISTRADOR > ROLE_CAJERO
            ROLE_ADMINISTRADOR > ROLE_ALMACENISTA
            """);
        return rh;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/ventas/guardar", "/ventas/buscar")
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            )
            .authorizeHttpRequests(auth -> auth
                // Público
                .requestMatchers("/", "/login", "/register",
                                 "/css/**", "/js/**", "/img/**",
                                 "/favicon.ico", "/.well-known/**")
                .permitAll()

                // Index para clientes normales
                .requestMatchers("/index", "/home").authenticated()

                // Solo ADMINISTRADOR
                .requestMatchers("/dashboard", "/dashboard/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/usuarios", "/usuarios/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/roles", "/roles/**").hasRole("ADMINISTRADOR")

                // ADMINISTRADOR o CAJERO
                .requestMatchers("/ventas", "/ventas/**",
                                 "/caja", "/caja/**")
                .hasAnyRole("ADMINISTRADOR", "CAJERO")

                // ADMINISTRADOR o ALMACENISTA
                .requestMatchers("/almacenes", "/almacenes/**",
                                 "/productos", "/productos/**",
                                 "/categorias", "/categorias/**",
                                 "/proveedores", "/proveedores/**")
                .hasAnyRole("ADMINISTRADOR", "ALMACENISTA")

                // Todo lo demás: autenticado
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler((request, response, authentication) -> {
                    Map<String, String> redirect = Map.of(
                        "ROLE_ADMINISTRADOR", "/dashboard",
                        "ROLE_CAJERO", "/ventas",
                        "ROLE_ALMACENISTA", "/productos/dashboard",
                        "ROLE_CLIENTE", "/index"
                    );
                    String target = authentication.getAuthorities().stream()
                        .map(a -> a.getAuthority())
                        .filter(redirect::containsKey)
                        .map(redirect::get)
                        .findFirst()
                        .orElse("/index");
                    response.sendRedirect(target);
                })
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedHandler((req, res, e) -> res.sendRedirect("/login?denied=true"))
                .authenticationEntryPoint((req, res, e) -> res.sendRedirect("/"))
            )
            .authenticationProvider(authenticationProvider(passwordEncoder()));

        return http.build();
    }
}

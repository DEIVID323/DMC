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

@Configuration
@EnableMethodSecurity
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

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                                .authorizeHttpRequests(reg -> reg
                                                .requestMatchers("/", "/login", "/css/**", "/js/**", "/img/**")
                                                .permitAll()
                                                .requestMatchers("/Ventas/**", "/Caja/**").hasAnyRole("ADMIN", "CAJERO")
                                                .requestMatchers("/Productos/**", "/Almacenes/**", "/inventario/**",
                                                                "/almacen/**")
                                                .hasAnyRole("ADMIN", "ALMACENISTA")
                                                /* .requestMatchers("/usuarios/**", "/admin/**").hasRole("ADMIN") */
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                // Redirección SIN crear otro archivo: handler inline por rol
                                                .successHandler((req, res, auth) -> {
                                                        var a = auth.getAuthorities();
                                                        String target = a.stream().anyMatch(
                                                                        x -> x.getAuthority().equals("ROLE_ADMIN"))
                                                                                        ? "/admin/dashboard"
                                                                                        : a.stream().anyMatch(x -> x
                                                                                                        .getAuthority()
                                                                                                        .equals("ROLE_CAJERO"))
                                                                                                                        ? "/Ventas/dashboard"
                                                                                                                        : a.stream().anyMatch(
                                                                                                                                        x -> x.getAuthority()
                                                                                                                                                        .equals("ROLE_ALMACENISTA"))
                                                                                                                                                                        ? "/Productos/dashboard"
                                                                                                                                                                        : "/dashboard";
                                                        res.sendRedirect(target);
                                                })
                                                .failureUrl("/login?error=true")
                                                .permitAll())
                                .logout(lo -> lo.logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout=true")
                                                .deleteCookies("JSESSIONID")
                                                .invalidateHttpSession(true));
                return http.build();
        }
}

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
                DaoAuthenticationProvider p = new DaoAuthenticationProvider();
                p.setUserDetailsService(uds);
                p.setPasswordEncoder(encoder);
                return p;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
                return cfg.getAuthenticationManager();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                                .authorizeHttpRequests(reg -> reg
                                                .requestMatchers("/", "/login", "/css/**", "/js/**", "/img/**",
                                                                "/favicon.ico", "/.well-known/**")
                                                .permitAll()

                                                // 👇 Rutas base + /** (si no, /ventas DA 403)
                                                .requestMatchers("/ventas", "/ventas/**", "/caja", "/caja/**")
                                                .hasAnyRole("ADMINISTRADOR", "CAJERO")

                                                .requestMatchers("/almacenes", "/almacenes/**")
                                                .hasAnyRole("ADMINISTRADOR", "ALMACENISTA")

                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .successHandler((req, res, auth) -> {
                                                        var a = auth.getAuthorities();
                                                        String target = a.stream().anyMatch(x -> x.getAuthority()
                                                                        .equals("ROLE_ADMINISTRADOR"))
                                                                                        ? "/dashboard"
                                                                                        : a.stream().anyMatch(x -> x
                                                                                                        .getAuthority()
                                                                                                        .equals("ROLE_CAJERO"))
                                                                                                                        ? "/ventas"
                                                                                                                        : a.stream().anyMatch(
                                                                                                                                        x -> x.getAuthority()
                                                                                                                                                        .equals("ROLE_ALMACENISTA"))
                                                                                                                                                                        ? "/productos/dashboard"
                                                                                                                                                                        : "/dashboard";
                                                        res.sendRedirect(target);
                                                })
                                                .failureUrl("/login?error=true")
                                                .permitAll())
                                .logout(lo -> lo
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout=true")
                                                .deleteCookies("JSESSIONID")
                                                .invalidateHttpSession(true))
                                .exceptionHandling(e -> e.accessDeniedHandler(
                                                (req, res, ex) -> res.sendRedirect("/login?denied=true")))
                                .authenticationProvider(authenticationProvider(passwordEncoder()));

                return http.build();
        }
}

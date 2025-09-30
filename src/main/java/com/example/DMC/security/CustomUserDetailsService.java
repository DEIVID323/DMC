package com.example.DMC.security;

import com.example.DMC.model.Usuario;
import com.example.DMC.repository.PermisoRepository;
import com.example.DMC.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
        private final UsuarioRepository usuarioRepo;
        private final PermisoRepository permisoRepo;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Usuario u = usuarioRepo.findByUsernameAndActivoTrue(username)
                                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

                String role = "ROLE_" + u.getRol().getNombreRol().toUpperCase(); // admin -> ROLE_ADMIN

                List<String> permisos = permisoRepo.findPermisosByUsername(username);

                Set<GrantedAuthority> auths = permisos.stream()
                                .filter(p -> p != null && !p.isBlank())
                                .map(String::toUpperCase)
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toSet());

                auths.add(new SimpleGrantedAuthority(role));

                return new org.springframework.security.core.userdetails.User(
                                u.getUsername(),
                                u.getPassword(), // en BCrypt!
                                Boolean.TRUE.equals(u.getActivo()), // habilitado
                                true, true, true,
                                auths);
        }
}

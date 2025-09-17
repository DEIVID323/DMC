package com.example.DMC.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.DMC.model.RolPermiso;
import com.example.DMC.model.Usuario;
import com.example.DMC.repository.RolPermisoRepository;
import com.example.DMC.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolPermisoRepository rolPermisoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // Verificar que el rol no sea nulo
        if (usuario.getRol() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombreRol().toUpperCase()));

            // Agregar permisos específicos del rol
            List<RolPermiso> permisos = rolPermisoRepository.findByIdRol(usuario.getRol().getIdRol());
            for (RolPermiso rolPermiso : permisos) {
                if (rolPermiso.getPermiso() != null) {
                    authorities.add(new SimpleGrantedAuthority(rolPermiso.getPermiso().getNombrePermiso()));
                }
            }
        }

        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getActivo(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities);
    }
}
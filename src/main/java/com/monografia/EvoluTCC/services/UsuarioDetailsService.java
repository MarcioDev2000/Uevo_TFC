package com.monografia.EvoluTCC.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.monografia.EvoluTCC.models.Usuario;
import com.monografia.EvoluTCC.repositories.UsuarioRepository;

import java.util.Optional;

@Service
public class UsuarioDetailsService implements UserDetailsService { 

    @Autowired
    private UsuarioRepository usuarioRepository; 

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            // Verifica se a conta está ativa
            if (!usuario.isStatus()) {
                throw new UsernameNotFoundException("Sua conta ainda não foi ativada. Entre em contato com o administrador.");
            }

            return org.springframework.security.core.userdetails.User
                    .withUsername(usuario.getEmail())
                    .password(usuario.getPassword())
                    .authorities(usuario.getTipoUsuario().getNome())
                    .accountLocked(false)
                    .disabled(false)
                    .build();
        } else {
            throw new UsernameNotFoundException("Usuário não encontrado com o email: " + email);
        }
    }
}
package com.monografia.EvoluTCC.seeders;

import com.monografia.EvoluTCC.models.TipoUsuario;
import com.monografia.EvoluTCC.repositories.TipoUsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class TipoUsuarioSeeder implements CommandLineRunner {

    private final TipoUsuarioRepository tipoUsuarioRepository;

    public TipoUsuarioSeeder(TipoUsuarioRepository tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    @Override
    public void run(String... args) {
        List<TipoUsuario> tiposUsuarios = Arrays.asList(
                new TipoUsuario("Aluno", "/aluno"),
                new TipoUsuario("Orientador", "/orientador"),
                new TipoUsuario("Admin", "/admin")
        );

        tiposUsuarios.forEach(tipo -> {
            if (!tipoUsuarioRepository.existsByNome(tipo.getNome())) {
                tipoUsuarioRepository.save(tipo);
                System.out.println("Tipo de Usuário criado: " + tipo.getNome());
            } else {
                System.out.println("Tipo de Usuário já existe: " + tipo.getNome());
            }
        });
    }
}

package com.monografia.EvoluTCC.seeders;

import com.monografia.EvoluTCC.models.Menus;
import com.monografia.EvoluTCC.models.TipoUsuario;
import com.monografia.EvoluTCC.repositories.MenuRepository;
import com.monografia.EvoluTCC.repositories.TipoUsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
public class MenuSeeder implements CommandLineRunner {

    private final MenuRepository menuRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;

    public MenuSeeder(MenuRepository menuRepository, TipoUsuarioRepository tipoUsuarioRepository) {
        this.menuRepository = menuRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    @Override
    public void run(String... args) {
        if (menuRepository.count() == 0) {
            List<Menus> menus = new ArrayList<>();

            Optional<TipoUsuario> aluno = tipoUsuarioRepository.findByNome("Aluno");
            Optional<TipoUsuario> orientador = tipoUsuarioRepository.findByNome("Orientador");

            aluno.ifPresent(tipo -> {
                menus.add(new Menus("Dashboard", "icon-chart-pie-36", "/aluno/estatistica", tipo.getId().toString()));
                menus.add(new Menus("Perfil", "icon-user", "/aluno/perfil", tipo.getId().toString()));
            });

            orientador.ifPresent(tipo -> {
                menus.add(new Menus("Dashboard", "icon-chart-bar-32", "/orientador/estatistica", tipo.getId().toString()));
                menus.add(new Menus("Gerenciar Alunos", "icon-users", "/orientador/alunos", tipo.getId().toString()));
            });

            menuRepository.saveAll(menus);
            System.out.println("Menus criados com sucesso!");
        } else {
            System.out.println("Menus já existem no banco de dados.");
        }
    }
}

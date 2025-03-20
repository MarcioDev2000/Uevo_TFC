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

            Optional<TipoUsuario> admin = tipoUsuarioRepository.findByNome("Admin");
            Optional<TipoUsuario> aluno = tipoUsuarioRepository.findByNome("Aluno");
            Optional<TipoUsuario> orientador = tipoUsuarioRepository.findByNome("Orientador");

            admin.ifPresent(tipo -> {
                menus.add(new Menus("Dashboard", "icon-chart-bar-32", "/admin/dashboard", tipo));
                menus.add(new Menus("Ver Orientadores", "icon-single-02", "/admin/orientadores", tipo));
                menus.add(new Menus("Ver Alunos", "icon-single-02", "/admin/alunos", tipo));
                menus.add(new Menus("Gestão de Monografias", "icon-paper", "/admin/monografias", tipo));
                menus.add(new Menus("Calendário", "icon-calendar-60", "/admin/calendario", tipo));
                menus.add(new Menus("Pré-Defesa", "icon-calendar-60", "/admin/pre-defesa", tipo));
                menus.add(new Menus("Marcar Defesa", "icon-calendar-60", "/admin/defesa", tipo));
                menus.add(new Menus("Usuarios", "icon-single-02", "/admin/user-inativos", tipo));
                menus.add(new Menus("Defesa", "icon-calendar-60", "/admin/defesa", tipo));
                menus.add(new Menus("Detalhes da Defesa", "icon-paper", "/admin/detalhe-defesa", tipo));
            });

            aluno.ifPresent(tipo -> {
                menus.add(new Menus("Dashboard", "icon-chart-pie-36", "/aluno/estatistica", tipo));
                menus.add(new Menus("Perfil", "icon-single-02", "/aluno/perfil", tipo));
                menus.add(new Menus("Inscrição de Monografia", "icon-paper", "/aluno/inscricao-monografia", tipo));
                menus.add(new Menus("Calendário", "icon-calendar-60", "/aluno/calendario", tipo)); 
                menus.add(new Menus("Pré-Defesa", "icon-calendar-60", "/aluno/pre-defesa", tipo));
                menus.add(new Menus("Defesa", "icon-calendar-60", "/aluno/defesa", tipo));
                menus.add(new Menus("Detalhes da Defesa", "icon-paper", "/aluno/detalhe-defesa", tipo));
               // menus.add(new Menus("Monografias", "icon-book-bookmark", "/aluno/minhas-monografias", tipo)); 
            });

            orientador.ifPresent(tipo -> {
                menus.add(new Menus("Dashboard", "icon-chart-bar-32", "/orientador/estatistica", tipo));
                menus.add(new Menus("Gerenciar Alunos", "icon-single-02", "/orientador/alunos", tipo));
                menus.add(new Menus("Monografias Orientadas", "icon-book-bookmark", "/orientador/monografias", tipo));
                menus.add(new Menus("Calendário", "icon-calendar-60", "/orientador/calendario", tipo)); 
                menus.add(new Menus("Pré-Defesa", "icon-calendar-60", "/orientador/pre-defesa", tipo));
                menus.add(new Menus("Defesa", "icon-calendar-60", "/orientador/defesa", tipo));
                menus.add(new Menus("Detalhes da Defesa", "icon-paper", "/orientador/detalhe-defesa", tipo));

              
            });

            menuRepository.saveAll(menus);
            System.out.println("Menus criados com sucesso!");
        } else {
            System.out.println("Menus já existem no banco de dados.");
        }
    }
}
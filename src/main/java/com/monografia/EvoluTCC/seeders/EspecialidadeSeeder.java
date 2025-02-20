package com.monografia.EvoluTCC.seeders;

import com.monografia.EvoluTCC.models.Curso;
import com.monografia.EvoluTCC.models.Especialidade;
import com.monografia.EvoluTCC.repositories.CursoRepository;
import com.monografia.EvoluTCC.repositories.EspecialidadeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EspecialidadeSeeder implements CommandLineRunner {

    private final EspecialidadeRepository especialidadeRepository;
    private final CursoRepository cursoRepository;

    public EspecialidadeSeeder(EspecialidadeRepository especialidadeRepository, CursoRepository cursoRepository) {
        this.especialidadeRepository = especialidadeRepository;
        this.cursoRepository = cursoRepository;
    }

    @Override
    public void run(String... args) {
        if (especialidadeRepository.count() == 0) {
            Optional<Curso> curso = cursoRepository.findByNome("Engenharia Informática");

            if (curso.isPresent()) {
                List<Especialidade> especialidades = List.of(
                        new Especialidade("Programação", curso.get()),
                        new Especialidade("Redes", curso.get())
                );
                especialidadeRepository.saveAll(especialidades);
                System.out.println("Especialidades de Engenharia Informática criadas com sucesso!");
            } else {
                System.out.println("Curso de Engenharia Informática não encontrado.");
            }
        } else {
            System.out.println("Especialidades já existem no banco de dados.");
        }
    }
}

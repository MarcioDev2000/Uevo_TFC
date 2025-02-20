package com.monografia.EvoluTCC.seeders;

import com.monografia.EvoluTCC.models.Curso;
import com.monografia.EvoluTCC.models.Departamento;
import com.monografia.EvoluTCC.repositories.CursoRepository;
import com.monografia.EvoluTCC.repositories.DepartamentoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CursoSeeder implements CommandLineRunner {

    private final CursoRepository cursoRepository;
    private final DepartamentoRepository departamentoRepository;

    public CursoSeeder(CursoRepository cursoRepository, DepartamentoRepository departamentoRepository) {
        this.cursoRepository = cursoRepository;
        this.departamentoRepository = departamentoRepository;
    }

    @Override
public void run(String... args) {
    if (cursoRepository.count() == 0) {
        Optional<Departamento> departamentoOpt = departamentoRepository.findByNome("Departamento de Informática");

        if (departamentoOpt.isPresent()) {
            Departamento departamento = departamentoOpt.get();

            Curso curso = new Curso();
            curso.setNome("Engenharia Informática");
            curso.setDepartamento(departamento);

            cursoRepository.save(curso);
            System.out.println("Curso de Engenharia Informática criado com sucesso!");
        } else {
            System.out.println("Departamento de Informática não encontrado.");
        }
    } else {
        System.out.println("Curso já existe no banco de dados.");
    }
}

}

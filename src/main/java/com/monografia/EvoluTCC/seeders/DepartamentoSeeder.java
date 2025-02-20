package com.monografia.EvoluTCC.seeders;

import com.monografia.EvoluTCC.models.Departamento;
import com.monografia.EvoluTCC.models.Faculdade;
import com.monografia.EvoluTCC.repositories.DepartamentoRepository;
import com.monografia.EvoluTCC.repositories.FaculdadeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DepartamentoSeeder implements CommandLineRunner {

    private final DepartamentoRepository departamentoRepository;
    private final FaculdadeRepository faculdadeRepository;

    public DepartamentoSeeder(DepartamentoRepository departamentoRepository, FaculdadeRepository faculdadeRepository) {
        this.departamentoRepository = departamentoRepository;
        this.faculdadeRepository = faculdadeRepository;
    }

    @Override
    public void run(String... args) {
        if (departamentoRepository.count() == 0) {
            Optional<Faculdade> faculdade = faculdadeRepository.findByNome("Faculdade de Engenharia");

            if (faculdade.isPresent()) {
                Departamento departamento = new Departamento("Departamento de Informática", faculdade.get());
                departamentoRepository.save(departamento);
                System.out.println("Departamento de Informática criado com sucesso!");
            } else {
                System.out.println("Faculdade de Engenharia não encontrada.");
            }
        } else {
            System.out.println("Departamento já existe no banco de dados.");
        }
    }
}

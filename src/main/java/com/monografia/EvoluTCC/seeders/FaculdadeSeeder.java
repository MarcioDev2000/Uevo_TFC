package com.monografia.EvoluTCC.seeders;

import com.monografia.EvoluTCC.models.Faculdade;
import com.monografia.EvoluTCC.repositories.FaculdadeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FaculdadeSeeder implements CommandLineRunner {

    private final FaculdadeRepository faculdadeRepository;

    public FaculdadeSeeder(FaculdadeRepository faculdadeRepository) {
        this.faculdadeRepository = faculdadeRepository;
    }

    @Override
    public void run(String... args) {
        if (faculdadeRepository.count() == 0) {
            Faculdade faculdade = new Faculdade(
                    "Faculdade de Engenharia",
                    "Oferece cursos de Engenharia, incluindo Engenharia Informática."
            );
            faculdadeRepository.save(faculdade);
            System.out.println("Faculdade de Engenharia criada com sucesso!");
        } else {
            System.out.println("Faculdade já existe no banco de dados.");
        }
    }
}

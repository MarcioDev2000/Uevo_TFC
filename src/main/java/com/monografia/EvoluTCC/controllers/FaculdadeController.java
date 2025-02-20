package com.monografia.EvoluTCC.controllers;

import com.monografia.EvoluTCC.models.Faculdade;
import com.monografia.EvoluTCC.repositories.FaculdadeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/faculdades")
public class FaculdadeController {

    private final FaculdadeRepository faculdadeRepository;

    public FaculdadeController(FaculdadeRepository faculdadeRepository) {
        this.faculdadeRepository = faculdadeRepository;
    }

    @GetMapping
    public List<Faculdade> listarFaculdades() {
        return faculdadeRepository.findAll();
    }

    
}

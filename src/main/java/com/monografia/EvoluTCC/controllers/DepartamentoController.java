package com.monografia.EvoluTCC.controllers;

import com.monografia.EvoluTCC.models.Departamento;
import com.monografia.EvoluTCC.repositories.DepartamentoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/departamentos")
public class DepartamentoController {

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoController(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }
    @GetMapping
    public List<Departamento> listarDepartamentos() {
        return departamentoRepository.findAll();
    }
}

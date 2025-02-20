package com.monografia.EvoluTCC.controllers;

import com.monografia.EvoluTCC.models.TipoUsuario;
import com.monografia.EvoluTCC.repositories.TipoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tipos-usuario")
public class TipoUsuarioController {

    private final TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    public TipoUsuarioController(TipoUsuarioRepository tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    @GetMapping
    public List<TipoUsuario> getAllTiposUsuario() {
        return tipoUsuarioRepository.findAll();
    }
}
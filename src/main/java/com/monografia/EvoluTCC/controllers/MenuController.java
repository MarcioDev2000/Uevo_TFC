package com.monografia.EvoluTCC.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monografia.EvoluTCC.models.Menus;
import com.monografia.EvoluTCC.repositories.MenuRepository;

import java.util.List;

@RestController
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    private MenuRepository menuRepository;

    // Método para buscar todos os menus
    @GetMapping
    public ResponseEntity<List<Menus>> getAllMenus() {
        List<Menus> menus = menuRepository.findAll();
        return ResponseEntity.ok(menus);
    }
}

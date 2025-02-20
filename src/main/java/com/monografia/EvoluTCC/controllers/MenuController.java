package com.monografia.EvoluTCC.controllers;

import com.monografia.EvoluTCC.dto.MenuDTO;
import com.monografia.EvoluTCC.models.Menus;
import com.monografia.EvoluTCC.repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    private MenuRepository menuRepository;

    @GetMapping
    public ResponseEntity<List<MenuDTO>> getAllMenus() {
        List<Menus> menus = menuRepository.findAll();
        List<MenuDTO> menuDTOs = menus.stream()
                .map(menu -> new MenuDTO(
                        menu.getId(),
                        menu.getNome(),
                        menu.getIcon(),
                        menu.getRota(),
                        menu.getTipoUsuario().getNome() // Usa o nome do tipo de usuário
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(menuDTOs);
    }

    // Método para buscar menus por tipo de usuário
    @GetMapping("/")
    public ResponseEntity<List<MenuDTO>> getMenusByUsuarioId(@RequestParam("id_do_usuario") UUID idDoUsuario) {
        List<Menus> menus = menuRepository.findByTipoUsuarioId(idDoUsuario);
        List<MenuDTO> menuDTOs = menus.stream()
                .map(menu -> new MenuDTO(
                        menu.getId(),
                        menu.getNome(),
                        menu.getIcon(),
                        menu.getRota(),
                        menu.getTipoUsuario().getNome() // Usa o nome do tipo de usuário
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(menuDTOs);
    }
}
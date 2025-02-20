package com.monografia.EvoluTCC.services;

import com.monografia.EvoluTCC.models.Menus;
import com.monografia.EvoluTCC.repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    
    public List<Menus> getAllMenus() {
        return menuRepository.findAll();
    }

    public Menus getMenuById(UUID id) {
        return menuRepository.findById(id).orElse(null); 
    }

    // Método para salvar um novo menu
    public Menus createMenu(Menus menu) {
        return menuRepository.save(menu);
    }
}

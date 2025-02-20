package com.monografia.EvoluTCC.seeders;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.monografia.EvoluTCC.models.TipoUsuario;
import com.monografia.EvoluTCC.models.Usuario;
import com.monografia.EvoluTCC.repositories.TipoUsuarioRepository;
import com.monografia.EvoluTCC.repositories.UsuarioRepository;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;

    public AdminSeeder(UsuarioRepository usuarioRepository, TipoUsuarioRepository tipoUsuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByEmail("marciodev2000@gmail.com")) {
            TipoUsuario tipoAdmin = tipoUsuarioRepository.findByNome("Admin")
                    .orElseThrow(() -> new RuntimeException("Tipo de Usuário 'Admin' não encontrado."));

            Usuario admin = new Usuario();
            admin.setNome("Admin");
            admin.setSobrenome("Sistema");
            admin.setEmail("marciodev2000@gmail.com");
            admin.setPassword("admin123"); 
            admin.setTipoUsuario(tipoAdmin);

            usuarioRepository.save(admin);
            System.out.println("Admin criado com sucesso!");
        } else {
            System.out.println("Admin já existe no banco de dados.");
        }
    }
}
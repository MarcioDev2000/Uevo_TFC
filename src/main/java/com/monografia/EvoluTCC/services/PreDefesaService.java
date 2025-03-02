package com.monografia.EvoluTCC.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.monografia.EvoluTCC.Enums.StatusDefesa;
import com.monografia.EvoluTCC.Enums.StatusMonografia;
import com.monografia.EvoluTCC.models.Monografia;
import com.monografia.EvoluTCC.models.PreDefesa;
import com.monografia.EvoluTCC.models.Usuario;
import com.monografia.EvoluTCC.repositories.MonografiaRepository;
import com.monografia.EvoluTCC.repositories.PreDefesaRepository;
import com.monografia.EvoluTCC.repositories.UsuarioRepository;

@Service
public class PreDefesaService {

    @Autowired
    private PreDefesaRepository preDefesaRepository;

    @Autowired
    private MonografiaRepository monografiaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public PreDefesa marcarPreDefesa(UUID monografiaId, UUID presidenteId, UUID vogalId, LocalDateTime dataPreDefesa) {
        // Busca a monografia
        Monografia monografia = monografiaRepository.findById(monografiaId)
                .orElseThrow(() -> new RuntimeException("Monografia não encontrada com o ID: " + monografiaId));

        // Verifica se a monografia está em pré-defesa
        if (monografia.getStatus() != StatusMonografia.EM_PRE_DEFESA) {
            throw new RuntimeException("A monografia não está com o projeto entregue para pré-defesa.");
        }

        // Busca o presidente e o vogal
        Usuario presidente = usuarioRepository.findById(presidenteId)
                .orElseThrow(() -> new RuntimeException("Presidente não encontrado com o ID: " + presidenteId));

        Usuario vogal = usuarioRepository.findById(vogalId)
                .orElseThrow(() -> new RuntimeException("Vogal não encontrado com o ID: " + vogalId));

        // Cria a pré-defesa
        PreDefesa preDefesa = new PreDefesa();
        preDefesa.setMonografia(monografia);
        preDefesa.setPresidente(presidente);
        preDefesa.setVogal(vogal);
        preDefesa.setDataPreDefesa(dataPreDefesa);
        preDefesa.setStatus(StatusDefesa.MARCADA); // Status inicial da pré-defesa

        return preDefesaRepository.save(preDefesa);
    }

    public List<Monografia> getMonografiasEmPreDefesa() {
        return monografiaRepository.findByStatus(StatusMonografia.EM_PRE_DEFESA);
    }
}

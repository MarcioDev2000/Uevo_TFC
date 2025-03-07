package com.monografia.EvoluTCC.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.monografia.EvoluTCC.Enums.StatusDefesa;
import com.monografia.EvoluTCC.Enums.StatusMonografia;
import com.monografia.EvoluTCC.dto.PreDefesaDTO;
import com.monografia.EvoluTCC.models.Monografia;
import com.monografia.EvoluTCC.models.PreDefesa;
import com.monografia.EvoluTCC.models.Usuario;
import com.monografia.EvoluTCC.repositories.MonografiaRepository;
import com.monografia.EvoluTCC.repositories.PreDefesaRepository;
import com.monografia.EvoluTCC.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class PreDefesaService {

    @Autowired
    private PreDefesaRepository preDefesaRepository;

    @Autowired
    private MonografiaRepository monografiaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
public PreDefesaDTO criarPreDefesa(UUID monografiaId, UUID presidenteId, UUID vogalId, LocalDateTime dataInicio, LocalDateTime dataFim, String descricao) {
    // Busca a monografia pelo ID
    Monografia monografia = monografiaRepository.findById(monografiaId)
            .orElseThrow(() -> new RuntimeException("Monografia não encontrada com o ID: " + monografiaId));

    // Verifica se a monografia está no status APROVADO
    if (monografia.getStatus() != StatusMonografia.APROVADO) {
        throw new RuntimeException("A monografia precisa estar no status APROVADO para criar uma pré-defesa.");
    }

    // Busca o presidente pelo ID
    Usuario presidente = usuarioRepository.findById(presidenteId)
            .orElseThrow(() -> new RuntimeException("Presidente não encontrado com o ID: " + presidenteId));

    // Busca o vogal pelo ID
    Usuario vogal = usuarioRepository.findById(vogalId)
            .orElseThrow(() -> new RuntimeException("Vogal não encontrado com o ID: " + vogalId));

    // Verifica se o presidente e o vogal pertencem à mesma especialidade da monografia
    if (!presidente.getEspecialidade().getId().equals(monografia.getEspecialidade().getId())) {
        throw new RuntimeException("O presidente selecionado não pertence à especialidade da monografia.");
    }
    if (!vogal.getEspecialidade().getId().equals(monografia.getEspecialidade().getId())) {
        throw new RuntimeException("O vogal selecionado não pertence à especialidade da monografia.");
    }

    // Verifica se a data de início e fim são válidas
    if (dataInicio.isBefore(LocalDateTime.now())) {
        throw new RuntimeException("A data de início da pré-defesa deve ser futura.");
    }
    if (dataFim.isBefore(dataInicio)) {
        throw new RuntimeException("A data de término da pré-defesa deve ser posterior à data de início.");
    }

    // Verifica se já existe uma pré-defesa marcada no mesmo intervalo de tempo e mesma especialidade
    boolean conflitoDeDatas = preDefesaRepository.existsByDataInicioBetweenAndMonografiaEspecialidadeId(
            dataInicio, dataFim, monografia.getEspecialidade().getId());

    if (conflitoDeDatas) {
        throw new RuntimeException("Já existe uma pré-defesa marcada para o mesmo intervalo de tempo e especialidade.");
    }

    // Cria a pré-defesa
    PreDefesa preDefesa = new PreDefesa();
    preDefesa.setMonografia(monografia);
    preDefesa.setDataInicio(dataInicio);
    preDefesa.setDataFim(dataFim);
    preDefesa.setPresidente(presidente);
    preDefesa.setVogal(vogal);
    preDefesa.setStatus(StatusDefesa.MARCADA); // Status inicial da pré-defesa
    preDefesa.setDescricao(descricao);

    // Atualiza o status da monografia para EM_PRE_DEFESA
    monografia.setStatus(StatusMonografia.EM_PRE_DEFESA);
    monografiaRepository.save(monografia);

    // Salva a pré-defesa no banco de dados
    PreDefesa savedPreDefesa = preDefesaRepository.save(preDefesa);

    // Cria o DTO para retornar a resposta
    PreDefesaDTO preDefesaDTO = new PreDefesaDTO();
    preDefesaDTO.setId(savedPreDefesa.getId());
    preDefesaDTO.setMonografiaId(savedPreDefesa.getMonografia().getId());
    preDefesaDTO.setTemaMonografia(savedPreDefesa.getMonografia().getTema()); // Define o tema da monografia
    preDefesaDTO.setDescricao(savedPreDefesa.getDescricao());
    preDefesaDTO.setDataInicio(savedPreDefesa.getDataInicio());
    preDefesaDTO.setDataFim(savedPreDefesa.getDataFim());
    preDefesaDTO.setPresidenteId(savedPreDefesa.getPresidente().getId());
    preDefesaDTO.setVogalId(savedPreDefesa.getVogal().getId());
    preDefesaDTO.setStatus(savedPreDefesa.getStatus());

    return preDefesaDTO;
}
}
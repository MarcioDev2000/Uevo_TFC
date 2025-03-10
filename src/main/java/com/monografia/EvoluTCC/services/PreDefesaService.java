package com.monografia.EvoluTCC.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.monografia.EvoluTCC.Enums.StatusDefesa;
import com.monografia.EvoluTCC.Enums.StatusMonografia;
import com.monografia.EvoluTCC.dto.PreDefesaDTO;
import com.monografia.EvoluTCC.dto.PreDefesaResponseDTO;
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
    public PreDefesaDTO criarPreDefesa(UUID monografiaId, UUID presidenteId, UUID vogalId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        // Busca a monografia pelo ID
        Monografia monografia = monografiaRepository.findById(monografiaId)
                .orElseThrow(() -> new RuntimeException("Monografia não encontrada com o ID: " + monografiaId));
    
        // Verifica se a monografia está no status APROVADO
        if (monografia.getStatus() != StatusMonografia.APROVADO) {
            throw new RuntimeException("A monografia precisa estar no status APROVADO para criar uma pré-defesa.");
        }
    
        // Verifica se já existe uma pré-defesa ativa para a mesma monografia
        boolean preDefesaAtiva = preDefesaRepository.existsByMonografiaIdAndStatus(monografiaId, StatusDefesa.MARCADA);
        if (preDefesaAtiva) {
            throw new RuntimeException("Já existe uma pré-defesa ativa para esta monografia.");
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
        preDefesa.setStatus(StatusDefesa.MARCADA); 
 
        // Atualiza o status da monografia para EM_PRE_DEFESA
        monografia.setStatus(StatusMonografia.EM_PRE_DEFESA);
        monografiaRepository.save(monografia);
    
        // Salva a pré-defesa no banco de dados
        PreDefesa savedPreDefesa = preDefesaRepository.save(preDefesa);
    
        // Cria o DTO para retornar a resposta
        PreDefesaDTO preDefesaDTO = new PreDefesaDTO();
        preDefesaDTO.setId(savedPreDefesa.getId());
        preDefesaDTO.setMonografiaId(savedPreDefesa.getMonografia().getId());
        preDefesaDTO.setTemaMonografia(savedPreDefesa.getMonografia().getTema());
        preDefesaDTO.setDataInicio(savedPreDefesa.getDataInicio());
        preDefesaDTO.setDataFim(savedPreDefesa.getDataFim());
        preDefesaDTO.setPresidenteId(savedPreDefesa.getPresidente().getId());
        preDefesaDTO.setVogalId(savedPreDefesa.getVogal().getId());
        preDefesaDTO.setStatus(savedPreDefesa.getStatus());
    
        return preDefesaDTO;
    }

@Transactional
public PreDefesaResponseDTO buscarPreDefesaPorId(UUID preDefesaId) {
    try {
        PreDefesa preDefesa = preDefesaRepository.findById(preDefesaId)
                .orElseThrow(() -> new RuntimeException("Pré-defesa não encontrada com o ID: " + preDefesaId));

        return toDTO(preDefesa); // Aqui estava o erro: use toDTO em vez de toResponseDTO
    } catch (Exception e) {
        throw new RuntimeException("Unable to access LOB stream", e);
    }
}

public List<PreDefesaResponseDTO> listarTodasPreDefesasMarcadas() {
    List<PreDefesa> preDefesas = preDefesaRepository.findByStatus(StatusDefesa.MARCADA);
    return preDefesas.stream()
            .map(this::toDTO) // Aqui também estava o erro: use toDTO
            .collect(Collectors.toList());
}


private PreDefesaResponseDTO toDTO(PreDefesa preDefesa) {
    PreDefesaResponseDTO dto = new PreDefesaResponseDTO();
    dto.setId(preDefesa.getId());
    dto.setMonografiaId(preDefesa.getMonografia().getId());
    dto.setTemaMonografia(preDefesa.getMonografia().getTema());
    dto.setDataInicio(preDefesa.getDataInicio());
    dto.setDataFim(preDefesa.getDataFim());
    dto.setDescricao(preDefesa.getDescricao());
    dto.setPresidenteId(preDefesa.getPresidente().getId());
    dto.setPresidenteNomeCompleto(preDefesa.getPresidente().getNome(), preDefesa.getPresidente().getSobrenome());
    dto.setVogalId(preDefesa.getVogal().getId());
    dto.setVogalNomeCompleto(preDefesa.getVogal().getNome(), preDefesa.getVogal().getSobrenome());
    dto.setAlunoNomeCompleto(preDefesa.getMonografia().getAluno().getNome() + " " + preDefesa.getMonografia().getAluno().getSobrenome());
    dto.setEspecialidadeNome(preDefesa.getMonografia().getEspecialidade().getNome());
    dto.setOrientadorNomeCompleto(preDefesa.getMonografia().getOrientador().getNome() + " " + preDefesa.getMonografia().getOrientador().getSobrenome());

    dto.setStatus(preDefesa.getStatus());

    return dto;
}

public List<PreDefesaResponseDTO> listarPreDefesasPorStatus(StatusDefesa status) {
    List<PreDefesa> preDefesas;

    if (status != null) {
        preDefesas = preDefesaRepository.findByStatus(status);
    } else {
        preDefesas = preDefesaRepository.findAll();
    }

    return preDefesas.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
}

public List<PreDefesaResponseDTO> listarPreDefesasPorUsuario(UUID usuarioId) {
    // Busca o usuário pelo ID
    usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + usuarioId));

    // Busca as pré-defesas onde o usuário é presidente ou vogal
    List<PreDefesa> preDefesas = preDefesaRepository.findByPresidenteIdOrVogalId(usuarioId, usuarioId);

    // Converte as pré-defesas para DTOs
    return preDefesas.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
}

@Transactional
public PreDefesaResponseDTO atualizarStatusPreDefesa(UUID preDefesaId, StatusDefesa status, UUID usuarioId, String descricao) {
    // Busca a pré-defesa pelo ID
    PreDefesa preDefesa = preDefesaRepository.findById(preDefesaId)
            .orElseThrow(() -> new RuntimeException("Pré-defesa não encontrada com o ID: " + preDefesaId));

    // Busca o usuário pelo ID
    Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + usuarioId));

    // Verifica se o usuário é o presidente ou o vogal da pré-defesa
    if (!usuario.getId().equals(preDefesa.getPresidente().getId()) &&
        !usuario.getId().equals(preDefesa.getVogal().getId())) {
        throw new RuntimeException("Apenas o presidente ou o vogal podem atualizar o status da pré-defesa.");
    }

    // Verifica se a pré-defesa está no status MARCADA
    if (preDefesa.getStatus() != StatusDefesa.MARCADA) {
        throw new RuntimeException("A pré-defesa não está no status MARCADA e não pode ser atualizada.");
    }

    // Atualiza o status da pré-defesa
    preDefesa.setStatus(status);

    // Adiciona a descrição (recomendações) à pré-defesa
    if (descricao != null && !descricao.isEmpty()) {
        if (preDefesa.getDescricao() == null) {
            preDefesa.setDescricao(descricao);
        } else {
            preDefesa.setDescricao(preDefesa.getDescricao() + "\n" + descricao);
        }
    }

    // Salva a pré-defesa atualizada
    PreDefesa savedPreDefesa = preDefesaRepository.save(preDefesa);

    // Atualiza o status da monografia com base no resultado da pré-defesa
    Monografia monografia = preDefesa.getMonografia();
    if (status == StatusDefesa.APROVADO) {
        monografia.setStatus(StatusMonografia.APROVADO); // Aprovado para seguir para a defesa final
    } else if (status == StatusDefesa.EM_REVISAO) {
        monografia.setStatus(StatusMonografia.EM_REVISAO); // Precisa de revisão
    }
    monografiaRepository.save(monografia);

    // Retorna o DTO atualizado
    return toDTO(savedPreDefesa);
}

@Transactional
public PreDefesaResponseDTO visualizarPreDefesa(UUID preDefesaId, UUID usuarioId) {
    // Busca a pré-defesa pelo ID
    PreDefesa preDefesa = preDefesaRepository.findById(preDefesaId)
            .orElseThrow(() -> new RuntimeException("Pré-defesa não encontrada com o ID: " + preDefesaId));

    // Busca o usuário pelo ID
    Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + usuarioId));

    // Verifica se o usuário tem permissão para visualizar a pré-defesa
    boolean podeVisualizar = false;

    // Verifica se o usuário é o aluno associado à monografia
    if (usuario.getId().equals(preDefesa.getMonografia().getAluno().getId())) {
        podeVisualizar = true;
    }
    // Verifica se o usuário é o orientador associado à monografia
    else if (usuario.getId().equals(preDefesa.getMonografia().getOrientador().getId())) {
        podeVisualizar = true;
    }
    // Verifica se o usuário é o presidente da pré-defesa
    else if (usuario.getId().equals(preDefesa.getPresidente().getId())) {
        podeVisualizar = true;
    }
    // Verifica se o usuário é o vogal da pré-defesa
    else if (usuario.getId().equals(preDefesa.getVogal().getId())) {
        podeVisualizar = true;
    }

    // Se o usuário não tiver permissão, lança uma exceção
    if (!podeVisualizar) {
        throw new RuntimeException("Você não tem permissão para visualizar esta pré-defesa.");
    }

    // Retorna as informações da pré-defesa no formato DTO
    return toDTO(preDefesa);
}

}
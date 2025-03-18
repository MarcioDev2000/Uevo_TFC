package com.monografia.EvoluTCC.services;
import com.monografia.EvoluTCC.repositories.MonografiaRepository;
import com.monografia.EvoluTCC.Enums.StatusDefesa;
import com.monografia.EvoluTCC.Enums.StatusMonografia;
import com.monografia.EvoluTCC.dto.DefesaDTO;
import com.monografia.EvoluTCC.models.Defesa;
import com.monografia.EvoluTCC.models.Monografia;
import com.monografia.EvoluTCC.models.PreDefesa;
import com.monografia.EvoluTCC.models.Usuario;
import com.monografia.EvoluTCC.repositories.DefesaRepository;
import com.monografia.EvoluTCC.repositories.PreDefesaRepository;
import com.monografia.EvoluTCC.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DefesaService {

        @Autowired
    private DefesaRepository defesaRepository;

    @Autowired
    private MonografiaRepository monografiaRepository;

    @Autowired
    private PreDefesaRepository preDefesaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Defesa marcarDefesa(UUID preDefesaId, LocalDateTime dataInicio, LocalDateTime dataFim, UUID presidenteId, UUID vogalId) {
        // Busca a pré-defesa pelo ID
        PreDefesa preDefesa = preDefesaRepository.findById(preDefesaId)
                .orElseThrow(() -> new RuntimeException("Pré-defesa não encontrada com o ID: " + preDefesaId));
    
        // Busca a monografia associada à pré-defesa
        Monografia monografia = preDefesa.getMonografia();
    
        // Verifica se a monografia está no status EM_PRE_DEFESA
        if (monografia.getStatus() != StatusMonografia.APROVADO) {
            throw new RuntimeException("A monografia precisa estar no status APROVADO para marcar a defesa.");
        }
    
        // Verifica se a data de início é anterior à data de fim
        if (dataInicio.isAfter(dataFim)) {
            throw new RuntimeException("A data de início deve ser anterior à data de fim.");
        }
    
        // Verifica se já existe uma defesa marcada no mesmo intervalo de tempo
        boolean conflitoDeDatas = defesaRepository.existsByDataInicioBetweenAndMonografiaEspecialidadeId(
                dataInicio, dataFim, monografia.getEspecialidade().getId());
        if (conflitoDeDatas) {
            throw new RuntimeException("Já existe uma defesa marcada para o mesmo intervalo de tempo e especialidade.");
        }
    
        // Busca o presidente e o vogal
        Usuario presidente = usuarioRepository.findById(presidenteId)
                .orElseThrow(() -> new RuntimeException("Presidente não encontrado com o ID: " + presidenteId));
        Usuario vogal = usuarioRepository.findById(vogalId)
                .orElseThrow(() -> new RuntimeException("Vogal não encontrado com o ID: " + vogalId));
    
        // Verifica se o presidente e o vogal pertencem à mesma especialidade da monografia
        if (!presidente.getEspecialidade().getId().equals(monografia.getEspecialidade().getId())) {
            throw new RuntimeException("O presidente selecionado não pertence à especialidade da monografia.");
        }
        if (!vogal.getEspecialidade().getId().equals(monografia.getEspecialidade().getId())) {
            throw new RuntimeException("O vogal selecionado não pertence à especialidade da monografia.");
        }
    
        // Cria a defesa
        Defesa defesa = new Defesa();
        defesa.setMonografia(monografia);
        defesa.setDataInicio(dataInicio);
        defesa.setDataFim(dataFim);
        defesa.setPresidente(presidente);
        defesa.setVogal(vogal);
        defesa.setStatus(StatusDefesa.MARCADA);
        defesa.setPreDefesa(preDefesa); // Associa a defesa à pré-defesa
    
        // Atualiza o status da monografia para EM_DEFESA
        monografia.setStatus(StatusMonografia.EM_DEFESA);
        monografiaRepository.save(monografia);
    
        // Salva a defesa no banco de dados
        return defesaRepository.save(defesa);
    }

    public List<DefesaDTO> listarDefesasPorAluno(UUID alunoId) {
        return defesaRepository.findByMonografiaAlunoIdAndStatus(alunoId, StatusDefesa.MARCADA).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<DefesaDTO> listarDefesasPorOrientador(UUID orientadorId) {
        return defesaRepository.findByMonografiaOrientadorIdAndStatus(orientadorId, StatusDefesa.MARCADA).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<DefesaDTO> listarDefesasPorPresidenteOuVogal(UUID usuarioId) {
        return defesaRepository.findByPresidenteIdOrVogalIdAndStatus(usuarioId, usuarioId, StatusDefesa.MARCADA).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Defesa aplicarNotaObservacao(UUID defesaId, Float nota, String observacoes) {
        Defesa defesa = defesaRepository.findById(defesaId)
                .orElseThrow(() -> new RuntimeException("Defesa não encontrada com o ID: " + defesaId));
    
        // Define a nota e as observações
        defesa.setNota(nota);
        defesa.setObservacoes(observacoes);
    
        // Atualiza o status da defesa e da monografia
        if (nota >= 10.0) { // Defesa aprovada
            defesa.setStatus(StatusDefesa.APROVADO);
            defesa.getMonografia().setStatus(StatusMonografia.APROVADO);
        } else { // Defesa em revisão
            defesa.setStatus(StatusDefesa.EM_REVISAO);
            defesa.getMonografia().setStatus(StatusMonografia.EM_REVISAO);
        }
    
        // Salva as alterações
        defesaRepository.save(defesa);
        monografiaRepository.save(defesa.getMonografia());
    
        return defesa;
    }

 private DefesaDTO toDTO(Defesa defesa) {
        DefesaDTO dto = new DefesaDTO();
        dto.setId(defesa.getId());
        dto.setMonografiaId(defesa.getMonografia().getId());
        dto.setDataInicio(defesa.getDataInicio());
        dto.setDataFim(defesa.getDataFim());
        dto.setPresidenteId(defesa.getPresidente().getId());
        dto.setVogalId(defesa.getVogal().getId());
        dto.setPresidenteId(defesa.getPresidente().getId());
        dto.setPresidenteNomeCompleto(defesa.getPresidente().getNome() + " " + defesa.getPresidente().getSobrenome());
    
        dto.setVogalId(defesa.getVogal().getId());
        dto.setVogalNomeCompleto(defesa.getVogal().getNome() + " " + defesa.getVogal().getSobrenome());
        dto.setNota(defesa.getNota());
        dto.setObservacoes(defesa.getObservacoes());
        dto.setStatus(defesa.getStatus());

        // Adicionar links dos documentos
        Monografia monografia = defesa.getMonografia();
        DocumentoUtils.adicionarLinksDocumentos(monografia);
        dto.setLinkExtratoBancario(monografia.getLinkExtratoBancario());
        dto.setLinkTermoOrientador(monografia.getLinkTermoOrientador());
        dto.setLinkDeclaracaoNotas(monografia.getLinkDeclaracaoNotas());
        dto.setLinkProjeto(monografia.getLinkProjeto());
        dto.setLinkDocumentoBi(monografia.getLinkDocumentoBi());
        dto.setLinkTermoDoAluno(monografia.getLinkTermoDoAluno());

        return dto;
    }

}

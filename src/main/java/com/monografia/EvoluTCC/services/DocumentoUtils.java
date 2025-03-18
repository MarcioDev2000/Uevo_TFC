package com.monografia.EvoluTCC.services;
import com.monografia.EvoluTCC.models.Monografia;
import java.util.UUID;
public class DocumentoUtils {
    public static void adicionarLinksDocumentos(Monografia monografia) {
        UUID monografiaId = monografia.getId();
        String baseUrl = "http://localhost:8082/monografias/" + monografiaId + "/documentos/";

        if (monografia.getExtratoBancario() != null && monografia.getExtratoBancario().length > 0) {
            monografia.setLinkExtratoBancario(baseUrl + "extrato_bancario/visualizar");
        }
        if (monografia.getDeclaracaoNotas() != null && monografia.getDeclaracaoNotas().length > 0) {
            monografia.setLinkDeclaracaoNotas(baseUrl + "declaracao_notas/visualizar");
        }
        if (monografia.getTermoOrientador() != null && monografia.getTermoOrientador().length > 0) {
            monografia.setLinkTermoOrientador(baseUrl + "termo_orientador/visualizar");
        }
        if (monografia.getProjeto() != null && monografia.getProjeto().length > 0) {
            monografia.setLinkProjeto(baseUrl + "projeto/visualizar");
        }
        if (monografia.getDocumentoBi() != null && monografia.getDocumentoBi().length > 0) {
            monografia.setLinkDocumentoBi(baseUrl + "documento_bi/visualizar");
        }
        if (monografia.getTermoDoAluno() != null && monografia.getTermoDoAluno().length > 0) {
            monografia.setLinkTermoDoAluno(baseUrl + "termo_do_aluno/visualizar");
        }
    }

   
}

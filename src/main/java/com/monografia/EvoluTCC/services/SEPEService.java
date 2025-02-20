package com.monografia.EvoluTCC.services;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class SEPEService {
    private final RestTemplate restTemplate;

    public SEPEService() {
        this.restTemplate = new RestTemplate();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> consultarBI(String numeroBI) {
        // Constrói a URL dinâmica
        @SuppressWarnings("deprecation")
        String url = UriComponentsBuilder
                .fromHttpUrl("https://www.sepe.gov.ao/ao/actions/bi.ajcall.php")
                .queryParam("bi", numeroBI)
                .toUriString();

        try {
           
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            // Logar a resposta para verificar o formato
            System.out.println(response);

            return response;
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao consultar o BI: " + e.getMessage());
        }
    }

     
    
}

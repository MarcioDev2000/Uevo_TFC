package com.monografia.EvoluTCC.config;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    // Gere uma chave secreta de 256 bits para HS256
    private static final SecretKey CHAVE_SECRETA = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long TEMPO_EXPIRACAO_TOKEN = 1000 * 15 * 60; // 15 minutos
    private static final long TEMPO_EXPIRACAO_RESET_SENHA = 1000 * 30 * 60; // 30 minutos

    public JwtUtil() {
    }

    public String gerarToken(String nomeUsuario) {
        Map<String, Object> dados = new HashMap<>();
        return criarToken(dados, nomeUsuario, TEMPO_EXPIRACAO_TOKEN);
    }

    public String gerarTokenResetSenha(String email) {
        return criarToken(new HashMap<>(), email, TEMPO_EXPIRACAO_RESET_SENHA);
    }

    public boolean validarToken(String token, String nomeUsuario) {
        return nomeUsuario.equals(extrairNomeUsuario(token)) && !tokenExpirado(token);
    }

    public String extrairNomeUsuario(String token) {
        return extrairTodasAsClaims(token).getSubject();
    }

    public boolean tokenExpirado(String token) {
        return extrairDataExpiracao(token).before(new Date());
    }

    private Date extrairDataExpiracao(String token) {
        return extrairTodasAsClaims(token).getExpiration();
    }

    private Claims extrairTodasAsClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(CHAVE_SECRETA)  // Usando a chave secreta segura
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String criarToken(Map<String, Object> dados, String sujeito, long tempoExpiracao) {
        return Jwts.builder()
                .setClaims(dados)
                .setSubject(sujeito)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tempoExpiracao))
                .signWith(CHAVE_SECRETA) // Usando a chave secreta segura
                .compact();
    }

    public boolean tokenValido(String token) {
        try {
            return extrairNomeUsuario(token) != null && !tokenExpirado(token);
        } catch (Exception e) {
            return false;
        }
    }
}
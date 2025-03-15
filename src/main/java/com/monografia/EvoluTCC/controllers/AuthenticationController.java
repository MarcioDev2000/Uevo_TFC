package com.monografia.EvoluTCC.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.monografia.EvoluTCC.config.JwtUtil;
import com.monografia.EvoluTCC.dto.ResetPasswordDto;
import com.monografia.EvoluTCC.dto.UsuarioDto;
import com.monografia.EvoluTCC.models.Usuario;
import com.monografia.EvoluTCC.services.UsuarioService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UsuarioService usuarioService; // Use a instância injetada

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login/")
public ResponseEntity<?> loginUser(@RequestBody UsuarioDto usuarioDto) {
    try {
        // Busca o usuário no banco de dados antes da autenticação
        Usuario userModel = usuarioService.findUserByEmail(usuarioDto.getEmail());

        // Verifica se o usuário existe e se sua conta está ativa
        if (userModel == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Credenciais inválidas"));
        } else if (!userModel.isStatus()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Sua conta ainda não foi ativada. Entre em contato com o administrador."));
        }

        // Autentica o usuário
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioDto.getEmail(), usuarioDto.getPassword())
        );

        // Gera o token JWT
        String token = jwtUtil.gerarToken(usuarioDto.getEmail());

        // Prepara a resposta com os dados do usuário e o token
        Map<String, Object> response = new HashMap<>();
        response.put("id", userModel.getId());
        response.put("nome", userModel.getNome());
        response.put("email", userModel.getEmail());
        response.put("tipo_de_entidade", userModel.getTipoUsuario().getNome());
        response.put("tipo_de_entidade_id", userModel.getTipoUsuario().getId());
        response.put("rota", userModel.getTipoUsuario().getRota());
        response.put("token", token);

        return ResponseEntity.ok(response);
    } catch (AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Credenciais inválidas"));
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
    }
}


    @PostMapping("/email-redefinir-palavra-passe/")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            // Use a instância de usuarioService para chamar o método
            usuarioService.forgotPassword(email);
            return ResponseEntity.ok("Email de redefinição de senha enviado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password/")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        try {
            // Use a instância de usuarioService para chamar o método
            usuarioService.resetPassword(resetPasswordDto.getToken(), resetPasswordDto.getNewPassword());
            return ResponseEntity.ok("Senha atualizada com sucesso.");
        } catch (RuntimeException e) {
            System.out.println("Erro ao redefinir a senha: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro inesperado.");
        }
    }
}
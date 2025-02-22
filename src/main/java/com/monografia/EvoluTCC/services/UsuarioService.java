package com.monografia.EvoluTCC.services;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.monografia.EvoluTCC.config.JwtUtil;
import com.monografia.EvoluTCC.dto.UsuarioDto;
import com.monografia.EvoluTCC.models.TipoUsuario;
import com.monografia.EvoluTCC.models.Usuario;
import com.monografia.EvoluTCC.producers.UserProducer;
import com.monografia.EvoluTCC.repositories.TipoUsuarioRepository;
import com.monografia.EvoluTCC.repositories.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserProducer userProducer;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SEPEService sepeService;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    public Usuario criarUsuario(UsuarioDto usuarioDto) {
        // Verifica se o email já está cadastrado
        if (usuarioRepository.findByEmail(usuarioDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }
    
        // Valida o NIF com o serviço externo
        Map<String, Object> dadosBI = sepeService.consultarBI(usuarioDto.getNif());
        if (dadosBI == null || dadosBI.isEmpty()) {
            throw new IllegalArgumentException("Não foi possível validar os dados do BI.");
        }
    
        // Busca o TipoUsuario pelo UUID
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(usuarioDto.getTipoUsuario())
                .orElseThrow(() -> new RuntimeException("Tipo de usuário não encontrado"));
    
        // Cria o novo usuário
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDto.getNome());
        usuario.setSobrenome(usuarioDto.getSobrenome());
        usuario.setEndereco(usuarioDto.getEndereco());
        usuario.setTelefone(usuarioDto.getTelefone());
        usuario.setEmail(usuarioDto.getEmail());
        usuario.setNif(usuarioDto.getNif());
        usuario.setTipoUsuario(tipoUsuario); // Define o TipoUsuario
        usuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));
    
        // Define especialidade, se fornecida
        if (usuarioDto.getEspecialidade() != null) {
            usuario.setEspecialidade(usuarioDto.getEspecialidade());
        }
    
        // Define matrícula, se fornecida
        if (usuarioDto.getMatricula() != null) {
            usuario.setMatricula(usuarioDto.getMatricula());
        }
    
        try {
            // Salva o usuário no banco de dados
            Usuario salvarUsuario = usuarioRepository.save(usuario);
            // Envia email de confirmação
            userProducer.sendEmail(salvarUsuario);
            return salvarUsuario;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("O NIF já está cadastrado no sistema.");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar usuário no banco de dados.", e);
        }
    }

    @Transactional
    public UsuarioDto atualizarUsuarioCompleto(UUID id, UsuarioDto usuarioDto) {
        // Busca o usuário pelo ID
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // Atualiza os campos do usuário
        usuario.setNome(usuarioDto.getNome());
        usuario.setSobrenome(usuarioDto.getSobrenome());
        usuario.setEndereco(usuarioDto.getEndereco());
        usuario.setTelefone(usuarioDto.getTelefone());
        usuario.setEmail(usuarioDto.getEmail());
        usuario.setNif(usuarioDto.getNif());
        usuario.setEspecialidade(usuarioDto.getEspecialidade());
        usuario.setMatricula(usuarioDto.getMatricula());

        // Busca e define o novo TipoUsuario
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(usuarioDto.getTipoUsuario())
                .orElseThrow(() -> new RuntimeException("Tipo de usuário não encontrado"));
        usuario.setTipoUsuario(tipoUsuario);

        // Atualiza a senha, se fornecida
        if (usuarioDto.getPassword() != null && !usuarioDto.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));
        }

        // Salva as alterações
        usuarioRepository.save(usuario);

        return usuarioDto;
    }

    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> listarUsuarioPorId(UUID id) {
        return usuarioRepository.findById(id);
    }

    @Transactional
    public void deletarUsuario(UUID id) {
        usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        usuarioRepository.deleteById(id);
    }

    public Usuario findUserByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o email: " + email));
    }

    @Transactional
    public void forgotPassword(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com este email."));

        String resetPasswordToken = jwtUtil.gerarTokenResetSenha(email);
        usuario.setResetPasswordToken(resetPasswordToken);

        Date expirationDate = new Date(System.currentTimeMillis() + 60000);
        usuario.setTokenExpirationDate(expirationDate);

        usuarioRepository.save(usuario);
    }

    public void resetPassword(String token, String newPassword) {
        Usuario usuario = usuarioRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (usuario.getTokenExpirationDate().before(new Date())) {
            throw new RuntimeException("Token expirado");
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        usuario.setPassword(encodedPassword);
        usuario.setResetPasswordToken(null);
        usuario.setTokenExpirationDate(null);

        usuarioRepository.save(usuario);
    }
}
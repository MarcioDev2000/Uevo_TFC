package com.monografia.EvoluTCC.config;
import org.springframework.stereotype.Component;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.monografia.EvoluTCC.services.UsuarioDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final UsuarioDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UsuarioDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@SuppressWarnings("null") HttpServletRequest request, 
                                    @SuppressWarnings("null") HttpServletResponse response, 
                                    @SuppressWarnings("null") FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        // Verifica se o cabeçalho Authorization está presente e começa com "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Remove "Bearer " do início

            try {
                String username = jwtUtil.extrairNomeUsuario(token);
                if (jwtUtil.validarToken(token, username)) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else if (jwtUtil.tokenExpirado(token)) {
                    // Se o token expirou, gere um novo token
                    String newToken = jwtUtil.gerarToken(jwtUtil.extrairNomeUsuario(token));
                    response.setHeader("Authorization", "Bearer " + newToken); // Envie o novo token no cabeçalho
                    UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUtil.extrairNomeUsuario(token));
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (Exception e) {
                logger.warn("Token inválido ou expirado: {}", e.getMessage());
            }
        }

       
        filterChain.doFilter(request, response);
    }
}
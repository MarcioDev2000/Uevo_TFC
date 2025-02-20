package com.monografia.EvoluTCC.dto;

import jakarta.validation.constraints.NotBlank;

public class ResetPasswordDto {

    @NotBlank(message = "Token não pode estar em branco")
    private String token;

    @NotBlank(message = "Nova senha não pode estar em branco")
    private String newPassword;

    // Getters e Setters

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
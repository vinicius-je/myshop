package com.myshop.myshop_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Entrada de {@code POST /clientes}.
 *
 * <p>DTO em vez da entidade {@code Cliente} por dois motivos: o cliente da API
 * não deve conseguir enviar um {@code id} e escolher a chave primária, e o
 * contrato HTTP fica livre para evoluir sem arrastar o modelo de banco junto.
 */
public record CriarClienteRequest(

        @NotBlank(message = "nome é obrigatório")
        String nome,

        @NotBlank(message = "email é obrigatório")
        @Email(message = "email deve ser válido")
        String email) {
}

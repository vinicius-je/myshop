package com.myshop.myshop_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/** Entrada de {@code POST /produtos}. */
public record CriarProdutoRequest(

        @NotBlank(message = "nome é obrigatório")
        String nome,

        @NotNull(message = "preco é obrigatório")
        @Positive(message = "preco deve ser maior que zero")
        BigDecimal preco) {
}

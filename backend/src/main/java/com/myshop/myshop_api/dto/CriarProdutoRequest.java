package com.myshop.myshop_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

/** Entrada de {@code POST /produtos}. */
public record CriarProdutoRequest(

        @NotBlank(message = "nome é obrigatório")
        String nome,

        @NotNull(message = "preco é obrigatório")
        @Positive(message = "preco deve ser maior que zero")
        BigDecimal preco,

        @URL(message = "imagem deve ser uma URL válida")
        @Size(max = 500, message = "imagem deve ter no máximo 500 caracteres")
        String imagem) {
}

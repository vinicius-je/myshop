package com.myshop.myshop_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Uma linha do corpo de {@code POST /pedidos}.
 *
 * <p>Repare no que <b>não</b> está aqui: preço. O preço vem do catálogo no
 * servidor. Se o request pudesse informá-lo, qualquer um compraria por R$ 0,01.
 */
public record ItemPedidoRequest(

        @NotNull(message = "produtoId é obrigatório")
        Long produtoId,

        @NotNull(message = "quantidade é obrigatória")
        @Positive(message = "quantidade deve ser maior que zero")
        Integer quantidade) {
}

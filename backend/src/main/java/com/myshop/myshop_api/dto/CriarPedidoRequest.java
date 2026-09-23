package com.myshop.myshop_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Entrada de {@code POST /pedidos}.
 *
 * <p>{@code @Valid} na lista faz o Bean Validation descer até cada
 * {@link ItemPedidoRequest}. Sem essa anotação, as regras dos itens seriam
 * silenciosamente ignoradas.
 */
public record CriarPedidoRequest(

        @NotNull(message = "clienteId é obrigatório")
        Long clienteId,

        @NotEmpty(message = "pedido deve conter ao menos um item")
        @Valid
        List<ItemPedidoRequest> itens) {
}

package com.myshop.myshop_api.dto;

import com.myshop.myshop_api.domain.ItemPedido;

import java.math.BigDecimal;

/** Uma linha da resposta de pedido. */
public record ItemPedidoResponse(
        Long id,
        Long produtoId,
        String produtoNome,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal) {

    public static ItemPedidoResponse de(ItemPedido item) {
        return new ItemPedidoResponse(
                item.getId(),
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.subtotal());
    }
}

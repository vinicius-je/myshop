package com.myshop.myshop_api.exception;

/** Lançada ao tentar pagar um pedido cancelado. */
public class PedidoCanceladoException extends RuntimeException {

    public PedidoCanceladoException(Long pedidoId) {
        super("Pedido %d está cancelado e não pode ser pago".formatted(pedidoId));
    }
}

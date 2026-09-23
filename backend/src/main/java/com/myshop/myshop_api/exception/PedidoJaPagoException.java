package com.myshop.myshop_api.exception;

/** Lançada ao tentar pagar um pedido que já está com status PAGO. */
public class PedidoJaPagoException extends RuntimeException {

    public PedidoJaPagoException(Long pedidoId) {
        super("Pedido %d já está pago".formatted(pedidoId));
    }
}

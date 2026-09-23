package com.myshop.myshop_api.domain;

/**
 * Estados possíveis de um pedido.
 *
 * <p>Um enum (e não uma String solta) impede que o pedido assuma um estado
 * inexistente: o compilador passa a ser o primeiro validador da regra.
 */
public enum StatusPedido {

    PENDENTE,
    PAGO,
    CANCELADO
}

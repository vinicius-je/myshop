package com.myshop.myshop_api.dto;

import com.myshop.myshop_api.domain.Cliente;

/** Saída de {@code POST /clientes}. */
public record ClienteResponse(Long id, String nome, String email) {

    public static ClienteResponse de(Cliente cliente) {
        return new ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getEmail());
    }
}

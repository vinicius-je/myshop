package com.myshop.myshop_api.dto;

import com.myshop.myshop_api.domain.Produto;

import java.math.BigDecimal;

/** Saída de {@code POST /produtos}. */
public record ProdutoResponse(Long id, String nome, BigDecimal preco) {

    public static ProdutoResponse de(Produto produto) {
        return new ProdutoResponse(produto.getId(), produto.getNome(), produto.getPreco());
    }
}

package com.myshop.myshop_api.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Entrada de {@code POST /pedidos/{id}/pagamento}.
 *
 * <p>O tipo é {@code String}, e não um enum, de propósito: um enum
 * {@code TipoPagamento} precisaria ganhar uma constante nova a cada forma de
 * pagamento criada — ou seja, <b>modificar código existente</b>, exatamente o
 * que o OCP quer evitar. Com String, a validação do valor é feita contra o
 * registro de implementações do Spring, que se atualiza sozinho.
 */
public record PagamentoRequest(

        @NotBlank(message = "tipo é obrigatório")
        String tipo) {
}

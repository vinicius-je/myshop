package com.myshop.myshop_api.dto;

import com.myshop.myshop_api.domain.Pedido;
import com.myshop.myshop_api.domain.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Saída de {@code POST /pedidos}, {@code GET /pedidos/{id}} e
 * {@code POST /pedidos/{id}/pagamento}.
 *
 * <p>Devolver a entidade {@code Pedido} direto provocaria dois problemas:
 * serialização de associações lazy (o famoso erro de proxy do Hibernate) e um
 * contrato de API que muda sozinho toda vez que alguém acrescenta um campo no
 * banco.
 */
public record PedidoResponse(
        Long id,
        Long clienteId,
        String clienteNome,
        LocalDateTime data,
        StatusPedido status,
        BigDecimal valorTotal,
        List<ItemPedidoResponse> itens) {

    public static PedidoResponse de(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.getCliente().getId(),
                pedido.getCliente().getNome(),
                pedido.getData(),
                pedido.getStatus(),
                pedido.getValorTotal(),
                pedido.getItens().stream().map(ItemPedidoResponse::de).toList());
    }
}

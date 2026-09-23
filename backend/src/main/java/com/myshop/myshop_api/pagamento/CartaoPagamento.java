package com.myshop.myshop_api.pagamento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Pagamento via cartão de crédito.
 *
 * <p>Comportamento próprio: passa por autorização na adquirente antes de
 * capturar o valor. Mesmo contrato, execução diferente — é isso que o
 * polimorfismo resolve.
 */
@Component("CARTAO")
public class CartaoPagamento implements Pagamento {

    private static final Logger log = LoggerFactory.getLogger(CartaoPagamento.class);

    @Override
    public void pagar(BigDecimal valor) {
        log.info("Cartão: solicitando autorização de R$ {} à adquirente", valor);
        log.info("Cartão: autorização aprovada, valor capturado");
    }
}

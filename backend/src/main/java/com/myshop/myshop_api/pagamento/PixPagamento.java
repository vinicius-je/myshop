package com.myshop.myshop_api.pagamento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Pagamento via Pix.
 *
 * <p>O nome do bean ({@code "PIX"}) é o identificador usado pela API no corpo
 * de {@code POST /pedidos/{id}/pagamento}. O Spring monta um
 * {@code Map<String, Pagamento>} com esses nomes, e é assim que o
 * {@code PagamentoService} escolhe a implementação sem nenhum if/else.
 */
@Component("PIX")
public class PixPagamento implements Pagamento {

    private static final Logger log = LoggerFactory.getLogger(PixPagamento.class);

    @Override
    public void pagar(BigDecimal valor) {
        log.info("Pix: gerando QR Code de R$ {} — liquidação imediata", valor);
    }
}

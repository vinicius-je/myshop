package com.myshop.myshop_api.pagamento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Pagamento via boleto bancário.
 *
 * <p>Comportamento próprio: emite a linha digitável com data de vencimento.
 */
@Component("BOLETO")
public class BoletoPagamento implements Pagamento {

    private static final Logger log = LoggerFactory.getLogger(BoletoPagamento.class);

    private static final int DIAS_PARA_VENCIMENTO = 3;

    @Override
    public void pagar(BigDecimal valor) {
        LocalDate vencimento = LocalDate.now().plusDays(DIAS_PARA_VENCIMENTO);
        log.info("Boleto: emitindo linha digitável de R$ {} com vencimento em {}", valor, vencimento);
    }
}

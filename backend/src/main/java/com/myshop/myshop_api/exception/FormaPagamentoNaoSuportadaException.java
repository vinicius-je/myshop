package com.myshop.myshop_api.exception;

import java.util.Collection;

/**
 * Lançada quando o tipo de pagamento informado não corresponde a nenhuma
 * implementação registrada.
 *
 * <p>A mensagem lista as formas disponíveis lendo o registro do Spring. Ao
 * adicionar uma nova implementação, esta mensagem passa a citá-la sozinha —
 * nenhuma lista fixa precisa ser mantida aqui.
 */
public class FormaPagamentoNaoSuportadaException extends RuntimeException {

    public FormaPagamentoNaoSuportadaException(String tipoInformado, Collection<String> disponiveis) {
        super("Forma de pagamento '%s' não suportada. Disponíveis: %s"
                .formatted(tipoInformado, String.join(", ", disponiveis)));
    }
}

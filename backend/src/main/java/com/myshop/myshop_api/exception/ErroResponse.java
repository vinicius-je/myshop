package com.myshop.myshop_api.exception;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Corpo padrão de erro devolvido pela API.
 *
 * @param timestamp momento do erro
 * @param status    código HTTP
 * @param erro      descrição curta do status
 * @param mensagem  explicação legível do que deu errado
 * @param detalhes  erros de validação campo a campo (vazio nos demais casos)
 */
public record ErroResponse(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        List<String> detalhes) {

    public static ErroResponse de(int status, String erro, String mensagem) {
        return new ErroResponse(LocalDateTime.now(), status, erro, mensagem, List.of());
    }
}

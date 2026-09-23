package com.myshop.myshop_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Traduz exceções de domínio em respostas HTTP.
 *
 * <p><b>SRP</b> — este é o único lugar do projeto que decide status code e
 * formato de erro. Services e entidades apenas lançam exceções com significado
 * de negócio; nenhum deles importa {@code HttpStatus}. Se amanhã a aplicação
 * virar um consumidor de fila em vez de uma API REST, o domínio não muda — só
 * esta classe é substituída.
 */
@RestControllerAdvice
public class TratadorDeErros {

    /** 404 — cliente, produto ou pedido inexistente. */
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratarNaoEncontrado(RecursoNaoEncontradoException ex) {
        return construir(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /** 409 — o pedido existe, mas seu estado atual não permite a operação. */
    @ExceptionHandler({PedidoJaPagoException.class, PedidoCanceladoException.class})
    public ResponseEntity<ErroResponse> tratarConflitoDeEstado(RuntimeException ex) {
        return construir(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** 400 — tipo de pagamento desconhecido. */
    @ExceptionHandler(FormaPagamentoNaoSuportadaException.class)
    public ResponseEntity<ErroResponse> tratarPagamentoInvalido(FormaPagamentoNaoSuportadaException ex) {
        return construir(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /** 400 — falha de Bean Validation nos DTOs de entrada. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarValidacao(MethodArgumentNotValidException ex) {
        List<String> detalhes = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> "%s: %s".formatted(erro.getField(), erro.getDefaultMessage()))
                .toList();

        ErroResponse corpo = new ErroResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Dados de entrada inválidos",
                detalhes);

        return ResponseEntity.badRequest().body(corpo);
    }

    private ResponseEntity<ErroResponse> construir(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status)
                .body(ErroResponse.de(status.value(), status.getReasonPhrase(), mensagem));
    }
}

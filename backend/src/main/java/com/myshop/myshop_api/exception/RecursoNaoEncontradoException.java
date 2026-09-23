package com.myshop.myshop_api.exception;

/**
 * Lançada quando um id informado não existe no banco.
 *
 * <p>Uma única exceção cobre cliente, produto e pedido inexistentes: os três
 * casos têm a mesma causa e o mesmo status HTTP (404). Criar três classes
 * idênticas mudando só o nome seria ruído, não design.
 */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String recurso, Long id) {
        super("%s de id %d não encontrado".formatted(recurso, id));
    }
}

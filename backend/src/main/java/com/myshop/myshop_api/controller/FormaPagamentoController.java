package com.myshop.myshop_api.controller;

import com.myshop.myshop_api.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Expõe as formas de pagamento aceitas, para o frontend montar a tela de
 * checkout sem manter uma lista fixa própria.
 */
@Tag(name = "Formas de pagamento", description = "Consulta das formas de pagamento aceitas")
@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {

    private final PagamentoService pagamentoService;

    public FormaPagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @Operation(summary = "Lista as formas de pagamento disponíveis")
    @ApiResponse(responseCode = "200", description = "Identificadores aceitos em POST /pedidos/{id}/pagamento")
    @GetMapping
    public ResponseEntity<List<String>> listar() {
        return ResponseEntity.ok(pagamentoService.formasDisponiveis());
    }
}

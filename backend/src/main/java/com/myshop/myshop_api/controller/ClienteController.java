package com.myshop.myshop_api.controller;

import com.myshop.myshop_api.dto.ClienteResponse;
import com.myshop.myshop_api.dto.CriarClienteRequest;
import com.myshop.myshop_api.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <b>SRP</b> — a camada HTTP: recebe request, dispara validação, delega ao
 * service e devolve status. Nenhuma regra de negócio mora aqui.
 */
@Tag(name = "Clientes", description = "Cadastro e consulta de clientes")
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /** {@code @Valid} dispara o Bean Validation; falhas viram 400 no TratadorDeErros. */
    @Operation(summary = "Cadastra um cliente")
    @ApiResponse(responseCode = "201", description = "Cliente criado")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @PostMapping
    public ResponseEntity<ClienteResponse> criar(@Valid @RequestBody CriarClienteRequest request) {
        ClienteResponse corpo = ClienteResponse.de(clienteService.criar(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(corpo);
    }

    @Operation(summary = "Lista os clientes cadastrados")
    @ApiResponse(responseCode = "200", description = "Clientes encontrados")
    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
        return ResponseEntity.ok(clienteService.listar().stream().map(ClienteResponse::de).toList());
    }
}

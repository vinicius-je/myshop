package com.myshop.myshop_api.controller;

import com.myshop.myshop_api.dto.CriarProdutoRequest;
import com.myshop.myshop_api.dto.ProdutoResponse;
import com.myshop.myshop_api.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Produtos", description = "Cadastro e consulta de produtos")
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Operation(summary = "Cadastra um produto")
    @ApiResponse(responseCode = "201", description = "Produto criado")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(@Valid @RequestBody CriarProdutoRequest request) {
        ProdutoResponse corpo = ProdutoResponse.de(produtoService.criar(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(corpo);
    }

    @Operation(summary = "Lista os produtos do catálogo")
    @ApiResponse(responseCode = "200", description = "Produtos encontrados")
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listar() {
        return ResponseEntity.ok(produtoService.listar().stream().map(ProdutoResponse::de).toList());
    }

    @Operation(summary = "Busca um produto por id")
    @ApiResponse(responseCode = "200", description = "Produto encontrado")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(ProdutoResponse.de(produtoService.buscarPorId(id)));
    }
}

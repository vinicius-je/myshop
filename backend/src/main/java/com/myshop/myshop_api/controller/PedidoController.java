package com.myshop.myshop_api.controller;

import com.myshop.myshop_api.dto.CriarPedidoRequest;
import com.myshop.myshop_api.dto.PagamentoRequest;
import com.myshop.myshop_api.dto.PedidoResponse;
import com.myshop.myshop_api.service.PagamentoService;
import com.myshop.myshop_api.service.PedidoService;
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

/**
 * Endpoints de pedido.
 *
 * <p><b>SRP</b> — o controller depende de dois services porque expõe dois
 * assuntos distintos (montar pedido e cobrá-lo). Ele apenas encaminha: quem
 * quiser entender a regra de pagamento lê o {@code PagamentoService}, não esta
 * classe.
 */
@Tag(name = "Pedidos", description = "Criação, consulta, cancelamento e pagamento de pedidos")
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final PagamentoService pagamentoService;

    public PedidoController(PedidoService pedidoService, PagamentoService pagamentoService) {
        this.pedidoService = pedidoService;
        this.pagamentoService = pagamentoService;
    }

    @Operation(summary = "Cria um pedido")
    @ApiResponse(responseCode = "201", description = "Pedido criado")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @ApiResponse(responseCode = "404", description = "Cliente ou produto não encontrado")
    @PostMapping
    public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody CriarPedidoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.criar(request));
    }

    @Operation(summary = "Busca um pedido por id")
    @ApiResponse(responseCode = "200", description = "Pedido encontrado")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @Operation(summary = "Cancela um pedido")
    @ApiResponse(responseCode = "200", description = "Pedido cancelado")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    @ApiResponse(responseCode = "409", description = "Pedido já pago ou já cancelado")
    @PostMapping("/{id}/cancelamento")
    public ResponseEntity<PedidoResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.cancelar(id));
    }

    @Operation(summary = "Paga um pedido")
    @ApiResponse(responseCode = "200", description = "Pedido pago")
    @ApiResponse(responseCode = "400", description = "Forma de pagamento não suportada ou dados inválidos")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    @ApiResponse(responseCode = "409", description = "Pedido já pago ou cancelado")
    @PostMapping("/{id}/pagamento")
    public ResponseEntity<PedidoResponse> pagar(@PathVariable Long id,
                                                @Valid @RequestBody PagamentoRequest request) {
        return ResponseEntity.ok(pagamentoService.pagar(id, request));
    }
}

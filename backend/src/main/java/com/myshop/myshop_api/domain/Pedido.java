package com.myshop.myshop_api.domain;

import com.myshop.myshop_api.exception.PedidoCanceladoException;
import com.myshop.myshop_api.exception.PedidoJaPagoException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Pedido realizado por um cliente.
 *
 * <p><b>SRP</b> — o pedido é dono das regras que dependem apenas do próprio
 * estado: como somar o total e quando pode mudar de status. Regras que
 * precisam de outras peças (buscar cliente, persistir, cobrar) ficam no
 * {@code PedidoService} e no {@code PagamentoService}.
 *
 * <p>Note que não existe {@code setStatus} público. O status só muda pelos
 * métodos {@link #marcarComoPago()} e {@link #cancelar()}, que carregam a
 * validação junto. Um setter aberto permitiria a qualquer classe colocar o
 * pedido num estado inválido sem passar pela regra.
 */
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private LocalDateTime data;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPedido status;

    @Column(name = "valor_total", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorTotal;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    /** Construtor exigido pelo JPA. */
    protected Pedido() {
    }

    public Pedido(Cliente cliente) {
        this.cliente = cliente;
        this.data = LocalDateTime.now();
        this.status = StatusPedido.PENDENTE;
        this.valorTotal = BigDecimal.ZERO;
    }

    /**
     * Acrescenta um item e atualiza o total.
     *
     * <p>Mantém os dois lados da associação consistentes: o item aponta para o
     * pedido e o pedido conhece o item. Sem isso o JPA gravaria a linha com
     * {@code pedido_id} nulo.
     */
    public void adicionarItem(ItemPedido item) {
        item.vincularAo(this);
        this.itens.add(item);
        recalcularTotal();
    }

    /**
     * Soma os subtotais dos itens.
     *
     * <p>O total é derivado, nunca informado pelo cliente da API. Aceitar um
     * {@code valorTotal} vindo do request permitiria comprar por qualquer preço.
     */
    private void recalcularTotal() {
        this.valorTotal = itens.stream()
                .map(ItemPedido::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Verifica se o pedido está apto a receber pagamento.
     *
     * <p>Pedido cancelado não volta atrás; pedido já pago não é cobrado duas
     * vezes. Como a checagem vive aqui, qualquer forma de pagamento — atual ou
     * futura — herda a mesma proteção de graça.
     */
    public void validarQuePodeSerPago() {
        if (status == StatusPedido.CANCELADO) {
            throw new PedidoCanceladoException(id);
        }
        if (status == StatusPedido.PAGO) {
            throw new PedidoJaPagoException(id);
        }
    }

    public void marcarComoPago() {
        validarQuePodeSerPago();
        this.status = StatusPedido.PAGO;
    }

    public void cancelar() {
        if (status == StatusPedido.PAGO) {
            throw new PedidoJaPagoException(id);
        }
        this.status = StatusPedido.CANCELADO;
    }

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDateTime getData() {
        return data;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    /** Lista imutável: itens entram apenas por {@link #adicionarItem}. */
    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }
}

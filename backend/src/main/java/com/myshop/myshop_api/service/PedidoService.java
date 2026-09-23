package com.myshop.myshop_api.service;

import com.myshop.myshop_api.domain.Cliente;
import com.myshop.myshop_api.domain.ItemPedido;
import com.myshop.myshop_api.domain.Pedido;
import com.myshop.myshop_api.domain.Produto;
import com.myshop.myshop_api.dto.CriarPedidoRequest;
import com.myshop.myshop_api.dto.ItemPedidoRequest;
import com.myshop.myshop_api.dto.PedidoResponse;
import com.myshop.myshop_api.exception.RecursoNaoEncontradoException;
import com.myshop.myshop_api.repository.ClienteRepository;
import com.myshop.myshop_api.repository.PedidoRepository;
import com.myshop.myshop_api.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Regras de criação e consulta de pedido.
 *
 * <p><b>SRP</b> — este service monta pedido. Ele não cobra (isso é do
 * {@code PagamentoService}), não fala HTTP (isso é do controller) e não escreve
 * SQL (isso é do repository). Uma classe {@code PedidoManager} que criasse o
 * pedido, processasse o cartão e ainda mandasse e-mail teria três motivos
 * distintos para mudar — e é exatamente isso que o SRP proíbe.
 */
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteRepository clienteRepository,
                         ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    /**
     * Cria um pedido PENDENTE a partir do cliente e dos itens informados.
     *
     * <p>O valor total não é montado aqui: cada item calcula seu subtotal e o
     * pedido soma os subtotais. O service só orquestra.
     */
    @Transactional
    public PedidoResponse criar(CriarPedidoRequest request) {
        Cliente cliente = clienteRepository.findById(request.clienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente", request.clienteId()));

        Pedido pedido = new Pedido(cliente);

        for (ItemPedidoRequest itemRequest : request.itens()) {
            Produto produto = produtoRepository.findById(itemRequest.produtoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Produto", itemRequest.produtoId()));

            pedido.adicionarItem(new ItemPedido(produto, itemRequest.quantidade()));
        }

        return PedidoResponse.de(pedidoRepository.save(pedido));
    }

    /**
     * Busca um pedido e o converte em DTO.
     *
     * <p>A conversão acontece dentro da transação de propósito: as associações
     * são LAZY e {@code spring.jpa.open-in-view} está desligado, então os dados
     * precisam ser lidos enquanto a sessão do Hibernate ainda está aberta.
     */
    @Transactional(readOnly = true)
    public PedidoResponse buscarPorId(Long id) {
        return PedidoResponse.de(buscarEntidade(id));
    }

    /**
     * Cancela um pedido ainda não pago.
     *
     * <p>A regra de "o que pode ser cancelado" está na entidade; o service só
     * carrega o pedido e delega.
     */
    @Transactional
    public PedidoResponse cancelar(Long id) {
        Pedido pedido = buscarEntidade(id);
        pedido.cancelar();
        return PedidoResponse.de(pedido);
    }

    /**
     * Devolve a entidade, para uso do {@code PagamentoService}.
     *
     * <p>Sem {@code @Transactional} próprio: quando chamado de dentro de uma
     * transação de escrita, o pedido volta gerenciado pelo Hibernate e a
     * mudança de status é gravada por dirty checking.
     */
    public Pedido buscarEntidade(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido", id));
    }
}

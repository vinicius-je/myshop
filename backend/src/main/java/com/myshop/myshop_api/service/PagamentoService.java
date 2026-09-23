package com.myshop.myshop_api.service;

import com.myshop.myshop_api.domain.Pedido;
import com.myshop.myshop_api.dto.PagamentoRequest;
import com.myshop.myshop_api.dto.PedidoResponse;
import com.myshop.myshop_api.exception.FormaPagamentoNaoSuportadaException;
import com.myshop.myshop_api.pagamento.Pagamento;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Map;

/**
 * Processa o pagamento de um pedido.
 *
 * <p><b>SRP</b> — só cobra. Criar pedido é do {@code PedidoService}; decidir o
 * status HTTP do erro é do {@code TratadorDeErros}.
 *
 * <p><b>DIP</b> — o campo é {@code Map<String, Pagamento>}: a abstração. Esta
 * classe nunca escreve {@code PixPagamento}, {@code CartaoPagamento} ou
 * {@code BoletoPagamento} — nem sequer as importa. Confira os imports acima:
 * não há nenhuma implementação concreta de pagamento entre eles.
 *
 * <p><b>OCP</b> — é isso que torna o service fechado para modificação e aberto
 * para extensão. O Spring injeta neste Map <i>todos</i> os beans que
 * implementam {@code Pagamento}, usando o nome do bean como chave. Criar
 * {@code @Component("PAYPAL") class PaypalPagamento implements Pagamento} faz a
 * chave "PAYPAL" aparecer aqui sozinha, sem tocar em uma linha deste arquivo.
 *
 * <p>O anti-exemplo — o que <b>não</b> fazer:
 *
 * <pre>{@code
 * // Cada forma nova obriga a editar este método: fechado para extensão,
 * // aberto para modificação. É o OCP de cabeça para baixo.
 * if (tipo.equals("PIX")) {
 *     new PixPagamento().pagar(valor);
 * } else if (tipo.equals("CARTAO")) {
 *     new CartaoPagamento().pagar(valor);
 * } else if (tipo.equals("BOLETO")) {
 *     new BoletoPagamento().pagar(valor);
 * }
 * }</pre>
 */
@Service
public class PagamentoService {

    private final PedidoService pedidoService;

    /** Chave = nome do bean ("PIX", "CARTAO", "BOLETO"); valor = a implementação. */
    private final Map<String, Pagamento> formasDePagamento;

    public PagamentoService(PedidoService pedidoService, Map<String, Pagamento> formasDePagamento) {
        this.pedidoService = pedidoService;
        this.formasDePagamento = formasDePagamento;
    }

    /**
     * Cobra o valor do pedido pela forma escolhida e marca o pedido como PAGO.
     *
     * <p><b>LSP</b> — a variável é do tipo {@code Pagamento} e o service chama
     * {@code pagar(valor)} sem saber, nem se importar, qual implementação está
     * ali. Qualquer uma delas serve no lugar de qualquer outra: é isso que
     * "substituível" significa. Se alguma implementação exigisse uma chamada
     * extra antes de {@code pagar}, ou lançasse {@code UnsupportedOperationException},
     * este código quebraria — e o princípio estaria violado.
     */
    @Transactional
    public PedidoResponse pagar(Long pedidoId, PagamentoRequest request) {
        Pedido pedido = pedidoService.buscarEntidade(pedidoId);
        Pagamento formaEscolhida = resolverForma(request.tipo());

        pedido.validarQuePodeSerPago();
        formaEscolhida.pagar(pedido.getValorTotal());
        pedido.marcarComoPago();

        return PedidoResponse.de(pedido);
    }

    /**
     * Traduz o tipo recebido na API na implementação correspondente.
     *
     * <p>O {@code if} aqui não é seleção de tipo — é guarda de entrada
     * inválida. Ele não cresce quando uma forma nova é criada; continua sendo
     * exatamente este {@code if} com qualquer número de implementações. A
     * diferença em relação ao if/else proibido é essa: um escala com o número
     * de formas de pagamento, o outro não.
     */
    private Pagamento resolverForma(String tipo) {
        Pagamento forma = formasDePagamento.get(tipo.toUpperCase(Locale.ROOT));

        if (forma == null) {
            throw new FormaPagamentoNaoSuportadaException(tipo, formasDePagamento.keySet());
        }

        return forma;
    }
}

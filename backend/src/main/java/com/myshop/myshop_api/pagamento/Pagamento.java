package com.myshop.myshop_api.pagamento;

import java.math.BigDecimal;

/**
 * Contrato de uma forma de pagamento.
 *
 * <p><b>ISP</b> — a interface tem exatamente um método. Toda implementação usa
 * 100% dela; ninguém é obrigado a implementar operação que não faz sentido no
 * seu contexto. O anti-exemplo seria uma interface inchada:
 *
 * <pre>{@code
 * // NÃO fazer: boleto não estorna online, Pix não parcela,
 * // cartão não tem código de barras. Metade dos métodos viraria
 * // "throw new UnsupportedOperationException()" — o que quebraria LSP.
 * interface Pagamento {
 *     void pagar(BigDecimal valor);
 *     void estornar(BigDecimal valor);
 *     void parcelar(int vezes);
 *     String gerarCodigoDeBarras();
 *     String consultarBandeira();
 * }
 * }</pre>
 *
 * <p><b>LSP</b> — o contrato é: dado um valor válido, execute a cobrança. Toda
 * implementação precisa honrar isso. Uma implementação que lançasse exceção
 * "não suportado" ou ignorasse o valor não seria substituível pelas outras e
 * quebraria todo código que depende de {@code Pagamento}.
 *
 * <p><b>DIP</b> — serviços dependem desta abstração, nunca de
 * {@code PixPagamento} ou {@code CartaoPagamento} diretamente.
 */
public interface Pagamento {

    void pagar(BigDecimal valor);
}

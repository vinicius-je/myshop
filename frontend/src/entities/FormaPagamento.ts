/** Forma de pagamento pronta para exibição. `codigo` é o valor enviado à API. */
export interface FormaPagamento {
  codigo: string
  rotulo: string
  descricao: string
}

/**
 * Textos de exibição por código.
 *
 * OCP: quem decide *quais* formas existem é o backend (`GET /formas-pagamento`).
 * Se ele passar a aceitar "PAYPAL", a opção aparece no checkout sem mudar código,
 * só com o rótulo genérico. Para melhorar o texto, acrescenta-se uma entrada
 * neste mapa; nenhum `if` ou `switch` precisa ser editado.
 */
const apresentacao: Record<string, Omit<FormaPagamento, 'codigo'>> = {
  PIX: { rotulo: 'Pix', descricao: 'Aprovação imediata via QR Code' },
  CARTAO: { rotulo: 'Cartão de crédito', descricao: 'Autorização na operadora do cartão' },
  BOLETO: { rotulo: 'Boleto bancário', descricao: 'Vencimento em 3 dias' },
}

export function criarFormaPagamento(codigo: string): FormaPagamento {
  return { codigo, ...(apresentacao[codigo] ?? { rotulo: codigo, descricao: '' }) }
}

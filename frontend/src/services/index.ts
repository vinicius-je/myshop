import { http } from '@/api/http'
import { ClienteApiService } from './ClienteApiService'
import { FormaPagamentoApiService } from './FormaPagamentoApiService'
import { PedidoApiService } from './PedidoApiService'
import { ProdutoApiService } from './ProdutoApiService'

/**
 * Ponto único onde as implementações concretas são montadas (composition root).
 * É o único arquivo que sabe que os services usam axios; os composables recebem
 * estas instâncias como valor padrão e dependem apenas dos contratos.
 */
export const produtoService = new ProdutoApiService(http)
export const clienteService = new ClienteApiService(http)
export const pedidoService = new PedidoApiService(http)
export const formaPagamentoService = new FormaPagamentoApiService(http)

export type * from './contratos'

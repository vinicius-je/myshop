import type { ItemPedidoPayload } from '@/api/dtos'
import type { Cliente } from '@/entities/Cliente'
import type { FormaPagamento } from '@/entities/FormaPagamento'
import type { Pedido } from '@/entities/Pedido'
import type { Produto } from '@/entities/Produto'

/**
 * Contratos dos services.
 *
 * DIP: os composables dependem destas interfaces, não das classes que usam
 * axios. Em um teste, basta passar um objeto `{ listar: async () => [...] }`
 * para o composable — sem mock de HTTP, sem backend no ar.
 *
 * ISP: uma interface por recurso, cada uma só com o que a tela usa. O checkout
 * precisa de `PedidoService`, mas não de `ProdutoService`; ninguém é obrigado a
 * depender de métodos que não chama.
 */

export interface ProdutoService {
  listar(): Promise<Produto[]>
}

export interface ClienteService {
  listar(): Promise<Cliente[]>
  criar(nome: string, email: string): Promise<Cliente>
}

export interface PedidoService {
  criar(clienteId: number, itens: ItemPedidoPayload[]): Promise<Pedido>
  buscar(id: number): Promise<Pedido>
  pagar(id: number, formaPagamento: string): Promise<Pedido>
}

export interface FormaPagamentoService {
  listar(): Promise<FormaPagamento[]>
}

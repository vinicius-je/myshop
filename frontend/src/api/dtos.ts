/**
 * Contratos HTTP da MyShop API, espelhando os records `*Request`/`*Response` do backend.
 *
 * Ficam separados das entidades pelo mesmo motivo que o backend separa DTO de
 * entidade: o formato do JSON pode mudar sem arrastar a regra de negócio junto.
 * Só os services enxergam estes tipos.
 */

export type StatusPedido = 'PENDENTE' | 'PAGO' | 'CANCELADO'

export interface ProdutoDTO {
  id: number
  nome: string
  preco: number
  imagem: string | null
}

export interface ClienteDTO {
  id: number
  nome: string
  email: string
}

export interface CriarClientePayload {
  nome: string
  email: string
}

export interface ItemPedidoPayload {
  produtoId: number
  quantidade: number
}

export interface CriarPedidoPayload {
  clienteId: number
  itens: ItemPedidoPayload[]
}

export interface ItemPedidoDTO {
  id: number
  produtoId: number
  produtoNome: string
  produtoImagem: string | null
  quantidade: number
  precoUnitario: number
  subtotal: number
}

export interface PedidoDTO {
  id: number
  clienteId: number
  clienteNome: string
  data: string
  status: StatusPedido
  valorTotal: number
  itens: ItemPedidoDTO[]
}

/** Corpo de erro padrão (`ErroResponse` no backend). */
export interface ErroDTO {
  timestamp: string
  status: number
  erro: string
  mensagem: string
  detalhes: string[]
}

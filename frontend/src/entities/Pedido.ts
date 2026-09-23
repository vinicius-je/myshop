import type { PedidoDTO, StatusPedido } from '@/api/dtos'
import { Dinheiro } from './Dinheiro'

export interface ItemPedido {
  produtoId: number
  produtoNome: string
  produtoImagem: string | null
  quantidade: number
  precoUnitario: Dinheiro
  subtotal: Dinheiro
}

/** Pedido já gravado no backend. Os valores vêm calculados da API — aqui só convertemos. */
export class Pedido {
  constructor(
    readonly id: number,
    readonly clienteNome: string,
    readonly data: Date,
    readonly status: StatusPedido,
    readonly valorTotal: Dinheiro,
    readonly itens: readonly ItemPedido[],
  ) {}

  get estaPago(): boolean {
    return this.status === 'PAGO'
  }

  static deApi(dto: PedidoDTO): Pedido {
    return new Pedido(
      dto.id,
      dto.clienteNome,
      new Date(dto.data),
      dto.status,
      Dinheiro.deReais(dto.valorTotal),
      dto.itens.map((item) => ({
        produtoId: item.produtoId,
        produtoNome: item.produtoNome,
        produtoImagem: item.produtoImagem,
        quantidade: item.quantidade,
        precoUnitario: Dinheiro.deReais(item.precoUnitario),
        subtotal: Dinheiro.deReais(item.subtotal),
      })),
    )
  }
}

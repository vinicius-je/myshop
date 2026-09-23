import { Dinheiro } from './Dinheiro'
import type { Produto } from './Produto'

/**
 * Uma linha do carrinho: produto + quantidade.
 *
 * Encapsulamento: `quantidade` só muda por `alterarQuantidade`, que valida o valor.
 * Com um campo público qualquer componente poderia gravar `0`, `-3` ou `2.5`.
 */
export class ItemCarrinho {
  private _quantidade: number

  constructor(
    readonly produto: Produto,
    quantidade: number,
  ) {
    this._quantidade = ItemCarrinho.validar(quantidade)
  }

  get quantidade(): number {
    return this._quantidade
  }

  get precoUnitario(): Dinheiro {
    return this.produto.preco
  }

  /** Subtotal da linha. Mesmo cálculo do `ItemPedido.subtotal()` do backend. */
  get subtotal(): Dinheiro {
    return this.precoUnitario.multiplicar(this._quantidade)
  }

  alterarQuantidade(quantidade: number): void {
    this._quantidade = ItemCarrinho.validar(quantidade)
  }

  acrescentar(quantidade: number): void {
    this.alterarQuantidade(this._quantidade + quantidade)
  }

  private static validar(quantidade: number): number {
    if (!Number.isInteger(quantidade) || quantidade < 1) {
      throw new RangeError('Quantidade deve ser um inteiro maior que zero')
    }
    return quantidade
  }
}

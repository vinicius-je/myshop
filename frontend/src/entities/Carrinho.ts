import type { ItemPedidoPayload } from '@/api/dtos'
import { Dinheiro } from './Dinheiro'
import { ItemCarrinho } from './ItemCarrinho'
import { Produto } from './Produto'

/** Forma serializável do carrinho, usada para guardá-lo no navegador. */
export interface CarrinhoSnapshot {
  itens: { id: number; nome: string; precoCentavos: number; imagem?: string | null; quantidade: number }[]
}

/**
 * Carrinho de compras.
 *
 * SRP: o carrinho é dono das regras que dependem só dos próprios itens — não
 * duplicar produto, somar o total, montar o corpo do pedido. Ele não fala com a
 * API nem com o `localStorage`; isso fica no service e no composable.
 */
export class Carrinho {
  private _itens: ItemCarrinho[] = []

  /** Leitura apenas: itens entram e saem pelos métodos abaixo. */
  get itens(): readonly ItemCarrinho[] {
    return this._itens
  }

  get estaVazio(): boolean {
    return this._itens.length === 0
  }

  /** Soma das quantidades — o número exibido no ícone do carrinho. */
  get quantidadeTotal(): number {
    return this._itens.reduce((soma, item) => soma + item.quantidade, 0)
  }

  get total(): Dinheiro {
    return this._itens.reduce((soma, item) => soma.somar(item.subtotal), Dinheiro.ZERO)
  }

  /** Adicionar um produto que já está no carrinho soma a quantidade em vez de duplicar a linha. */
  adicionar(produto: Produto, quantidade = 1): void {
    const existente = this.buscar(produto.id)
    if (existente) {
      existente.acrescentar(quantidade)
    } else {
      this._itens.push(new ItemCarrinho(produto, quantidade))
    }
  }

  alterarQuantidade(produtoId: number, quantidade: number): void {
    this.buscar(produtoId)?.alterarQuantidade(quantidade)
  }

  remover(produtoId: number): void {
    this._itens = this._itens.filter((item) => item.produto.id !== produtoId)
  }

  limpar(): void {
    this._itens = []
  }

  /**
   * Corpo de `POST /pedidos`. Só id e quantidade: o preço é sempre o do catálogo
   * no servidor, nunca o que o navegador informa.
   */
  paraPedido(): ItemPedidoPayload[] {
    return this._itens.map((item) => ({ produtoId: item.produto.id, quantidade: item.quantidade }))
  }

  paraSnapshot(): CarrinhoSnapshot {
    return {
      itens: this._itens.map((item) => ({
        id: item.produto.id,
        nome: item.produto.nome,
        precoCentavos: item.precoUnitario.centavos,
        imagem: item.produto.imagem,
        quantidade: item.quantidade,
      })),
    }
  }

  static restaurar(snapshot: CarrinhoSnapshot): Carrinho {
    const carrinho = new Carrinho()
    for (const item of snapshot.itens) {
      const produto = new Produto(item.id, item.nome, Dinheiro.deCentavos(item.precoCentavos), item.imagem ?? null)
      carrinho.adicionar(produto, item.quantidade)
    }
    return carrinho
  }

  private buscar(produtoId: number): ItemCarrinho | undefined {
    return this._itens.find((item) => item.produto.id === produtoId)
  }
}

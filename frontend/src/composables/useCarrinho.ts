import { computed, reactive, watch } from 'vue'
import { Carrinho, type CarrinhoSnapshot } from '@/entities/Carrinho'
import type { Produto } from '@/entities/Produto'

const CHAVE_STORAGE = 'myshop:carrinho'

function restaurar(): Carrinho {
  try {
    const salvo = localStorage.getItem(CHAVE_STORAGE)
    return salvo ? Carrinho.restaurar(JSON.parse(salvo) as CarrinhoSnapshot) : new Carrinho()
  } catch {
    // Snapshot corrompido ou storage bloqueado: começa com carrinho vazio.
    return new Carrinho()
  }
}

function salvar(snapshot: CarrinhoSnapshot): void {
  try {
    localStorage.setItem(CHAVE_STORAGE, JSON.stringify(snapshot))
  } catch {
    // Sem storage o carrinho continua funcionando, só não sobrevive ao F5.
  }
}

/**
 * Estado no escopo do módulo: todas as telas que chamam `useCarrinho()` enxergam
 * o mesmo carrinho (o badge do cabeçalho, a vitrine, o checkout).
 */
const carrinho = reactive(restaurar()) as Carrinho

watch(() => carrinho.paraSnapshot(), salvar, { deep: true })

/**
 * Ponte entre a entidade `Carrinho` e os componentes Vue.
 *
 * As regras (somar total, não duplicar item, validar quantidade) estão na
 * entidade; aqui só as expomos de forma reativa e cuidamos da persistência.
 */
export function useCarrinho() {
  return {
    itens: computed(() => carrinho.itens),
    total: computed(() => carrinho.total),
    quantidadeTotal: computed(() => carrinho.quantidadeTotal),
    estaVazio: computed(() => carrinho.estaVazio),

    adicionar: (produto: Produto, quantidade = 1) => carrinho.adicionar(produto, quantidade),
    alterarQuantidade: (produtoId: number, quantidade: number) =>
      carrinho.alterarQuantidade(produtoId, quantidade),
    remover: (produtoId: number) => carrinho.remover(produtoId),
    limpar: () => carrinho.limpar(),
    paraPedido: () => carrinho.paraPedido(),
  }
}

export type UseCarrinho = ReturnType<typeof useCarrinho>

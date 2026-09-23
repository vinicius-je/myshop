import { computed, ref } from 'vue'
import { ApiError } from '@/api/ApiError'
import type { Produto } from '@/entities/Produto'
import { produtoService, type ProdutoService } from '@/services'

/** Carrega o catálogo e filtra por nome. O service é injetável para testes (DIP). */
export function useProdutos(service: ProdutoService = produtoService) {
  const produtos = ref<Produto[]>([])
  const carregando = ref(false)
  const erro = ref<string | null>(null)
  const busca = ref('')

  const produtosFiltrados = computed(() => {
    const termo = busca.value.trim().toLocaleLowerCase('pt-BR')
    if (!termo) return produtos.value
    return produtos.value.filter((p) => p.nome.toLocaleLowerCase('pt-BR').includes(termo))
  })

  async function carregar(): Promise<void> {
    carregando.value = true
    erro.value = null
    try {
      produtos.value = await service.listar()
    } catch (e) {
      erro.value = ApiError.de(e).message
    } finally {
      carregando.value = false
    }
  }

  return { produtos, produtosFiltrados, busca, carregando, erro, carregar }
}

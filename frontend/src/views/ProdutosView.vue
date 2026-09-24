<script setup lang="ts">
import { onMounted } from 'vue'
import ProdutoCard from '@/components/ProdutoCard.vue'
import { useCarrinho } from '@/composables/useCarrinho'
import { useProdutos } from '@/composables/useProdutos'

const { produtosFiltrados, busca, carregando, erro, carregar } = useProdutos()
const { itens, adicionar } = useCarrinho()

function quantidadeNoCarrinho(produtoId: number): number {
  return itens.value.find((item) => item.produto.id === produtoId)?.quantidade ?? 0
}

onMounted(carregar)
</script>

<template>
  <section>
    <div class="titulo">
      <h1>Produtos</h1>
      <input v-model="busca" type="search" class="campo busca" placeholder="Buscar produto…" />
    </div>

    <p v-if="carregando" class="aviso">Carregando catálogo…</p>
    <div v-else-if="erro" class="alerta alerta--erro">
      {{ erro }}
      <button type="button" class="botao" @click="carregar">Tentar novamente</button>
    </div>
    <p v-else-if="produtosFiltrados.length === 0" class="aviso">Nenhum produto encontrado.</p>

    <div v-else class="grade">
      <ProdutoCard
        v-for="produto in produtosFiltrados"
        :key="produto.id"
        :produto="produto"
        :no-carrinho="quantidadeNoCarrinho(produto.id)"
        @adicionar="(quantidade) => adicionar(produto, quantidade)"
      />
    </div>
  </section>
</template>

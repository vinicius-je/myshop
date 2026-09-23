<script setup lang="ts">
import { ref } from 'vue'
import type { Produto } from '@/entities/Produto'
import ProdutoImagem from './ProdutoImagem.vue'
import SeletorQuantidade from './SeletorQuantidade.vue'

defineProps<{ produto: Produto; noCarrinho: number }>()
const emit = defineEmits<{ adicionar: [quantidade: number] }>()

const quantidade = ref(1)

function adicionar() {
  emit('adicionar', quantidade.value)
  quantidade.value = 1
}
</script>

<template>
  <article class="card produto">
    <ProdutoImagem :src="produto.imagem" :alt="produto.nome" />
    <h3 class="produto__nome">{{ produto.nome }}</h3>
    <p class="produto__preco">{{ produto.preco.formatar() }}</p>
    <p v-if="noCarrinho > 0" class="produto__no-carrinho">{{ noCarrinho }} no carrinho</p>
    <div class="produto__acoes">
      <SeletorQuantidade v-model="quantidade" />
      <button type="button" class="botao botao--primario" @click="adicionar">Adicionar</button>
    </div>
  </article>
</template>

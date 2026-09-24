<script setup lang="ts">
import { useCarrinho } from '@/composables/useCarrinho'
import ProdutoImagem from './ProdutoImagem.vue'

const { itens, total } = useCarrinho()
</script>

<template>
  <section class="card resumo">
    <h2>Resumo</h2>
    <ul class="resumo__itens">
      <li v-for="item in itens" :key="item.produto.id">
        <span class="item-produto">
          <ProdutoImagem :src="item.produto.imagem" :alt="item.produto.nome" class="miniatura miniatura--pequena" />
          {{ item.quantidade }}× {{ item.produto.nome }}
        </span>
        <span>{{ item.subtotal.formatar() }}</span>
      </li>
    </ul>
    <p class="resumo__total">
      <span>Total</span>
      <strong>{{ total.formatar() }}</strong>
    </p>
  </section>
</template>

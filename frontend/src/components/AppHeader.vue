<script setup lang="ts">
import { useCarrinho } from '@/composables/useCarrinho'

const { quantidadeTotal } = useCarrinho()
</script>

<template>
  <header class="header">
    <div class="container header__conteudo">
      <RouterLink to="/" class="header__logo">MyShop</RouterLink>
      <nav class="header__nav">
        <RouterLink to="/">Produtos</RouterLink>
        <RouterLink to="/carrinho" class="header__carrinho">
          Carrinho
          <span v-if="quantidadeTotal > 0" class="badge">{{ quantidadeTotal }}</span>
        </RouterLink>
      </nav>
    </div>
  </header>
</template>

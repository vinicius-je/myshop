<script setup lang="ts">
import { ref, watch } from 'vue'

const props = defineProps<{ src: string | null; alt: string }>()

// Link quebrado ou produto sem imagem: mostra o placeholder em vez do ícone de erro do browser.
const falhou = ref(false)
watch(
  () => props.src,
  () => (falhou.value = false),
)
</script>

<template>
  <img v-if="src && !falhou" :src="src" :alt="alt" class="produto-imagem" loading="lazy" @error="falhou = true" />
  <div v-else class="produto-imagem produto-imagem--vazia" role="img" :aria-label="alt">Sem imagem</div>
</template>

<script setup lang="ts">
const quantidade = defineModel<number>({ required: true })

const props = withDefaults(defineProps<{ min?: number; max?: number }>(), { min: 1, max: 99 })

function ajustar(delta: number) {
  quantidade.value = Math.min(props.max, Math.max(props.min, quantidade.value + delta))
}

function aoDigitar(evento: Event) {
  const valor = Number.parseInt((evento.target as HTMLInputElement).value, 10)
  quantidade.value = Number.isNaN(valor) ? props.min : Math.min(props.max, Math.max(props.min, valor))
}
</script>

<template>
  <div class="quantidade">
    <button type="button" aria-label="Diminuir" :disabled="quantidade <= min" @click="ajustar(-1)">−</button>
    <input
      type="number"
      :min="min"
      :max="max"
      :value="quantidade"
      aria-label="Quantidade"
      @change="aoDigitar"
    />
    <button type="button" aria-label="Aumentar" :disabled="quantidade >= max" @click="ajustar(1)">+</button>
  </div>
</template>

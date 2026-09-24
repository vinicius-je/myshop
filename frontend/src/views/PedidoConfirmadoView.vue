<script setup lang="ts">
import { onMounted } from 'vue'
import ProdutoImagem from '@/components/ProdutoImagem.vue'
import { usePedido } from '@/composables/usePedido'

const props = defineProps<{ id: number }>()
const { pedido, carregando, erro, carregar } = usePedido()

onMounted(() => carregar(props.id))
</script>

<template>
  <section>
    <p v-if="carregando" class="aviso">Carregando pedido…</p>
    <div v-else-if="erro" class="alerta alerta--erro">{{ erro }}</div>

    <div v-else-if="pedido" class="card confirmacao">
      <h1>{{ pedido.estaPago ? 'Pedido confirmado!' : `Pedido #${pedido.id}` }}</h1>
      <p>
        Pedido <strong>#{{ pedido.id }}</strong> de {{ pedido.clienteNome }} ·
        {{ pedido.data.toLocaleString('pt-BR') }}
      </p>
      <p>Status: <span class="status" :class="`status--${pedido.status.toLowerCase()}`">{{ pedido.status }}</span></p>

      <table class="tabela">
        <thead>
          <tr>
            <th>Produto</th>
            <th class="num">Preço unitário</th>
            <th class="num">Qtd.</th>
            <th class="num">Subtotal</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in pedido.itens" :key="item.produtoId">
            <td data-rotulo="Produto">
              <span class="item-produto">
                <ProdutoImagem :src="item.produtoImagem" :alt="item.produtoNome" class="miniatura" />
                {{ item.produtoNome }}
              </span>
            </td>
            <td data-rotulo="Preço unitário" class="num">{{ item.precoUnitario.formatar() }}</td>
            <td data-rotulo="Qtd." class="num">{{ item.quantidade }}</td>
            <td data-rotulo="Subtotal" class="num">{{ item.subtotal.formatar() }}</td>
          </tr>
        </tbody>
        <tfoot>
          <tr>
            <td colspan="3">Total</td>
            <td class="num"><strong>{{ pedido.valorTotal.formatar() }}</strong></td>
          </tr>
        </tfoot>
      </table>

      <RouterLink to="/" class="botao botao--primario">Voltar à loja</RouterLink>
    </div>
  </section>
</template>

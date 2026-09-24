<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import ResumoCarrinho from '@/components/ResumoCarrinho.vue'
import { useCheckout } from '@/composables/useCheckout'

const router = useRouter()
const {
  clientes,
  formas,
  clienteId,
  formaPagamento,
  carregando,
  enviando,
  erro,
  podeFinalizar,
  carregar,
  cadastrarCliente,
  finalizar,
} = useCheckout()

const mostrarCadastro = ref(false)
const novoCliente = reactive({ nome: '', email: '' })

async function salvarCliente() {
  if (await cadastrarCliente(novoCliente.nome, novoCliente.email)) {
    novoCliente.nome = ''
    novoCliente.email = ''
    mostrarCadastro.value = false
  }
}

async function confirmar() {
  const pedido = await finalizar()
  if (pedido) router.push({ name: 'pedido-confirmado', params: { id: pedido.id } })
}

onMounted(carregar)
</script>

<template>
  <section>
    <div class="titulo">
      <h1>Finalizar compra</h1>
    </div>

    <p v-if="carregando" class="aviso">Carregando…</p>

    <div v-else class="checkout">
      <form class="checkout__form" @submit.prevent="confirmar">
        <fieldset class="card">
          <legend>Cliente</legend>
          <select v-model="clienteId" class="campo" required>
            <option :value="null" disabled>Selecione o cliente</option>
            <option v-for="cliente in clientes" :key="cliente.id" :value="cliente.id">
              {{ cliente.nome }} — {{ cliente.email }}
            </option>
          </select>

          <button type="button" class="botao botao--link" @click="mostrarCadastro = !mostrarCadastro">
            {{ mostrarCadastro ? 'Cancelar cadastro' : 'Cadastrar novo cliente' }}
          </button>

          <div v-if="mostrarCadastro" class="novo-cliente">
            <input v-model="novoCliente.nome" class="campo" placeholder="Nome" autocomplete="name" />
            <input v-model="novoCliente.email" class="campo" type="email" placeholder="E-mail" autocomplete="email" />
            <button
              type="button"
              class="botao"
              :disabled="!novoCliente.nome || !novoCliente.email"
              @click="salvarCliente"
            >
              Salvar cliente
            </button>
          </div>
        </fieldset>

        <fieldset class="card">
          <legend>Forma de pagamento</legend>
          <p v-if="formas.length === 0" class="aviso">Nenhuma forma de pagamento disponível.</p>
          <label
            v-for="forma in formas"
            :key="forma.codigo"
            class="opcao"
            :class="{ 'opcao--selecionada': formaPagamento === forma.codigo }"
          >
            <input v-model="formaPagamento" type="radio" name="forma-pagamento" :value="forma.codigo" />
            <span>
              <strong>{{ forma.rotulo }}</strong>
              <small v-if="forma.descricao">{{ forma.descricao }}</small>
            </span>
          </label>
        </fieldset>

        <div v-if="erro" class="alerta alerta--erro" role="alert">{{ erro }}</div>

        <div class="acoes">
          <RouterLink to="/carrinho" class="botao">Voltar ao carrinho</RouterLink>
          <button type="submit" class="botao botao--primario" :disabled="!podeFinalizar">
            {{ enviando ? 'Processando…' : 'Confirmar pedido' }}
          </button>
        </div>
      </form>

      <ResumoCarrinho />
    </div>
  </section>
</template>

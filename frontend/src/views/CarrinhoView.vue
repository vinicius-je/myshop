<script setup lang="ts">
import ProdutoImagem from '@/components/ProdutoImagem.vue'
import SeletorQuantidade from '@/components/SeletorQuantidade.vue'
import { useCarrinho } from '@/composables/useCarrinho'

const { itens, total, estaVazio, alterarQuantidade, remover, limpar } = useCarrinho()
</script>

<template>
  <section>
    <div class="titulo">
      <h1>Carrinho</h1>
      <button v-if="!estaVazio" type="button" class="botao botao--link" @click="limpar">Esvaziar carrinho</button>
    </div>

    <div v-if="estaVazio" class="card vazio">
      <p>Seu carrinho está vazio.</p>
      <RouterLink to="/" class="botao botao--primario">Ver produtos</RouterLink>
    </div>

    <template v-else>
      <div class="card tabela-wrapper">
        <table class="tabela">
          <thead>
            <tr>
              <th>Produto</th>
              <th class="num">Preço unitário</th>
              <th>Quantidade</th>
              <th class="num">Subtotal</th>
              <th><span class="sr-only">Ações</span></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in itens" :key="item.produto.id">
              <td data-rotulo="Produto">
                <span class="item-produto">
                  <ProdutoImagem :src="item.produto.imagem" :alt="item.produto.nome" class="miniatura" />
                  {{ item.produto.nome }}
                </span>
              </td>
              <td data-rotulo="Preço unitário" class="num">{{ item.precoUnitario.formatar() }}</td>
              <td data-rotulo="Quantidade">
                <SeletorQuantidade
                  :model-value="item.quantidade"
                  @update:model-value="(qtd) => alterarQuantidade(item.produto.id, qtd)"
                />
              </td>
              <td data-rotulo="Subtotal" class="num">{{ item.subtotal.formatar() }}</td>
              <td>
                <button type="button" class="botao botao--link" @click="remover(item.produto.id)">Remover</button>
              </td>
            </tr>
          </tbody>
          <tfoot>
            <tr>
              <td colspan="3">Total</td>
              <td class="num"><strong>{{ total.formatar() }}</strong></td>
              <td></td>
            </tr>
          </tfoot>
        </table>
      </div>

      <div class="acoes">
        <RouterLink to="/" class="botao">Continuar comprando</RouterLink>
        <RouterLink to="/checkout" class="botao botao--primario">Finalizar compra</RouterLink>
      </div>
    </template>
  </section>
</template>

import { createRouter, createWebHistory } from 'vue-router'
import { useCarrinho } from '@/composables/useCarrinho'
import ProdutosView from '@/views/ProdutosView.vue'

export const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'produtos', component: ProdutosView },
    { path: '/carrinho', name: 'carrinho', component: () => import('@/views/CarrinhoView.vue') },
    {
      path: '/checkout',
      name: 'checkout',
      component: () => import('@/views/CheckoutView.vue'),
      // Não há o que finalizar com o carrinho vazio.
      beforeEnter: () => (useCarrinho().estaVazio.value ? { name: 'carrinho' } : true),
    },
    {
      path: '/pedidos/:id',
      name: 'pedido-confirmado',
      component: () => import('@/views/PedidoConfirmadoView.vue'),
      props: (rota) => ({ id: Number(rota.params.id) }),
    },
    { path: '/:pathMatch(.*)*', redirect: { name: 'produtos' } },
  ],
})

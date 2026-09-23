import { computed, ref, watch } from 'vue'
import { ApiError } from '@/api/ApiError'
import type { Cliente } from '@/entities/Cliente'
import type { FormaPagamento } from '@/entities/FormaPagamento'
import type { Pedido } from '@/entities/Pedido'
import {
  clienteService as clienteServicePadrao,
  formaPagamentoService as formaPagamentoServicePadrao,
  pedidoService as pedidoServicePadrao,
  type ClienteService,
  type FormaPagamentoService,
  type PedidoService,
} from '@/services'
import { useCarrinho, type UseCarrinho } from './useCarrinho'

export interface CheckoutDependencias {
  clienteService: ClienteService
  pedidoService: PedidoService
  formaPagamentoService: FormaPagamentoService
  carrinho: UseCarrinho
}

/**
 * Regras da finalização de compra.
 *
 * Fluxo: `POST /pedidos` (pedido nasce PENDENTE) e, em seguida,
 * `POST /pedidos/{id}/pagamento`. São duas chamadas porque o backend separa
 * montar pedido de cobrá-lo (SRP no `PedidoService`/`PagamentoService`).
 *
 * DIP: todas as dependências chegam por parâmetro, com as implementações reais
 * como padrão. A tela chama `useCheckout()`; um teste chama
 * `useCheckout({ pedidoService: fake, ... })`.
 */
export function useCheckout(deps: Partial<CheckoutDependencias> = {}) {
  const {
    clienteService = clienteServicePadrao,
    pedidoService = pedidoServicePadrao,
    formaPagamentoService = formaPagamentoServicePadrao,
    carrinho = useCarrinho(),
  } = deps

  const clientes = ref<Cliente[]>([])
  const formas = ref<FormaPagamento[]>([])
  const clienteId = ref<number | null>(null)
  const formaPagamento = ref<string | null>(null)

  const carregando = ref(false)
  const enviando = ref(false)
  const erro = ref<string | null>(null)

  /**
   * Pedido já criado cujo pagamento falhou. Se o usuário tentar de novo (ex.:
   * trocando a forma de pagamento), reaproveitamos o mesmo pedido em vez de
   * criar outro PENDENTE duplicado no banco.
   */
  const pedidoPendente = ref<Pedido | null>(null)

  // Trocar de cliente invalida o pedido pendente: ele pertence ao cliente anterior.
  watch(clienteId, () => {
    pedidoPendente.value = null
  })

  const podeFinalizar = computed(
    () =>
      !carrinho.estaVazio.value &&
      clienteId.value !== null &&
      formaPagamento.value !== null &&
      !enviando.value,
  )

  async function carregar(): Promise<void> {
    carregando.value = true
    erro.value = null
    try {
      const [listaClientes, listaFormas] = await Promise.all([
        clienteService.listar(),
        formaPagamentoService.listar(),
      ])
      clientes.value = listaClientes
      formas.value = listaFormas
    } catch (e) {
      erro.value = ApiError.de(e).message
    } finally {
      carregando.value = false
    }
  }

  async function cadastrarCliente(nome: string, email: string): Promise<boolean> {
    erro.value = null
    try {
      const novo = await clienteService.criar(nome.trim(), email.trim())
      clientes.value = [...clientes.value, novo]
      clienteId.value = novo.id
      return true
    } catch (e) {
      erro.value = mensagemDe(e)
      return false
    }
  }

  /** Cria (ou reaproveita) o pedido, paga e esvazia o carrinho. Devolve o pedido pago. */
  async function finalizar(): Promise<Pedido | null> {
    if (!podeFinalizar.value) return null

    enviando.value = true
    erro.value = null
    try {
      const pedido =
        pedidoPendente.value ?? (await pedidoService.criar(clienteId.value!, carrinho.paraPedido()))
      pedidoPendente.value = pedido

      const pago = await pedidoService.pagar(pedido.id, formaPagamento.value!)

      pedidoPendente.value = null
      carrinho.limpar()
      return pago
    } catch (e) {
      erro.value = mensagemDe(e)
      return null
    } finally {
      enviando.value = false
    }
  }

  return {
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
  }
}

function mensagemDe(e: unknown): string {
  const apiErro = ApiError.de(e)
  return apiErro.detalhes.length ? `${apiErro.message}: ${apiErro.detalhes.join('; ')}` : apiErro.message
}

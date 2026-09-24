import { ref } from 'vue'
import { ApiError } from '@/api/ApiError'
import type { Pedido } from '@/entities/Pedido'
import { pedidoService, type PedidoService } from '@/services'

/** Consulta um pedido já gravado (tela de confirmação). */
export function usePedido(service: PedidoService = pedidoService) {
  const pedido = ref<Pedido | null>(null)
  const carregando = ref(false)
  const erro = ref<string | null>(null)

  async function carregar(id: number): Promise<void> {
    carregando.value = true
    erro.value = null
    try {
      pedido.value = await service.buscar(id)
    } catch (e) {
      erro.value = ApiError.de(e).message
    } finally {
      carregando.value = false
    }
  }

  return { pedido, carregando, erro, carregar }
}

import type { AxiosInstance } from 'axios'
import type { CriarPedidoPayload, ItemPedidoPayload, PedidoDTO } from '@/api/dtos'
import { Pedido } from '@/entities/Pedido'
import type { PedidoService } from './contratos'

export class PedidoApiService implements PedidoService {
  constructor(private readonly http: AxiosInstance) {}

  async criar(clienteId: number, itens: ItemPedidoPayload[]): Promise<Pedido> {
    const payload: CriarPedidoPayload = { clienteId, itens }
    const { data } = await this.http.post<PedidoDTO>('/pedidos', payload)
    return Pedido.deApi(data)
  }

  async buscar(id: number): Promise<Pedido> {
    const { data } = await this.http.get<PedidoDTO>(`/pedidos/${id}`)
    return Pedido.deApi(data)
  }

  async pagar(id: number, formaPagamento: string): Promise<Pedido> {
    const { data } = await this.http.post<PedidoDTO>(`/pedidos/${id}/pagamento`, { tipo: formaPagamento })
    return Pedido.deApi(data)
  }
}

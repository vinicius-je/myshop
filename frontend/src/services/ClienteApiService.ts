import type { AxiosInstance } from 'axios'
import type { ClienteDTO, CriarClientePayload } from '@/api/dtos'
import type { Cliente } from '@/entities/Cliente'
import type { ClienteService } from './contratos'

export class ClienteApiService implements ClienteService {
  constructor(private readonly http: AxiosInstance) {}

  async listar(): Promise<Cliente[]> {
    const { data } = await this.http.get<ClienteDTO[]>('/clientes')
    return data
  }

  async criar(nome: string, email: string): Promise<Cliente> {
    const payload: CriarClientePayload = { nome, email }
    const { data } = await this.http.post<ClienteDTO>('/clientes', payload)
    return data
  }
}

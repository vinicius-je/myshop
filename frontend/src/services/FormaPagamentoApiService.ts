import type { AxiosInstance } from 'axios'
import { criarFormaPagamento, type FormaPagamento } from '@/entities/FormaPagamento'
import type { FormaPagamentoService } from './contratos'

export class FormaPagamentoApiService implements FormaPagamentoService {
  constructor(private readonly http: AxiosInstance) {}

  async listar(): Promise<FormaPagamento[]> {
    const { data } = await this.http.get<string[]>('/formas-pagamento')
    return data.map(criarFormaPagamento)
  }
}

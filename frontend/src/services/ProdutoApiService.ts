import type { AxiosInstance } from 'axios'
import type { ProdutoDTO } from '@/api/dtos'
import { Produto } from '@/entities/Produto'
import type { ProdutoService } from './contratos'

/** SRP: só conversa com `/produtos` e converte DTO em entidade. */
export class ProdutoApiService implements ProdutoService {
  constructor(private readonly http: AxiosInstance) {}

  async listar(): Promise<Produto[]> {
    const { data } = await this.http.get<ProdutoDTO[]>('/produtos')
    return data.map(Produto.deApi)
  }
}

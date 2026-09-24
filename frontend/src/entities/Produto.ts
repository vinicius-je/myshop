import type { ProdutoDTO } from '@/api/dtos'
import { Dinheiro } from './Dinheiro'

/**
 * Produto do catálogo.
 *
 * A diferença para o `ProdutoDTO` é o preço: aqui ele já é um `Dinheiro`, então
 * nenhum componente precisa lembrar de converter ou formatar número solto.
 */
export class Produto {
  constructor(
    readonly id: number,
    readonly nome: string,
    readonly preco: Dinheiro,
    /** URL da imagem; `null` quando o produto não tem foto cadastrada. */
    readonly imagem: string | null = null,
  ) {}

  static deApi(dto: ProdutoDTO): Produto {
    return new Produto(dto.id, dto.nome, Dinheiro.deReais(dto.preco), dto.imagem)
  }
}

import { isAxiosError } from 'axios'
import type { ErroDTO } from './dtos'

/**
 * Erro vindo da API, já traduzido para algo que a tela consegue exibir.
 *
 * Herda de `Error` para continuar funcionando com `throw`/`catch` e com
 * `instanceof`, mas carrega o status HTTP e os detalhes de validação que o
 * `TratadorDeErros` do backend devolve.
 */
export class ApiError extends Error {
  constructor(
    message: string,
    readonly status: number | null,
    readonly detalhes: readonly string[] = [],
  ) {
    super(message)
    this.name = 'ApiError'
  }

  static de(erro: unknown): ApiError {
    if (erro instanceof ApiError) return erro

    if (isAxiosError<ErroDTO>(erro)) {
      const corpo = erro.response?.data
      if (corpo?.mensagem) {
        return new ApiError(corpo.mensagem, corpo.status, corpo.detalhes ?? [])
      }
      if (!erro.response) {
        return new ApiError('Não foi possível conectar à API. Verifique se o backend está no ar.', null)
      }
      return new ApiError(`Erro inesperado da API (${erro.response.status})`, erro.response.status)
    }

    return new ApiError(erro instanceof Error ? erro.message : 'Erro desconhecido', null)
  }
}

import axios from 'axios'
import { ApiError } from './ApiError'

/**
 * Instância única do axios para a MyShop API.
 *
 * O interceptor converte qualquer falha em `ApiError`. Assim services e
 * composables tratam um só tipo de erro, sem conhecer a estrutura do axios.
 */
export const http = axios.create({
  baseURL: import.meta.env.VITE_API_URL ?? 'http://localhost:8080',
  headers: { 'Content-Type': 'application/json' },
  timeout: 10_000,
})

http.interceptors.response.use(
  (resposta) => resposta,
  (erro) => Promise.reject(ApiError.de(erro)),
)

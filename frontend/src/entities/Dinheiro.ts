/**
 * Valor monetário imutável, guardado em centavos inteiros.
 *
 * Por que uma classe e não `number`? Em JavaScript `0.1 + 0.2 === 0.30000000000000004`.
 * Somar preços como float faz o total do carrinho divergir do total calculado pelo
 * backend (BigDecimal). Encapsular o valor em centavos garante aritmética exata e
 * concentra a formatação em um só lugar.
 *
 * Imutável: toda operação devolve uma nova instância, então um item nunca altera,
 * por acidente, o preço de outro.
 */
export class Dinheiro {
  private static readonly formatador = new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
  })

  static readonly ZERO = new Dinheiro(0)

  private constructor(readonly centavos: number) {
    if (!Number.isInteger(centavos)) {
      throw new Error(`Valor em centavos deve ser inteiro: ${centavos}`)
    }
  }

  /** Converte o valor decimal recebido da API (ex.: 349.9) em centavos. */
  static deReais(valor: number): Dinheiro {
    return new Dinheiro(Math.round(valor * 100))
  }

  static deCentavos(centavos: number): Dinheiro {
    return new Dinheiro(centavos)
  }

  somar(outro: Dinheiro): Dinheiro {
    return new Dinheiro(this.centavos + outro.centavos)
  }

  multiplicar(quantidade: number): Dinheiro {
    return new Dinheiro(this.centavos * quantidade)
  }

  formatar(): string {
    return Dinheiro.formatador.format(this.centavos / 100)
  }
}

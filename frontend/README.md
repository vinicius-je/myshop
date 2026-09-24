# MyShop — Frontend

Loja em Vue 3 + TypeScript + Vite que consome a MyShop API: vitrine de produtos,
carrinho e finalização de compra com escolha da forma de pagamento.

## Como rodar

Com o backend no ar (`./mvnw spring-boot:run -Dspring-boot.run.profiles=local` na pasta `backend`):

```bash
npm install
npm run dev          # http://localhost:5173
```

A URL da API vem de `VITE_API_URL` (padrão `http://localhost:8080`). Copie
`.env.example` para `.env` para mudar.

## Telas

| Rota | Tela |
|---|---|
| `/` | Vitrine: lista produtos, busca por nome, adiciona ao carrinho com quantidade |
| `/carrinho` | Itens, preço unitário, quantidade editável, subtotal e total |
| `/checkout` | Seleção/cadastro de cliente, forma de pagamento e confirmação |
| `/pedidos/:id` | Confirmação do pedido pago, lida de `GET /pedidos/{id}` |

## Estrutura

```
src/
├── api/           http.ts (axios), ApiError, DTOs do contrato HTTP
├── entities/      Dinheiro, Produto, ItemCarrinho, Carrinho, Pedido, Cliente, FormaPagamento
├── services/      contratos (interfaces) + implementações com axios
├── composables/   useCarrinho, useProdutos, useCheckout, usePedido — lógica de negócio
├── components/    AppHeader, ProdutoCard, SeletorQuantidade, ResumoCarrinho
├── views/         uma por rota
└── router/
```

Fluxo de dependência: `view → composable → service (interface) → axios`.
Componentes não chamam a API; services não guardam estado; entidades não
conhecem Vue nem HTTP.

## POO e SOLID — onde e por quê

Aplicado só onde resolve um problema concreto:

| Onde | Conceito | Problema que resolve |
|---|---|---|
| `entities/Dinheiro.ts` | Value object imutável, encapsulamento | `0.1 + 0.2 !== 0.3`. Guardar centavos inteiros deixa o total do carrinho igual ao do backend (BigDecimal) e centraliza a formatação em R$. |
| `entities/ItemCarrinho.ts` | Encapsulamento | Quantidade só muda por método que valida (inteiro ≥ 1). |
| `entities/Carrinho.ts` | SRP | Regras do carrinho (não duplicar item, somar total, montar corpo do pedido) num só lugar, sem Vue, API ou storage. |
| `api/ApiError.ts` | Herança de `Error` | Um só tipo de erro com status e detalhes do `TratadorDeErros`, ainda compatível com `throw`/`instanceof`. |
| `services/contratos.ts` | DIP + ISP | Composables dependem de interfaces pequenas por recurso; em teste, passa-se um objeto fake sem mockar HTTP. |
| `services/index.ts` | Composition root | Único lugar que instancia os services concretos com axios. |
| `composables/useCheckout.ts` | DIP | Dependências por parâmetro, com as reais como padrão. |
| `entities/FormaPagamento.ts` | OCP | Formas vêm de `GET /formas-pagamento`; uma forma nova no backend aparece no checkout sem mudar código. |

Onde **não** foi aplicado: `Cliente` é só dado, então é uma `interface`, não
uma classe. DTOs também são só tipos.

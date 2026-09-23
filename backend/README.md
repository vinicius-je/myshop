# MyShop API — Sistema de Pedidos

Projeto educacional em Spring Boot para estudar os cinco princípios SOLID.

## Stack

- Java 17
- Spring Boot 4.1.1 (Web MVC, Data JPA, Validation)
- SQL Server (uso normal) e H2 em memória (profile `local`)
- springdoc-openapi (Swagger UI)

## Como rodar

### Com H2 em memória, sem Docker

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

### Com SQL Server em container

Crie o database `MyShop` na instância e defina as variáveis de ambiente:

| Variável | Exemplo |
|---|---|
| `DB_HOST` | `localhost` |
| `DB_PORT` | `1433` |
| `DB_NAME` | `MyShop` |
| `DB_USER` | `sa` |
| `DB_PASSWORD` | *(sua senha)* |

```bash
./mvnw spring-boot:run
```

As tabelas são criadas pelo Hibernate (`ddl-auto: update`); o database em si não.

Swagger UI: <http://localhost:8080/swagger-ui.html>

## Endpoints

| Verbo | Rota | Descrição |
|---|---|---|
| POST | `/clientes` | Cria cliente |
| POST | `/produtos` | Cria produto |
| POST | `/pedidos` | Cria pedido (status PENDENTE) |
| GET | `/pedidos/{id}` | Busca pedido |
| POST | `/pedidos/{id}/pagamento` | Paga o pedido |
| POST | `/pedidos/{id}/cancelamento` | Cancela o pedido |

### Exemplo de fluxo

```bash
curl -X POST localhost:8080/clientes \
  -H "Content-Type: application/json" \
  -d "{\"nome\":\"Ana Souza\",\"email\":\"ana@example.com\"}"

curl -X POST localhost:8080/produtos \
  -H "Content-Type: application/json" \
  -d "{\"nome\":\"Teclado\",\"preco\":150.00}"

curl -X POST localhost:8080/pedidos \
  -H "Content-Type: application/json" \
  -d "{\"clienteId\":1,\"itens\":[{\"produtoId\":1,\"quantidade\":2}]}"

curl -X POST localhost:8080/pedidos/1/pagamento \
  -H "Content-Type: application/json" \
  -d "{\"tipo\":\"PIX\"}"
```

Tipos de pagamento aceitos: `PIX`, `CARTAO`, `BOLETO`.

## Erros tratados

| Situação | Status |
|---|---|
| Cliente, produto ou pedido inexistente | 404 |
| Pedido já pago | 409 |
| Pedido cancelado | 409 |
| Forma de pagamento desconhecida | 400 |
| Falha de Bean Validation | 400 |

## Estrutura

```
com.myshop.myshop_api
├── controller     HTTP
├── service        regras de aplicação
├── domain         entidades e regras de negócio
├── pagamento      interface Pagamento e implementações
├── repository     persistência
├── dto            contratos de entrada e saída
└── exception      exceções de domínio e @RestControllerAdvice
```

---

# Onde cada princípio SOLID aparece?

## SRP — Single Responsibility Principle

Cada classe tem um motivo para mudar:

| Classe | Muda quando… |
|---|---|
| `PedidoController` | o contrato HTTP muda |
| `PedidoService` | a regra de montagem do pedido muda |
| `PagamentoService` | a regra de cobrança muda |
| `PedidoRepository` | a persistência muda |
| `TratadorDeErros` | o formato de erro da API muda |
| `Pedido` | a regra de estado do pedido muda |

Parte da regra ficou **no domínio, não no service**. `ItemPedido.subtotal()` e
`Pedido.recalcularTotal()` moram na entidade porque dependem apenas do próprio
estado. Se o `PedidoService` multiplicasse `quantidade * precoUnitario`, a regra
estaria numa classe que não é dona dos dados — e se espalharia por todo lugar
que precisasse do total.

Mesma lógica em `Pedido.validarQuePodeSerPago()`: como a checagem vive na
entidade, qualquer forma de pagamento — inclusive as que ainda não existem —
herda a proteção automaticamente.

`Pedido` não tem `setStatus` público. O status só muda por `marcarComoPago()` e
`cancelar()`, que carregam a validação junto. Um setter aberto deixaria qualquer
classe colocar o pedido num estado inválido pulando a regra.

## OCP — Open/Closed Principle

O ponto central do projeto, em `PagamentoService`:

```java
private final Map<String, Pagamento> formasDePagamento;

public PagamentoService(PedidoService pedidoService, Map<String, Pagamento> formasDePagamento) {
    this.pedidoService = pedidoService;
    this.formasDePagamento = formasDePagamento;
}
```

O Spring injeta nesse `Map` **todos** os beans que implementam `Pagamento`,
usando o nome do bean como chave — por isso as implementações são
`@Component("PIX")`, `@Component("CARTAO")` e `@Component("BOLETO")`.

O anti-exemplo, que o projeto evita:

```java
// Cada forma nova obriga a editar este método:
// fechado para extensão, aberto para modificação.
if (tipo.equals("PIX")) {
    new PixPagamento().pagar(valor);
} else if (tipo.equals("CARTAO")) {
    new CartaoPagamento().pagar(valor);
} else if (tipo.equals("BOLETO")) {
    new BoletoPagamento().pagar(valor);
}
```

`PagamentoRequest.tipo` é `String` e não um enum de propósito: um enum
`TipoPagamento` precisaria ganhar uma constante a cada forma criada — ou seja,
**modificar código existente**, exatamente o que o OCP quer evitar. A validação
é feita contra o registro de beans, que se atualiza sozinho.

## LSP — Liskov Substitution Principle

```java
Pagamento formaEscolhida = resolverForma(request.tipo());
pedido.validarQuePodeSerPago();
formaEscolhida.pagar(pedido.getValorTotal());
pedido.marcarComoPago();
```

A variável é do tipo `Pagamento`. O service chama `pagar(valor)` sem saber qual
implementação está ali, e o fluxo seguinte é idêntico em todos os casos. As três
implementações fazem coisas diferentes por dentro (Pix liquida na hora, cartão
pede autorização à adquirente, boleto emite com vencimento) mas honram o mesmo
contrato.

Quebraria LSP uma implementação que lançasse `UnsupportedOperationException`,
que exigisse uma chamada extra antes de `pagar()`, ou que ignorasse o valor
recebido — qualquer uma obrigaria o `PagamentoService` a saber quem está do
outro lado.

## ISP — Interface Segregation Principle

`Pagamento` tem exatamente um método:

```java
public interface Pagamento {
    void pagar(BigDecimal valor);
}
```

Toda implementação usa 100% dela. O anti-exemplo:

```java
// Boleto não estorna online, Pix não parcela, cartão não tem
// código de barras. Metade dos métodos viraria
// "throw new UnsupportedOperationException()".
interface Pagamento {
    void pagar(BigDecimal valor);
    void estornar(BigDecimal valor);
    void parcelar(int vezes);
    String gerarCodigoDeBarras();
    String consultarBandeira();
}
```

Repare na conexão entre princípios: violar ISP aqui arrastaria LSP junto, porque
as implementações seriam forçadas a repudiar parte do contrato.

## DIP — Dependency Inversion Principle

Todas as dependências entram por construtor, sempre como abstração. Não há `new`
de componente gerenciado em lugar nenhum.

A prova mais forte está nos **imports** do `PagamentoService`:

```java
import com.myshop.myshop_api.pagamento.Pagamento;
```

Só a interface. Não existe `import ...PixPagamento`, nem `CartaoPagamento`, nem
`BoletoPagamento` — o compilador garante que o service não consegue sequer
mencionar uma implementação concreta.

Os repositories reforçam o mesmo: `ClienteRepository` é uma interface, e a
implementação é gerada pelo Spring Data em tempo de execução.

Evitado:

```java
private PixPagamento pagamento = new PixPagamento();
```

---

## Como adicionar uma nova forma de pagamento

Um arquivo novo. Nada mais:

```java
package com.myshop.myshop_api.pagamento;

@Component("PAYPAL")
public class PaypalPagamento implements Pagamento {

    private static final Logger log = LoggerFactory.getLogger(PaypalPagamento.class);

    @Override
    public void pagar(BigDecimal valor) {
        log.info("Paypal: redirecionando para checkout de R$ {}", valor);
    }
}
```

Reinicie e `{"tipo":"PAYPAL"}` já funciona.

Arquivos que **não** precisam ser tocados: `PagamentoService`,
`PedidoController`, `PagamentoRequest`, `Pagamento`, as outras implementações,
`TratadorDeErros` e qualquer configuração. Zero.

A mensagem de erro de tipo inválido passa a listar a nova forma sozinha, porque
lê o registro do Spring:

```
Forma de pagamento 'XPTO' não suportada. Disponíveis: BOLETO, CARTAO, PAYPAL, PIX
```

## E o `if` dentro do PagamentoService?

```java
if (forma == null) {
    throw new FormaPagamentoNaoSuportadaException(tipo, formasDePagamento.keySet());
}
```

Não é seleção de tipo — é guarda de entrada inválida. A diferença prática é que
**ele não cresce**: com 3 ou com 30 formas de pagamento, continua sendo
exatamente este `if`. O if/else proibido escala com o número de formas; este não.

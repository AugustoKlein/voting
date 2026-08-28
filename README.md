# Voting API

API REST para gerenciamento de pautas e sessões de votação, desenvolvida com **Java 21** e **Spring Boot**.

O projeto permite criar pautas, abrir e encerrar sessões de votação e registrar votos, além de disponibilizar documentação através do **Swagger/OpenAPI**.

## Tecnologias

- Java 21
- Spring Boot
- Gradle
- MySQL 8
- Flyway
- Docker
- Swagger / OpenAPI
- JUnit 5
- Mockito

## Funcionalidades

- Criação de pautas
- Consulta de pautas
- Abertura de sessões de votação
- Registro de votos SIM/NÃO
- Encerramento de sessões
- Migração de banco de dados com Flyway
- Encerramento automático de sessões
- Documentação da API com Swagger/OpenAPI

## Comentários

- Para tratar de exceções de forma global utilizei a classe [MyExceptionHandler](https://github.com/AugustoKlein/voting/blob/fb040b99cde42c177120f1a2eebd42fa63bc7e2a/src/main/java/com/voting/infra/handler/MyExceptionHandler.java),
onde são tratados os possíveis erros.
- Para simular o cenário da Tarefa Bônus 1("Criar uma Facade/Client Fake que retorna aleátoriamente se um CPF recebido é válido ou não."), utilizei de mocks.
Nesse [mock](https://github.com/AugustoKlein/voting/blob/0e63ca89caca0d0a6982dd85cb18692c7f944dfb/src/main/java/com/voting/client/service/service/impl/DocumentValidationClientServiceMock.java) uma randomização básica será feita, onde 50% das vezes o CPF retornara como válido. Também criei um [repositório fake](https://github.com/AugustoKlein/voting/blob/f0c0507041c85ccc476ae02f6ea85bf8106eb00d/src/main/java/com/voting/client/service/repository/DocumentValidationRepository.java) simulando como seria em um cenário real.

## Versionamento
Para o versionamento utilizei o padrão "/api/v1/..." na URL, pela simplicidade da aplicação acredito que foi a melhor
alternativa. 

Assim, quando uma possível versão v2 for necessária, não haveria quebra de contratos com a versão antiga, sendo possível a sua reutilização.

Também considerei a facilidade de criação de novos controllers para possíveis versões futuras.

## Fluxo da pauta

Uma pauta possui os seguintes estados:

```text
CREATED → OPEN → CLOSED
```

- **CREATED** — pauta criada.
- **OPEN** — votação aberta.
- **CLOSED** — votação encerrada.

## Fluxo das requisições
```text
                  POST /pauta
                       │
                       ▼
                    CREATED
                   /       \
                  /         \
         GET /pauta/{id}   PUT /open-session
                            │
                            ▼
                          OPEN
                       /    |    \
                      /     |     \
                     /      |      \
             GET /pauta   PUT /vote   PUT /close-session
                                      │
                                      ▼
                                   CLOSED
                                      │
                                      ▼
                              GET /pauta/{id}
```

## Scheduler

O projeto possui um scheduler responsável por verificar periodicamente as pautas com status `OPEN`.

A verificação é executada a cada **5 segundos**:

```
@Scheduled(fixedDelay = 5000)
```

O scheduler identifica as pautas cuja data de encerramento foi atingida e realiza o encerramento da sessão.

## Banco de dados

O projeto utiliza **MySQL 8** através do Docker Compose.

O banco de dados utilizado é:

```text
voting_id
```

## Executando a aplicação

### Pré-requisitos

- Java 21
- Docker
- Docker Compose
- Gradle

### Iniciando o projeto

Primeiro clone o projeto
```
git clone https://github.com/AugustoKlein/voting
 ```

Execute o projeto:

```bash
./gradlew bootRun --args="--spring.profiles.active=local"
```

Ao executar o projeto mesmo irá gerar Docker Container com MySQL, baseado no arquivo [compose.yaml](compose.yaml), caso haja problemas rode o comando:

```bash
docker compose up -d
```

Depois execute a aplicação

A aplicação estará disponível em:

```text
http://localhost:8080
```

As migrations são executadas automaticamente pelo **Flyway** durante a inicialização da aplicação.

## Endpoints

| Método | Endpoint                           | Descrição                   |
|--------|------------------------------------|-----------------------------|
| `POST` | `/api/v1/pauta`                    | Cria uma nova pauta         |
| `GET`  | `/api/v1/pauta/{id}`               | Busca uma pauta pelo ID     |
| `PUT`  | `/api/v1/pauta/{id}/open-session`  | Abre a sessão de votação    |
| `PUT`  | `/api/v1/pauta/{id}/vote`          | Registra um voto na pauta   |
| `PUT`  | `/api/v1/pauta/{id}/close-session` | Encerra a sessão de votação |


## Testes

Os testes utilizam:

- JUnit 5
- Mockito

Para executar:

```bash
./gradlew test
```

## Flyway

O versionamento do banco de dados é realizado utilizando **Flyway**.

As migrations estão localizadas em:

```text
src/main/resources/db/migration
```

## Performance

Para testes de performance utilizei o K6/grafana, onde testei apenas a funcionalidade de votar, tentando replicar um 
cenário real de múltiplos votantes, os testes avaliados foram:

- tempo de resposta;
- throughput;
- taxa de erros;
- comportamento sob concorrência;
- consumo de recursos;
- consistência dos votos sob alta concorrência.

No último teste esse foi o resultado:
```text
Requests:
  Total: 24368

Throughput:
  Requests/s: 405.86

Tempo de resposta:
  Média: 108.34 ms
  Mediana: 83.19 ms
  P90: 248.98 ms
  P95: 277.28 ms
  P99: 0.00 ms
  Máximo: 566.18 ms

Erros:
  Taxa de erro: 51.52%
```
Vale notar que a taxa de erros está coerente com a lógica do serviço, pois utilizei da **Tarefa Bônus 1 - Integração com sistemas externos**,
onde era descrito que um CPF aleatório retornaria válido ou não.

Para executar o teste é preciso:

1. Baixar o grafana/k6 do dockerHub

```
docker pull grafana/k6
```

2. Rodar o container
```
docker run --rm -v "${PWD}\performance:/scripts" grafana/k6 run /scripts/voting.js
```

## Documentação

A documentação da API é gerada automaticamente utilizando **Springdoc OpenAPI**.

- Swagger UI: http://localhost:8080/swagger-ui.html

- OpenAPI: http://localhost:8080/v3/api-docs

- Postman Collection: [Collection](https://github.com/AugustoKlein/voting/blob/819bb9adcb15ee4e9aa4e9e4bd29d036b4b90eb4/Voting%20API.postman_collection.json)

## 
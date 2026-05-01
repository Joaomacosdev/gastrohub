# GastroHub

Backend Spring Boot para o Tech Challenge Fase 1 da FIAP em Arquitetura e Desenvolvimento Java.
O projeto implementa uma API REST para gerenciamento de usuarios, enderecos e autenticacao em uma plataforma compartilhada de restaurantes.

<p align="left">
  <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk" alt="Java 21"/>
  <img src="https://img.shields.io/badge/Spring_Boot-3.2.12-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot 3.2.12"/>
  <img src="https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Data JPA"/>
  <img src="https://img.shields.io/badge/Spring_Security-6-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white" alt="Spring Security 6"/>
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL 8"/>
  <img src="https://img.shields.io/badge/Flyway-MySQL-CC0200?style=for-the-badge&logo=flyway&logoColor=white" alt="Flyway"/>
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker Compose"/>
  <img src="https://img.shields.io/badge/JUnit-5-25A162?style=for-the-badge&logo=junit5&logoColor=white" alt="JUnit 5"/>
  <img src="https://img.shields.io/badge/Mockito-Testes-78A641?style=for-the-badge" alt="Mockito"/>
  <img src="https://img.shields.io/badge/MapStruct-1.5.5.Final-FF6F00?style=for-the-badge" alt="MapStruct"/>
  <img src="https://img.shields.io/badge/Lombok-1.18.38-BC4521?style=for-the-badge" alt="Lombok"/>
  <img src="https://img.shields.io/badge/SpringDoc_OpenAPI-2.3.0-85EA2D?style=for-the-badge&logo=swagger&logoColor=black" alt="SpringDoc OpenAPI 2.3.0"/>
  <img src="https://img.shields.io/badge/JWT-jjwt_0.12.x-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white" alt="JWT jjwt 0.12.x"/>
</p>

## Funcionalidades

- Cadastro de usuarios com perfil de cliente ou dono de restaurante.
- Autenticacao stateless com JWT e senhas criptografadas com BCrypt.
- Consulta paginada, busca por nome, atualizacao e remocao de usuarios.
- Cadastro, listagem, consulta, atualizacao e remocao de enderecos vinculados ao usuario autenticado.
- Validacoes de e-mail e login unicos por Strategy Pattern.
- Respostas de erro padronizadas com `ProblemDetail` no formato RFC 7807.
- Migrations de banco com Flyway para criacao e evolucao do schema MySQL.
- Documentacao interativa com Swagger/OpenAPI e exemplos de sucesso e erro.
- Envio de notificacao por e-mail apos cadastro de usuario.
- Collection Postman versionada em `docs/postman/GastroHub.postman_collection.json`.

## Arquitetura

O codigo esta organizado por dominio em `user`, `address`, `notification` e `health`, com configuracoes transversais em `config` e infraestrutura em `infra`.
Os casos de uso de usuario e endereco seguem uma abordagem CQRS-lite, separando interfaces de comando (`CommandService`) e consulta (`QueryService`) para deixar leitura e escrita mais explicitas.

As validacoes de unicidade ficam isoladas em strategies, o mapeamento entre entidades e DTOs usa MapStruct, e as excecoes de negocio sao centralizadas no `GlobalExceptionHandler`, que monta respostas `ProblemDetail` por meio do `ProblemDetailFactory`.
A seguranca usa Spring Security com JWT stateless, filtro customizado de autenticacao e roles `ROLE_CLIENTE` e `ROLE_DONO_RESTAURANTE`.
O modulo `notification` escuta eventos de usuario criado e envia e-mail de boas-vindas usando Spring Mail.

## Endpoints

| Metodo | Path | Descricao | Auth |
| --- | --- | --- | --- |
| GET | `/ping` | Health check da API | Publico |
| POST | `/api/v1/users` | Cria um usuario | Publico |
| GET | `/api/v1/users` | Lista usuarios com paginacao | Publico |
| GET | `/api/v1/users/{id}` | Busca usuario por ID | Publico |
| GET | `/api/v1/users/search?nome={nome}` | Busca usuarios por nome | Publico |
| PUT | `/api/v1/users/{id}` | Atualiza dados do usuario | Bearer JWT (`ROLE_CLIENTE` ou `ROLE_DONO_RESTAURANTE`) |
| DELETE | `/api/v1/users/{id}` | Remove usuario | Bearer JWT (`ROLE_DONO_RESTAURANTE`) |
| POST | `/api/v1/auth/login` | Autentica usuario e retorna token JWT | Publico |
| PATCH | `/api/v1/auth/password/{userID}` | Troca de senha | Em integracao no PR #14; endpoint nao esta presente nesta branch |
| POST | `/api/v1/address` | Cria endereco para o usuario autenticado | Bearer JWT |
| GET | `/api/v1/address` | Lista enderecos | Bearer JWT |
| GET | `/api/v1/address/{id}` | Busca endereco por ID | Bearer JWT |
| PUT | `/api/v1/address/{id}` | Atualiza endereco | Bearer JWT |
| DELETE | `/api/v1/address/{id}` | Remove endereco | Bearer JWT |

## Como Rodar Com Docker Compose

1. Clone o repositorio:

```bash
git clone https://github.com/Joaomacosdev/gastrohub.git
cd gastrohub
```

2. Crie um arquivo `.env` na raiz do projeto:

```env
DB_ROOT_PASSWORD=root
DB_NAME=gastro_hub_db
DB_USER=gastro
DB_PASSWORD=G@str00
DB_PORT=3306
MAIL_USER=seu-email@gmail.com
MAIL_PASSWORD=sua-senha-de-app
JWT_SECRET=sua-chave-secreta-com-tamanho-seguro
JWT_EXPIRATION=86400000
```

3. Suba a aplicacao e o MySQL:

```bash
docker compose up --build
```

4. Acesse:

- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Como Rodar Localmente Sem Docker

Para rodar a aplicacao localmente, suba apenas o MySQL pelo Compose:

```bash
docker compose up mysql -d
```

Depois execute a aplicacao com o Maven Wrapper:

```bash
./mvnw spring-boot:run
```

No Windows, se preferir:

```bash
.\mvnw.cmd spring-boot:run
```

A configuracao local usa MySQL em `localhost:3309`, banco `gastro_hub_db`, usuario `gastro` e senha `G@str00`, conforme `application.properties`.

## Testes

Execute toda a suite:

```bash
./mvnw test
```

Execute um teste especifico:

```bash
./mvnw test -Dtest=UserServiceImplTest
```

O projeto possui testes unitarios com JUnit 5 e Mockito para services e validators de usuario, somando 21 cenarios unitarios nesta entrega.
Tambem ha testes de integracao com `@SpringBootTest`, MockMvc e H2 cobrindo fluxos E2E da API.

Resultado validado nesta branch:

```text
Tests run: 27, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Observacao: a entrega alvo e Java 21. Em ambiente local com JDK 25, pode ser necessario rodar os testes com `-DargLine="-Dnet.bytebuddy.experimental=true"` por compatibilidade do Mockito/Byte Buddy.

## Documentacao Da API

A documentacao Swagger esta disponivel em:

```text
http://localhost:8080/swagger-ui.html
```

Ela inclui exemplos de request/response de sucesso e erro para os principais endpoints, usando o formato real de erro da API:

```json
{
  "type": "https://api.gastrohub.com/errors/EMAIL_ALREADY_EXISTS",
  "title": "Email ja cadastrado",
  "status": 409,
  "detail": "Email vini@gastrohub.com ja cadastrado",
  "instance": "/api/v1/users",
  "code": "EMAIL_ALREADY_EXISTS",
  "timestamp": "2026-05-01T12:34:56.789Z"
}
```

<!-- TODO: Vinicius adiciona print do Swagger UI aqui -->
<!-- TODO: Vinicius adiciona print de endpoint expandido aqui -->
<!-- TODO: Vinicius adiciona print do Authorize com Bearer JWT aqui -->

## Estrutura De Pacotes

```text
br.com.gastrohub/
|-- address/
|   |-- controller/ (+ docs/)
|   |-- dto/ (request/ response/)
|   |-- entity/
|   |-- mapper/
|   |-- repository/
|   `-- service/
|-- config/
|-- health/
|   `-- contoller/
|-- infra/
|   |-- exception/ (+ handle/)
|   `-- security/
|-- notification/
|   |-- listener/
|   `-- service/
`-- user/
    |-- controller/ (+ docs/)
    |-- dto/ (request/ response/)
    |-- entity/ (+ enums/)
    |-- event/
    |-- mapper/
    |-- repository/
    |-- service/
    `-- strategy/
```

## Equipe

- Joao Marcos
- Felipe Scholl
- Rafael Soares
- Gabriel Ribeiro
- Vinicius Oliveira

Projeto desenvolvido para o Tech Challenge Fase 1 da FIAP.

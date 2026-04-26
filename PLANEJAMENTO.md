# 📐 Planejamento — Tech Lead / Arquiteto
### Tech Challenge - Fase 1 | Sistema de Gestão de Restaurantes
> **Documento gerado em:** Abril 2026 | **Versão:** 2.0 | **Responsável:** Felipe (Tech Lead)

---

Com base na documentação do **Tech Challenge - Fase 1** e na **estrutura real do projeto**, este documento estrutura o planejamento de execução sob a perspectiva do **Tech Lead / Arquiteto**.

O foco principal é garantir a integridade da arquitetura por domínio, a qualidade do código através de reviews e a correta integração entre as camadas de dados e segurança.

---

## 👥 Equipe

| # | Papel | Integrante | Responsabilidades |
|---|-------|------------|-------------------|
| 1 | QA / Infra | Gabriel | Docker, banco de dados, Collections Postman e testes de integração |
| 2 | Tech Lead | Felipe | Projeto base, entidades JPA, Migrations e Code Review final |
| 3 | DEV CRUD | João | Controllers, DTOs, lógica dos CRUDs e tratamento de erros |
| 4 | DEV Auth | Rafael | Configuração de segurança, implementação do Login e JWT |
| 5 | DEV Docs | Vine | Configuração do Swagger, testes unitários, README e relatório final |

---

## 📋 Descrição Geral dos Requisitos

O projeto consiste em um sistema de backend para gestão de usuários de um restaurante, suportando dois perfis (**Cliente** e **Dono de Restaurante**).

### Requisitos Funcionais (RF) de Alta Prioridade

| ID | Requisito | Prioridade |
|----|-----------|------------|
| RF-001 / RF-006 | CRUD completo de usuários com garantia de e-mail único | Alta |
| RF-002 / RF-003 | Endpoints dedicados para troca de senha e atualização de dados | Alta |
| RF-007 / RF-008 | Validação de login e suporte a múltiplos tipos de usuários | Alta |
| RF-009 | Documentação automática via Swagger/OpenAPI | Alta |
| RF-005 | Busca de usuários pelo nome | Média |
| RF-010 | Versionamento de API (`/api/v1/`) | Média |

### Requisitos Não-Funcionais (RNF)

| Requisito | Tecnologia |
|-----------|------------|
| **Arquitetura:** Baseada em Domínio | Spring Boot `3.2.12` |
| **Persistência:** Banco relacional + ORM | MySQL 8.0 + Spring Data JPA |
| **Migrações:** Versionamento de banco | Flyway |
| **Infraestrutura:** Conteinerização total | Docker + Docker Compose |
| **Qualidade:** Testes automatizados (cobertura ≥ 70%) | JUnit 5 + Mockito |
| **Mapeamento DTO ↔ Entity** | MapStruct |
| **Padronização de erros** | RFC 7807 (ProblemDetail) |
| **Validação de dados** | Bean Validation (Hibernate Validator) |

---

## 🗂️ Estrutura Real do Projeto e Divisão por Membro

A estrutura abaixo reflete o **código já existente no repositório**. Cada pasta tem responsabilidade clara de um membro, minimizando conflitos de merge.

```
src/main/java/br/com/gastrohub/
│
├── GastrohubApplication.java                          → Felipe (Tech Lead)
│
├── config/                                            → Felipe (Tech Lead) + Vine (DEV Docs)
│   ├── CorsConfig.java                                ✅ existe
│   └── OpenApiConfig.java                             ❌ criar — Vine
│
├── infra/                                             → Felipe (Tech Lead) + Rafael (DEV Auth)
│   ├── exception/
│   │   ├── NotFoundException.java                     ✅ existe
│   │   └── handle/
│   │       └── GlobalExceptionHandler.java            ✅ existe (vazio) — João preenche
│   └── security/
│       ├── SecurityConfiguration.java                 ✅ existe (vazio) — Rafael preenche
│       ├── JwtAuthFilter.java                         ❌ criar — Rafael
│       └── JwtTokenProvider.java                      ❌ criar — Rafael
│
├── user/                                              → João (DEV CRUD) + Rafael (DEV Auth)
│   ├── controller/
│   │   ├── UserController.java                        ✅ existe (vazio) — João preenche
│   │   ├── AuthController.java                        ✅ existe (vazio) — Rafael preenche
│   │   └── docs/
│   │       ├── UserControllerDocs.java                ✅ existe (vazio) — Vine preenche
│   │       └── AuthControllerDocs.java                ✅ existe (vazio) — Vine preenche
│   ├── dto/
│   │   ├── request/
│   │   │   ├── UserRequestDTO.java                    ✅ existe (vazio) — João preenche
│   │   │   ├── UpdateUserRequestDTO.java              ❌ criar — João
│   │   │   ├── ChangePasswordRequestDTO.java          ❌ criar — João
│   │   │   └── LoginRequestDTO.java                   ❌ criar — Rafael
│   │   └── response/
│   │       ├── UserResponseDTO.java                   ✅ existe (vazio) — João preenche
│   │       └── LoginResponseDTO.java                  ❌ criar — Rafael
│   ├── entity/
│   │   ├── User.java                                  ✅ existe (incompleto) — Felipe corrige
│   │   ├── Endereco.java                              ✅ existe (incompleto) — Felipe corrige
│   │   └── enums/
│   │       ├── Role.java                              ✅ existe
│   │       └── TestEnum.java                          ⚠️ deletar — Felipe
│   ├── mapper/
│   │   └── UserMapper.java                            ✅ existe (vazio) — João preenche
│   ├── repository/
│   │   └── UserRepository.java                        ✅ existe (renomeado) — Felipe adicionar `findByNomeContainingIgnoreCase`
│   ├── service/
│   │   ├── UserService.java                           ✅ existe (vazio) — João preenche
│   │   ├── UserServiceImpl.java                       ✅ existe (vazio) — João preenche
│   │   ├── AuthService.java                           ✅ existe (vazio) — Rafael preenche
│   │   └── AuthServiceImpl.java                       ✅ existe (vazio) — Rafael preenche
│   └── strategy/
│       └── ValidateUserStrategy.java                  ✅ existe (vazio) — João preenche
│
├── notification/                                      → Vine (DEV Docs) — baixa prioridade Fase 1
│   ├── dto/request/NotificationRequestDTO.java        ✅ existe
│   ├── entity/
│   │   ├── Notification.java                          ✅ existe
│   │   └── enums/NotificationType.java                ✅ existe
│   ├── repository/NotificationRepository.java         ✅ existe
│   ├── service/
│   │   ├── NotificationService.java                   ✅ existe
│   │   └── NotificationServiceImpl.java               ✅ existe
│   └── strategy/ValidateNotificationStrategy.java     ✅ existe
│
src/main/resources/
│   ├── application.properties                         ✅ existe — Felipe migra para .yml
│   └── db/migration/
│       └── V1__create_users_table.sql                 ❌ criar — Felipe
│
src/test/java/br/com/gastrohub/
│   ├── GastrohubApplicationTests.java                 ✅ existe
│   ├── user/service/UserServiceImplTest.java          ❌ criar — Vine
│   └── user/service/AuthServiceImplTest.java          ❌ criar — Vine
```

---

## ⚠️ Problemas Críticos Identificados no Projeto Atual

> Devem ser resolvidos **antes do Sprint 1 terminar** — responsabilidade do Tech Lead (Felipe):

| # | Arquivo | Problema | Ação |
|---|---------|----------|------|
| 1 | `user/repository/UserService.java` | Nome idêntico ao `user/service/UserService.java` — conflito crítico entre repository e service | **Renomeado para `UserRepository.java`** ✅ |
| 2 | `user/entity/enums/TestEnum.java` | Arquivo de teste sem propósito no código fonte | **Deletado** ✅ |
| 3 | `pom.xml` | Spring Boot `4.0.5` (instável/experimental) | **Corrigido para `3.2.12`** ✅ |
| 4 | `pom.xml` | Artefatos de teste separados por módulo (`spring-boot-starter-data-jpa-test`, `...-security-test`, etc.) ainda não existem no repositório Maven | **Decisão arquitetural:** serão criados como módulos de teste customizados pelo time — manter entradas no `pom.xml` ✅ |
| 5 | `pom.xml` | `spring-boot-starter-thymeleaf` + `thymeleaf-extras-springsecurity6` — projeto é REST API | **Removidos. `spring-boot-starter-webmvc` corrigido para `spring-boot-starter-web`** ✅ |
| 6 | `pom.xml` | MapStruct, Lombok, Flyway, SpringDoc OpenAPI e JJWT ausentes | **Adicionados** ✅ |
| 7 | `docker-compose.yml` | `version: '3,9'` — vírgula inválida | **Corrigido para `'3.9'`** ✅ |
| 8 | `docker-compose.yml` | Serviço `app` sem `healthcheck` no MySQL — race condition na inicialização | **Adicionado `healthcheck` no MySQL e `condition: service_healthy` no `app`** ✅ |
| 9 | `application.properties` | `ddl-auto=update` usado junto com Flyway — conflito de controle de schema | **Alterado para `ddl-auto=validate`** ✅ |
| 10 | `User.java` | `dataUltimaAlteracao` usa `java.util.Date` (legado); sem `@PrePersist`/`@PreUpdate`; sem getters/setters | **Migrado para `LocalDateTime`, hooks `@PrePersist`/`@PreUpdate` adicionados, `@Data`/`@NoArgsConstructor`/`@AllArgsConstructor` (Lombok) aplicados** ✅ |
| 11 | `Endereco.java` | Campos `estado` e `complemento` ausentes; sem anotações de validação | **Adicionados `estado`, `complemento`, `@NotBlank`, `@Size`, `@Pattern` e Lombok** ✅ |
| 12 | `UserController.java` | Declaração de `package` ausente no topo do arquivo | **Corrigido** ✅ |

---

## 🚀 Planejamento de Execução por Sprints

### Sprint 1 — Fundação e Estrutura Base (Semanas 1–2)

**Objetivo:** Garantir que o esqueleto do projeto esteja sólido e o ambiente Docker funcional.

**Sprint Goal:** Aplicação sobe no Docker, banco inicializa via Flyway, Swagger acessível em `/swagger-ui.html`.

#### ✅ Felipe — Tech Lead
**Pastas de responsabilidade:** `config/`, `infra/`, `user/entity/`, `user/repository/`, `src/main/resources/`

- [x] Corrigir `pom.xml` — versão Spring Boot `3.2.12` ✅, remover dependências inexistentes e Thymeleaf, adicionar MapStruct + processor, Lombok, Flyway, SpringDoc OpenAPI (`springdoc-openapi-starter-webmvc-ui`), JJWT
- [x] Renomear `user/repository/UserService.java` → `UserRepository.java` ✅ + adicionado `findByNomeContainingIgnoreCase`, `findByLogin`, `existsByEmail` ✅
- [x] Deletar `user/entity/enums/TestEnum.java` ✅
- [x] Completar `user/entity/User.java` — trocar `Date` → `LocalDateTime`, adicionar `@PrePersist`/`@PreUpdate` para `dataUltimaAlteracao`, adicionar `@Data` (Lombok) ou getters/setters manuais ✅
- [x] Completar `user/entity/Endereco.java` — adicionar `estado` (`@Size(max=2)`), `complemento`, anotações `@NotBlank`/`@NotNull` nos campos obrigatórios ✅
- [x] Migrar `application.properties` → `application.yml`; configurar Flyway; alterar `ddl-auto=validate` ✅
- [x] Criar `src/main/resources/db/migration/V1__create_users_table.sql` ✅

#### ✅ Gabriel — QA / Infra
**Pastas de responsabilidade:** raiz do projeto (`docker-compose.yml`, `Dockerfile`, `.env.example`)

- [x] Corrigir `docker-compose.yml` — `version: '3.9'`, adicionar `healthcheck` no serviço `mysql`, adicionar `depends_on: mysql: condition: service_healthy` no serviço `app` ✅
- [ ] Criar `.env.example` com variáveis `DB_ROOT_PASSWORD`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`, `DB_PORT`
- [ ] Validar que `Dockerfile` (multi-stage build) compila e sobe sem erros após correção do `pom.xml`

#### ✅ Rafael — DEV Auth
**Pastas de responsabilidade:** `infra/security/`, `user/service/AuthService*`, `user/controller/AuthController`, `user/dto/*/Login*`

- [ ] Preencher `infra/security/SecurityConfiguration.java` — `SecurityFilterChain` liberando `/api/v1/auth/**` e Swagger (`/swagger-ui/**`, `/v3/api-docs/**`), bloqueando demais
- [ ] Criar `infra/security/JwtTokenProvider.java` — geração e validação de tokens JWT com JJWT
- [ ] Criar `infra/security/JwtAuthFilter.java` — `OncePerRequestFilter` que extrai e valida JWT no header `Authorization`
- [ ] Criar `user/dto/request/LoginRequestDTO.java` e `user/dto/response/LoginResponseDTO.java`

#### ✅ João — DEV CRUD
**Pastas de responsabilidade:** `user/dto/`, `user/mapper/`, `user/service/UserService*`, `user/controller/UserController`, `user/strategy/`, `infra/exception/handle/`

- [ ] Preencher `user/dto/request/UserRequestDTO.java` — campos `nome`, `email`, `login`, `senha`, `endereco`, `role` com validações `@NotBlank`, `@Email`, `@Size`
- [ ] Criar `user/dto/request/UpdateUserRequestDTO.java` — sem campo `senha`
- [ ] Criar `user/dto/request/ChangePasswordRequestDTO.java` — `senhaAtual` + `novaSenha`
- [ ] Preencher `user/dto/response/UserResponseDTO.java` — sem expor `senha`
- [ ] Preencher `user/mapper/UserMapper.java` com `@Mapper(componentModel = "spring")` via MapStruct
- [ ] Definir assinaturas dos métodos em `user/service/UserService.java`

#### ✅ Vine — DEV Docs
**Pastas de responsabilidade:** `config/OpenApiConfig`, `user/controller/docs/`, `src/test/`

- [ ] Criar `config/OpenApiConfig.java` com título, versão e descrição via SpringDoc OpenAPI
- [ ] Preencher `user/controller/docs/UserControllerDocs.java` com `@Tag` e `@Operation` para cada endpoint
- [ ] Preencher `user/controller/docs/AuthControllerDocs.java`

---

### Sprint 2 — Implementação e Funcionalidades (Semanas 3–4)

**Objetivo:** Entregar todos os requisitos funcionais (RF-001 a RF-010) integrados e funcionando.

**Sprint Goal:** CRUD completo de usuários, login com JWT retornando token, Swagger documentado.

#### ✅ Felipe — Tech Lead

- [ ] **Code Review Contínuo:** Revisar todos os PRs de `feature/usuario-crud` e `feature/autenticacao` antes de merge em `develop`
- [ ] Criar migrations adicionais conforme necessidade (`V2__...`)
- [ ] Apoiar Rafael na integração do `JwtAuthFilter` com o `SecurityFilterChain`
- [ ] Validar que `UserMapper` (MapStruct) não vaza entidades JPA nos responses

#### ✅ João — DEV CRUD

- [ ] Implementar `UserServiceImpl.java`:
  - `create(UserRequestDTO)` — criptografar senha com BCrypt, validar e-mail único, persistir
  - `findById(UUID)` — lançar `NotFoundException` se não encontrado
  - `findByNome(String)` — delegar para `UserRepository.findByNomeContainingIgnoreCase`
  - `update(UUID, UpdateUserRequestDTO)` — não permite alterar senha, atualiza `dataUltimaAlteracao`
  - `changePassword(UUID, ChangePasswordRequestDTO)` — validar senha atual antes de trocar
  - `delete(UUID)` — hard delete
- [ ] Implementar `UserController.java` — rotas REST versionadas:
  - `POST /api/v1/users` → cadastro (201 Created + Location header)
  - `GET /api/v1/users?nome=` → busca por nome (200 OK)
  - `PUT /api/v1/users/{id}` → atualizar dados sem senha (200 OK)
  - `PATCH /api/v1/users/{id}/senha` → trocar senha (204 No Content)
  - `DELETE /api/v1/users/{id}` → excluir (204 No Content)
- [ ] Implementar `ValidateUserStrategy.java` — verificação de unicidade de e-mail
- [ ] Implementar `GlobalExceptionHandler.java` — RFC 7807: `NotFoundException` (404), `MethodArgumentNotValidException` (400), `DataIntegrityViolationException` (409)

#### ✅ Rafael — DEV Auth

- [ ] Implementar `AuthServiceImpl.java` — validar login/senha, gerar JWT, retornar `LoginResponseDTO`
- [ ] Implementar `AuthController.java` — `POST /api/v1/auth/login` (200 OK com token)
- [ ] Finalizar integração `JwtAuthFilter` no `SecurityFilterChain`
- [ ] Configurar `BCryptPasswordEncoder` como `@Bean` em `SecurityConfiguration`

#### ✅ Gabriel — QA / Infra

- [ ] Construir Collection Postman completa:
  - 📁 `1 - Usuarios` — POST cadastrar Cliente, POST cadastrar Dono Restaurante, GET buscar por nome, PUT atualizar, PATCH senha, DELETE
  - 📁 `2 - Autenticação` — POST login (sucesso e credenciais inválidas)
  - 📁 `3 - Cenários de Erro` — e-mail duplicado (409), usuário não encontrado (404), validação (400)
  - 📁 `4 - Health & Docs` — GET `/actuator/health`, GET `/swagger-ui.html`

#### ✅ Vine — DEV Docs

- [ ] Iniciar `UserServiceImplTest.java` — cobrir `create`, `findById`, `update`, `changePassword`, `delete` (JUnit 5 + Mockito)
- [ ] Iniciar `AuthServiceImplTest.java` — cobrir login com sucesso e credenciais inválidas

---

### Sprint 3 — Qualidade, Estabilização e Entrega (Semanas 5–6)

**Objetivo:** Cobertura de testes ≥ 70%, erros padronizados, documentação completa para entrega.

**Sprint Goal:** Sistema pronto para entrega, todos os itens do Checklist de Entrega marcados.

#### ✅ Felipe — Tech Lead

- [ ] **Code Review Final:** Aprovação de todos os PRs antes do merge em `main`
- [ ] Otimizar queries nos repositórios — revisar se `findByNomeContainingIgnoreCase` está usando índice
- [ ] Validar Checklist de Entrega completo: README, `docker-compose up --build`, Postman exportada em `/docs/postman/`
- [ ] Revisar Diagrama de Classes e Relatório Técnico antes da geração do PDF

#### ✅ João — DEV CRUD

- [ ] Refinar `GlobalExceptionHandler` — garantir cobertura de todos os casos de erro mapeados
- [ ] Garantir HTTP status codes corretos em todos os endpoints (201, 200, 204, 400, 404, 409)

#### ✅ Rafael — DEV Auth

- [ ] Configurar roles `ROLE_CLIENTE` / `ROLE_DONO_RESTAURANTE` com `@PreAuthorize` onde aplicável
- [ ] Garantir que rotas protegidas retornam 401 (sem token) e 403 (sem permissão) adequadamente

#### ✅ Gabriel — QA / Infra

- [ ] Executar collection Postman via Newman e gerar relatório de testes de integração
- [ ] Validar `docker-compose up --build` do zero em ambiente limpo e documentar passo-a-passo

#### ✅ Vine — DEV Docs

- [ ] Garantir cobertura ≥ 70% nos testes unitários de `UserServiceImpl` e `AuthServiceImpl`
- [ ] Completar README — passo-a-passo Docker Compose, exemplos de requests/responses, prints do Swagger
- [ ] Gerar PDF do Relatório Técnico final com: arquitetura, modelagem de entidades, endpoints, prints Swagger/Postman, estrutura do banco, passo-a-passo Docker

---

## ✅ Critérios de Sucesso — Definition of Done (DoD)

Para que o Tech Lead **aprove** uma tarefa e libere o merge, ela deve obrigatoriamente cumprir:

| # | Critério | Verificação |
|---|----------|-------------|
| 1 | **Revisão de Pares** | Pelo menos 1 aprovação de PR por membro do time |
| 2 | **Qualidade** | Testes unitários passando com **≥ 70% de cobertura** |
| 3 | **Documentação** | Swagger atualizado e acessível em `/swagger-ui.html` |
| 4 | **Operação** | Container Docker rodando sem erros via `docker-compose up` |
| 5 | **Migrations** | Flyway migration aplicada com sucesso (`flyway_schema_history` populado) |
| 6 | **Integração** | Testes Postman validados para o endpoint entregue |

---

## 🌿 Fluxo Git

```
main (protegida)
└── develop
    ├── feature/usuario-crud          → João
    ├── feature/autenticacao          → Rafael
    ├── feature/docker-infra          → Gabriel
    └── feature/tests-qa              → Vine
```

> **Regra:** Nenhum PR é mergeado em `develop` sem aprovação do Tech Lead (Felipe).
> **Regra:** Nunca alterar uma migration Flyway já aplicada — criar sempre uma nova (`V2__`, `V3__`...).

---

*Documento mantido pelo Tech Lead. Última atualização: Abril 2026 — v2.0 (alinhado à estrutura real do repositório).*

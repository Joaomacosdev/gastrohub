# 🏛️ Decisões Arquiteturais e Visão de Produto
### GastroHub — Sistema de Gestão de Restaurantes
> **Autor:** Felipe (Tech Lead) | **Data:** Abril 2026 | **Versão:** 1.0

---

## 1. Contexto

Embora a **Fase 1** entregue apenas o módulo de **Gestão de Usuários**, o sistema como um todo é uma plataforma de gestão de restaurantes com pedidos online, avaliações e múltiplos atores. As decisões tomadas agora impactam diretamente a escalabilidade das fases futuras.

Este documento registra:
- Decisões arquiteturais tomadas na Fase 1
- Pontos de atenção identificados
- Visão de evolução do domínio para fases futuras

---

## 2. Modelo de Domínio — Visão Completa do Produto

```
User (Fase 1 — base)
 ├── Cliente
 │    ├── Pedido ──────────── ItemPedido ──── Prato / Bebida    (Fase 2)
 │    └── Avaliacao ────────── Restaurante                      (Fase 2)
 │
 ├── DonoRestaurante
 │    └── Restaurante
 │         ├── Cardapio ────── Prato / Bebida                   (Fase 2)
 │         ├── Mesa                                             (Fase 3)
 │         └── Garcom ──────── Pedido (vínculo de comissão)     (Fase 3)
 │
 └── Garcom  ◄────────────────────────────── novo perfil        (Fase 3)
      ├── restaurante (FK)
      ├── percentualComissao
      └── Comissao ────────── Pedido (cálculo por período)      (Fase 3)
```

---

## 3. Decisões Tomadas na Fase 1

### 3.1 `User` como classe concreta única com campo `role`

**O que foi feito:** `User.java` é uma entidade JPA concreta com enum `Role` (`ROLE_CLIENTE`, `ROLE_DONO_RESTAURANTE`).

**Por que é válido na Fase 1:** Requisito da fase é apenas CRUD de usuários, sem atributos exclusivos por perfil.

**Impacto futuro:** Na Fase 2/3, `Cliente`, `DonoRestaurante` e `Garcom` terão atributos exclusivos. A estratégia de herança deverá ser aplicada.

**Estratégia recomendada para Fase 2:**
```
@Inheritance(strategy = InheritanceType.JOINED)
```
- Tabela base `users` mantém os atributos comuns
- Tabela `clientes` — atributos exclusivos do cliente
- Tabela `donos_restaurante` — CNPJ, dados do estabelecimento
- Tabela `garcons` — vínculo com restaurante, percentual de comissão

> ⚠️ **Não usar** `SINGLE_TABLE` — geraria dezenas de colunas `NULL` à medida que novos perfis forem adicionados.

---

### 3.2 `Endereco` acoplado ao domínio `user/`

**O que foi feito:** `Endereco.java` está em `user/entity/` como `@Embeddable`.

**Problema identificado:** Na Fase 2, `Restaurante` também terá endereço. Manter `Endereco` em `user/` cria dependência indevida entre domínios.

**Ação planejada para Fase 2:**
```
src/main/java/br/com/gastrohub/
└── shared/
    └── entity/
        └── Endereco.java   ← mover para cá
```

Isso permite que `User`, `Restaurante` e qualquer futura entidade que precise de endereço referenciem o mesmo `@Embeddable` sem duplicação.

---

### 3.3 `AuthService` dentro do domínio `user/`

**O que foi feito:** `AuthService`, `AuthServiceImpl` e `AuthController` estão em `user/`.

**Justificativa:** Na Fase 1, autenticação opera exclusivamente sobre usuários.

**Ação planejada para Fase 2:** Extrair para pacote próprio `auth/` quando outros domínios precisarem de autenticação (ex: autenticação de restaurantes, tokens de garçons).

---

### 3.4 Módulo `notification/` e `spring-boot-starter-mail`

**O que foi feito:** O módulo `notification/` existe na estrutura e `spring-boot-starter-mail` está no `pom.xml`.

**Justificativa:** Placeholder intencional para Fase 2. Não há RF de notificação na Fase 1.

**Casos de uso futuros previstos:**
- Confirmação de cadastro por e-mail
- Notificação de novo pedido para o Dono do Restaurante
- Atualização de status do pedido para o Cliente
- Aviso de comissão calculada para o Garçom

> A dependência permanece no `pom.xml` mas **não será configurada nem exposta** na Fase 1.

---

## 4. Novo Ator Identificado — Garçom (Fase 3)

### 4.1 Contexto

Um restaurante físico opera com garçons que atendem mesas, registram pedidos presencialmente e recebem comissão sobre os pedidos atendidos. Este ator não foi previsto na documentação original do Tech Challenge, mas é natural ao domínio de um sistema de gestão de restaurantes.

### 4.2 Atributos Previstos

| Atributo | Tipo | Obrigatoriedade | Observação |
|----------|------|----------------|------------|
| `id` | UUID | Obrigatório | Herdado de `User` |
| `nome` | String | Obrigatório | Herdado de `User` |
| `email` | String | Obrigatório | Herdado de `User` |
| `login` / `senha` | String | Obrigatório | Herdado de `User` |
| `restaurante` | FK → Restaurante | Obrigatório | Vínculo com o estabelecimento |
| `percentualComissao` | BigDecimal | Obrigatório | Ex: `0.10` = 10% |
| `ativo` | Boolean | Obrigatório | Controle de vínculo ativo/inativo |
| `dataAdmissao` | LocalDate | Obrigatório | Início do vínculo |
| `dataDemissao` | LocalDate | Opcional | Fim do vínculo |

### 4.3 Modelo de Comissão

A comissão será calculada com base nos pedidos atendidos pelo garçom em um período.

```
Comissao
 ├── id (UUID)
 ├── garcom (FK → Garcom)
 ├── pedido (FK → Pedido)
 ├── valorPedido (BigDecimal)
 ├── percentualAplicado (BigDecimal)
 ├── valorComissao (BigDecimal)  ← valorPedido × percentualAplicado
 └── dataCalculo (LocalDateTime)
```

**Regras de negócio previstas:**
- Comissão calculada apenas sobre pedidos com status `ENTREGUE` ou `PAGO`
- Cálculo pode ser por período (semanal, quinzenal, mensal)
- Garçom pode ter percentual individual ou herdar o padrão do restaurante
- Histórico de comissões deve ser imutável (não alterar após cálculo)

### 4.4 Endpoints Futuros Previstos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/v1/garcons` | Cadastrar garçom |
| `GET` | `/api/v1/garcons/{id}/comissoes` | Listar comissões do garçom |
| `GET` | `/api/v1/garcons/{id}/comissoes?inicio=&fim=` | Comissões por período |
| `POST` | `/api/v1/restaurantes/{id}/comissoes/calcular` | Calcular comissões do período |
| `GET` | `/api/v1/restaurantes/{id}/garcons` | Listar garçons do restaurante |

---

## 5. Visão de Evolução por Fase

| Fase | Módulo | Novos Atores / Entidades | Impacto em `User` |
|------|--------|--------------------------|-------------------|
| **Fase 1** | Gestão de Usuários | `User` (Cliente, DonoRestaurante) | Base criada |
| **Fase 2** | Pedidos e Cardápio | `Restaurante`, `Cardapio`, `Prato`, `Bebida`, `Pedido`, `ItemPedido`, `Avaliacao` | Herança `JOINED` + `shared/Endereco` |
| **Fase 3** | Operação Presencial | `Garcom`, `Mesa`, `Comissao` | Novo perfil em `Role` + tabela `garcons` |
| **Fase 4** | Financeiro / BI | `Relatorio`, `Fechamento`, `FluxoCaixa` | Dados históricos de pedidos e comissões |

---

## 6. Impacto Imediato na Fase 1 — O que preparar agora

| Ação | Responsável | Quando | Justificativa |
|------|-------------|--------|---------------|
| Adicionar `ROLE_GARCOM` no enum `Role` | Felipe | Sprint 2 | Evita migration futura em tabela com dados |
| Planejar `V2__` com estratégia `JOINED` | Felipe | Sprint 3 | Decisão de herança antes de ter dados em produção |
| Mover `Endereco` para `shared/` | Felipe | Sprint 2 | Antes do `Restaurante` ser criado na Fase 2 |
| Documentar regras de comissão no backlog | Tech Lead | Sprint 3 | Registrar regras de negócio antes de esquecer |

---

## 7. Riscos Identificados

| # | Risco | Probabilidade | Impacto | Mitigação |
|---|-------|---------------|---------|-----------|
| 1 | Refatoração de herança com dados em produção | Alta | Alto | Definir `JOINED` antes da Fase 2 ir a produção |
| 2 | `Endereco` duplicado em múltiplos domínios | Alta | Médio | Mover para `shared/` no início da Fase 2 |
| 3 | Cálculo de comissão incorreto por regra mal definida | Média | Alto | Documentar e revisar regras antes de implementar |
| 4 | Garçom sem vínculo de restaurante acessar pedidos de outros estabelecimentos | Média | Alto | Filtro por `restauranteId` obrigatório em todos os endpoints de garçom |

---

*Documento mantido pelo Tech Lead. Última atualização: Abril 2026 — v1.0*
*Próxima revisão prevista: início da Fase 2*


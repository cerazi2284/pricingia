# Arquitetura — Pricing IA SaaS

## Visão do monorepo

Repositório único de produto com aplicações separadas:

| Pasta | Responsabilidade |
|---|---|
| `backend/` | API, domínio, integrações, mensageria |
| `frontend/` | UI do lojista (React, futuro) |
| `infra/` | Docker Compose, scripts de ambiente |
| `docs/` | Documentação técnica e status |

## Backend — padrão macro

- **Modular Monolith** em um único deploy
- **DDD** para bounded contexts em `modules/`
- **Hexagonal Architecture** no módulo `pricing`
- **Vertical Slice** para fluxos simples
- **CQRS básico** em `analytics` (futuro)

## Regras de dependência

- Controller **não** chama repository direto
- Controller **não** contém regra de negócio
- Domínio **não** depende de Spring, JPA, HTTP, Shopify ou IA
- Hibernate **não** cria tabelas (`ddl-auto=none`)
- Migrations via **Flyway** em `backend/src/main/resources/db/migration`

## Fluxo principal (alvo)

1. Shopify envia webhook
2. Módulo `webhook` valida HMAC e publica no RabbitMQ
3. Retorna `202 Accepted`
4. Worker de `pricing` consome, aplica regras e opcionalmente IA
5. Persiste decisão e atualiza Shopify
6. `analytics` alimenta o dashboard

## Referências

- [Status do projeto](../status_projeto.md)
- [Diagramas](../diagrams/README.md)

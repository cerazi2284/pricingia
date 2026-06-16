# Backend — Pricing IA SaaS

API em **Java 21** + **Spring Boot 4** (modular monolith).

## Package base

`com.pricingia.saas`

## Estrutura interna

```text
src/main/java/com/pricingia/saas/
├── common/           # config, security, exception, web, observability
└── modules/          # bounded contexts
    ├── merchant/
    ├── shopify/
    ├── webhook/
    ├── pricing/
    ├── billing/
    ├── analytics/
    └── ai/
```

## Comandos

Suba a infra na raiz do monorepo antes de rodar a API:

```bash
docker compose -f infra/local/docker-compose.yml up -d
```

Depois, nesta pasta:

```bash
mvn clean test
mvn spring-boot:run
```

## Endpoints de verificação

- http://localhost:8080/actuator/health
- http://localhost:8080/actuator/info

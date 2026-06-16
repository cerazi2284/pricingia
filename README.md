# Pricing IA SaaS

Monorepo de produto para o assistente de precificação dinâmica com IA para lojistas Shopify.

## Estrutura

```text
pricingia/
├── backend/          # API Java 21 + Spring Boot 4
├── frontend/         # Dashboard React (futuro)
├── infra/            # Docker Compose e automação de ambiente
├── docs/             # Status, arquitetura e diagramas
└── .github/          # CI/CD
```

Cada aplicação mantém sua própria arquitetura. Backend e frontend **não** compartilham código nem pastas de implementação.

## Pré-requisitos

- Java 21
- Maven 3.9+
- Docker Desktop

## Como rodar localmente

### 1. Infraestrutura (PostgreSQL + RabbitMQ)

Na raiz do repositório:

```bash
docker compose -f infra/local/docker-compose.yml up -d
```

### 2. Backend

```bash
cd backend
mvn clean test
mvn spring-boot:run
```

### 3. Verificações

- Health: http://localhost:8080/actuator/health
- RabbitMQ: http://localhost:15672 (`pricingia_user` / `pricingia_pass`)

## Documentação

- [Status do projeto](docs/status_projeto.md)
- [Arquitetura](docs/architecture/README.md)
- [Diagramas](docs/diagrams/README.md)

# Status do Projeto — Pricing IA SaaS

## Etapas

### Etapa 1 — Fundação ✅ Concluída

**Data de conclusão:** 2026-06-16

#### Entregas

- [x] `application.properties` criado e `application.yaml` removido
- [x] `docker-compose.yml` em `infra/local/` (PostgreSQL e RabbitMQ)
- [x] Flyway inicial criado (`V1__create_initial_schema.sql`)
- [x] Schema `pricingia` criado
- [x] Pacotes base `common` e `modules` criados
- [x] Security temporária liberando apenas `/actuator/health` e `/actuator/info`
- [x] App rodando com `/actuator/health` = `UP`
- [x] Monorepo reorganizado (`backend/`, `frontend/`, `infra/`, `docs/`)

#### Validação local

Na raiz do monorepo:

```bash
docker compose -f infra/local/docker-compose.yml up -d
```

No diretório `backend/`:

```bash
cd backend
mvn clean test
mvn spring-boot:run
```

- Health: http://localhost:8080/actuator/health → `{"status":"UP"}`
- RabbitMQ: http://localhost:15672 (`pricingia_user` / `pricingia_pass`)

#### Fora do escopo desta etapa (não implementado)

- Controllers / endpoints de negócio
- Frontend
- Spring AI
- Redis
- Integração Shopify
- `GlobalExceptionHandler` / `ApiErrorResponse`

---

### Etapa 2 — Tratamento global de erros ⏳ Pendente

- `ApiErrorResponse`
- `GlobalExceptionHandler`
- Exceções base de domínio
- Testes do handler com MockMvc

---

### Etapas futuras

- Módulo `merchant`
- Módulo `webhook` + RabbitMQ
- Módulo `pricing` (hexagonal)
- Módulo `shopify`
- Módulo `billing`
- Módulo `analytics`
- Módulo `ai`
- Frontend React + Vite

# Backend — Pricing IA SaaS

API em **Java 21** + **Spring Boot 4** (modular monolith).

## Package base

`com.pricingia.saas`

## Estrutura interna

```text
src/main/java/com/pricingia/saas/
├── common/           # config (AMQP), security, exception (handler global)
└── modules/          # bounded contexts
    ├── merchant/     # settings do lojista + produtos monitorados
    ├── shopify/      # validação HMAC e tipos da Shopify
    ├── webhook/      # recebimento, dedupe e publicação de eventos
    ├── pricing/      # regra de preço + decisões + consumer
    ├── analytics/    # resumo para dashboard
    ├── billing/      # placeholder
    └── ai/           # placeholder (IA entra depois)
```

Cada módulo segue camadas: `presentation` → `application` → `infrastructure`, com `domain` puro
(sem Spring/JPA/HTTP). A regra de preço (`PricingRule`) e o HMAC (`ShopifyHmacVerifier`) são
domínio puro e testados sem Spring.

## Subir infra local

Na raiz do monorepo:

```bash
docker compose -f infra/local/docker-compose.yml up -d
```

Sobe PostgreSQL (5432) e RabbitMQ (5672 / painel 15672).

## Rodar o backend

Nesta pasta (`backend/`):

```bash
mvn spring-boot:run
```

No startup é criado um **merchant demo** (`demo-shop.myshopify.com`, moeda `USD`) se não existir.

## Rodar os testes

```bash
mvn clean test
```

Testes de integração usam **Testcontainers** (PostgreSQL e RabbitMQ reais). Docker precisa estar rodando.
Convenção: unitários terminam em `Test`, integração em `IT`.

## Configuração

- Secret do webhook vem de config (nunca hardcoded):
  `pricingia.shopify.webhook.secret` (default dev: `local-dev-secret`, override via
  env `PRICINGIA_SHOPIFY_WEBHOOK_SECRET`).
- RabbitMQ: exchange `shopify.webhooks.exchange`, queue `shopify.webhooks.received.queue`,
  routing key `shopify.webhook.received`.
- **CORS** habilitado para `http://localhost:*` (frontend Vite em `5173`).
- Frontend em `frontend/` consome `/api/**` via proxy Vite ou CORS direto.

## Frontend integrado

O frontend React consome as APIs deste backend. Rode:

```bash
cd ../frontend && npm run dev
```

Abra `http://localhost:5173` → dashboard, products, pricing decisions e settings usam dados reais.

## Endpoints

Verificação:
- `GET /actuator/health`
- `GET /actuator/info`

Merchant settings:
- `GET /api/merchant/settings`
- `PUT /api/merchant/settings`

Produtos monitorados:
- `GET /api/products`
- `GET /api/products/{id}`
- `POST /api/products/demo` — **temporário**, semeia produtos mockados no banco local

Decisões de preço:
- `GET /api/pricing/decisions`
- `GET /api/pricing/decisions/{id}`
- `POST /api/pricing/decisions/{id}/approve`
- `POST /api/pricing/decisions/{id}/reject`

Analytics:
- `GET /api/analytics/summary`

Webhook Shopify:
- `POST /webhooks/shopify` (headers: `X-Shopify-Hmac-Sha256`, `X-Shopify-Webhook-Id`,
  `X-Shopify-Topic`, `X-Shopify-Shop-Domain`)

### Comportamento do webhook

| Situação                | HTTP |
|-------------------------|------|
| HMAC válido + novo      | 200 (salva + publica no RabbitMQ) |
| HMAC inválido           | 401  |
| Header obrigatório falta | 400 |
| Duplicado (mesmo id)    | 200 (não republica) |
| RabbitMQ indisponível   | 503  |

## Exemplos

### Criar produtos demo

```bash
curl -i -X POST http://localhost:8080/api/products/demo
```

### Enviar webhook assinado (PowerShell)

```powershell
$secret = "local-dev-secret"
$body   = '{"shopify_product_id":"gid://shopify/Product/1001","topic":"products/update"}'
$mac    = [System.Security.Cryptography.HMACSHA256]::new([Text.Encoding]::UTF8.GetBytes($secret))
$sig    = [Convert]::ToBase64String($mac.ComputeHash([Text.Encoding]::UTF8.GetBytes($body)))

Invoke-RestMethod -Uri "http://localhost:8080/webhooks/shopify" -Method Post `
  -ContentType "application/json" -Body $body -Headers @{
    "X-Shopify-Hmac-Sha256" = $sig
    "X-Shopify-Webhook-Id"  = "local-001"
    "X-Shopify-Topic"       = "products/update"
    "X-Shopify-Shop-Domain" = "demo-shop.myshopify.com"
  }
```

### Enviar webhook assinado (bash/curl)

```bash
SECRET="local-dev-secret"
BODY='{"shopify_product_id":"gid://shopify/Product/1001","topic":"products/update"}'
SIG=$(printf '%s' "$BODY" | openssl dgst -sha256 -hmac "$SECRET" -binary | base64)

curl -i -X POST http://localhost:8080/webhooks/shopify \
  -H "Content-Type: application/json" \
  -H "X-Shopify-Hmac-Sha256: $SIG" \
  -H "X-Shopify-Webhook-Id: local-001" \
  -H "X-Shopify-Topic: products/update" \
  -H "X-Shopify-Shop-Domain: demo-shop.myshopify.com" \
  -d "$BODY"
```

Depois cheque a decisão gerada:

```bash
curl http://localhost:8080/api/pricing/decisions
```

## Limitações do MVP (intencional)

- Sem integração real com Shopify Admin API (HMAC validado, mas nada é buscado/atualizado lá).
- Sem IA real — regra de preço é determinística (`PricingRule`).
- Sem autenticação de usuário: `/api/**` está liberado para dev local; `/webhooks/shopify` é
  autenticado por HMAC. Auth real (login/OAuth) virá depois.
- Single-tenant: um merchant demo. Multi-tenant virá depois.
- `POST /api/products/demo` é temporário (seed local).
```

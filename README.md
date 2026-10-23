# ecommerce-platform

Projeto de estudo de **microservices em Java**, com foco nos principais *cross-cutting concerns* (segurança, resiliência, observabilidade, consistência de dados, contratos) e nos dois estilos de comunicação entre serviços: **síncrona** (REST/gRPC) e **assíncrona** (Kafka).

O domínio é uma plataforma de pedidos simplificada: um pedido depende de catálogo, estoque e pagamento, o que gera cenários reais de falha parcial, retentativa e consistência.

## Serviços

| Serviço | Responsabilidade | Banco | Status |
|---|---|---|---|
| `catalog-service` | Produtos e preços | `catalog_db` | Em funcionamento (CRUD) |
| `order-service` | Criação e acompanhamento de pedidos | `order_db` | Planejado |
| `inventory-service` | Reserva e liberação de estoque | `inventory_db` | Planejado |
| `payment-service` | Cobrança simulada, com falhas configuráveis | `payment_db` | Planejado |
| `notification-service` | Consome eventos e notifica | - | Planejado |
| `api-gateway` | Entrada única, JWT, rate limiting | - | Planejado |

## Stack

Java 21, Spring Boot 4.1.x, Maven (Maven Wrapper em cada serviço), PostgreSQL 16 com Flyway, Kafka (KRaft), Keycloak, Docker Compose. Observabilidade (OpenTelemetry), resiliência (Resilience4j) e testes de contrato (Pact) entram nas fases seguintes.

## Estrutura do repositório

```
ecommerce-platform/
├── docs/adr/            # decisões de arquitetura (ADRs)
├── infra/               # docker-compose.yml, init-databases.sql
└── services/
    └── catalog-service/ # um projeto Maven independente por serviço
```

Cada serviço é um projeto independente: tem seu próprio `pom.xml`, seu banco e é publicado sem depender dos outros. Não há `pom.xml` pai agregando os serviços. Veja o [ADR 0002](docs/adr/0002-monorepo-com-servicos-independentes.md).

## Como rodar

Pré-requisitos: Docker e **JDK 21** (o pacote `jdk`, não apenas o JRE).

```bash
# 1. Infraestrutura
cd infra
docker compose up -d

# 2. Serviço
cd ../services/catalog-service
./mvnw spring-boot:run
```

Teste:

```bash
curl localhost:8081/products
curl localhost:8081/actuator/health
```

### Portas e acessos locais

| Componente | Endereço | Credenciais |
|---|---|---|
| catalog-service | http://localhost:8081 | - |
| PostgreSQL | localhost:5432 | usuário/senha por serviço (ex.: `catalog`/`catalog`) |
| Kafka | localhost:9092 | - |
| Kafka UI | http://localhost:8089 | - |
| Keycloak | http://localhost:8180 | `admin`/`admin` |

Todas as credenciais são de **desenvolvimento local**. Não as reutilize em outro ambiente.

Se alterar `infra/init-databases.sql` depois da primeira subida, recrie os volumes (apaga os dados locais):

```bash
docker compose down -v && docker compose up -d
```

## Roteiro

1. **Fundação:** catalog e order com REST síncrono, Postgres, Flyway
2. **Segurança:** Keycloak e JWT no gateway e nos serviços
3. **Assíncrono:** Kafka, inventory-service, fluxo `OrderCreated` → `StockReserved`
4. **Saga completa:** pagamento, compensações, notificação
5. **Confiabilidade:** Outbox, idempotência, DLQ, Resilience4j
6. **Observabilidade:** OpenTelemetry, métricas, tracing ponta a ponta
7. **Contratos e testes:** Pact, Schema Registry, Testcontainers, gRPC
8. **Plataforma:** Kubernetes, configuração e segredos, probes

## Decisões de arquitetura

As decisões e seus motivos ficam em [`docs/adr`](docs/adr/).

| ADR | Decisão |
|---|---|
| [0001](docs/adr/0001-um-banco-por-servico.md) | Um banco por serviço |
| [0002](docs/adr/0002-monorepo-com-servicos-independentes.md) | Monorepo com serviços independentes |
| [0003](docs/adr/0003-spring-boot-4-e-java-21.md) | Spring Boot 4.1 e Java 21 |

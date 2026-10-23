# 0001. Um banco por serviço

- Status: aceita
- Data: 2026-10-23

## Contexto

Os serviços precisam evoluir e ser publicados de forma independente. Um banco compartilhado criaria acoplamento pelo schema: qualquer mudança de tabela poderia quebrar outro serviço, e a posse dos dados ficaria ambígua. Ao mesmo tempo, o ambiente local precisa ser leve, e subir uma instância de Postgres por serviço seria desperdício de recursos.

## Decisão

Cada serviço tem seu **próprio banco e seu próprio usuário**, e apenas ele acessa esse banco. Nenhum serviço lê tabelas de outro: o acesso a dados alheios é sempre por contrato (API ou evento).

No ambiente local, os bancos (`catalog_db`, `order_db`, `inventory_db`, `payment_db`) ficam numa **única instância** de PostgreSQL, criados por `infra/init-databases.sql`, cada um com um usuário dono dedicado. As migrações de cada serviço são gerenciadas pelo Flyway.

## Alternativas consideradas

- **Banco único e schema compartilhado:** simples, mas acopla os serviços e impede deploy independente.
- **Uma instância de Postgres por serviço:** mais fiel à produção, porém custosa para desenvolvimento local. Pode ser adotada na fase de plataforma (Kubernetes).
- **Schemas separados no mesmo banco:** isola menos que bancos distintos e facilita acessos indevidos entre serviços.

## Consequências

- Cada serviço é dono dos seus dados e pode mudar seu schema sem coordenar com os demais.
- Não há *joins* nem transações entre serviços. A consistência entre eles passa a depender de padrões como Saga e Outbox, que fazem parte do estudo.
- Dados necessários a outro serviço são copiados no momento certo. Exemplo: o pedido guarda o preço unitário do produto no momento da compra, em vez de consultar o catálogo depois.
- Em produção seriam instâncias separadas. A instância única local é uma concessão de ambiente, não um padrão a repetir.

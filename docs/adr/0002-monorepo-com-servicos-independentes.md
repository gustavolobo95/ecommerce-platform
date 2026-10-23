# 0002. Monorepo com serviços independentes

- Status: aceita
- Data: 2026-10-23

## Contexto

A arquitetura é de microservices, e a dúvida era se manter todos os serviços em um só repositório contradiz o conceito. O que define microservices é a independência de implantação, de dados e de código em tempo de execução, e não a localização dos arquivos. Para um projeto de estudo, é útil subir tudo com um clone, compartilhar a infraestrutura e alterar contratos entre serviços no mesmo commit.

## Decisão

Usar **um único repositório (monorepo)** com a seguinte organização:

- `services/<nome>`: um projeto Maven **completamente independente** por serviço, com `pom.xml`, Maven Wrapper e build próprios
- `infra/`: infraestrutura local compartilhada (Docker Compose, scripts de banco)
- `docs/`: documentação e ADRs

Regras que preservam a independência:

1. **Não haver `pom.xml` pai** que agregue os serviços como módulos, nem build conjunto.
2. **Não compartilhar classes de domínio** entre serviços. Cada serviço tem seus próprios DTOs, mesmo que fiquem parecidos.
3. Uma biblioteca em `libs/`, se existir, é **somente técnica ou de contratos** (proto, schemas), nunca regra de negócio.
4. A comunicação entre serviços é feita apenas por HTTP, gRPC ou eventos.

## Alternativas consideradas

- **Um repositório por serviço (polyrepo):** isolamento mais forte de histórico, permissões e CI, porém mais custo para mudanças coordenadas. Pode ser adotado depois: cada pasta de `services/` já é autônoma e pode virar um repositório próprio.
- **Monorepo com `pom.xml` pai multi-módulo:** facilita a importação na IDE, mas induz a build e versionamento conjuntos, aproximando o projeto de um monólito distribuído.

## Consequências

- Mudanças que atravessam serviços cabem em um só commit e pull request.
- Exige disciplina para não criar acoplamento, o que as regras acima tentam garantir.
- Cada novo serviço precisa ser vinculado manualmente na IDE (no IntelliJ, *Add as Maven Project*), já que não há `pom.xml` agregador.
- O CI futuro deverá construir e testar apenas os serviços cujas pastas mudaram.
- Separar em repositórios no futuro é uma mudança simples e serve de exercício de CI/CD por serviço.

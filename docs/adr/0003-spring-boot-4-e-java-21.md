# 0003. Spring Boot 4.1 e Java 21

- Status: aceita
- Data: 2026-10-23

## Contexto

É preciso escolher a base tecnológica dos serviços. O Spring Boot 4.1.0 foi lançado em junho de 2026 e traz suporte a Spring gRPC e melhorias de observabilidade (OpenTelemetry), ambos relevantes para este estudo. A linha 4.1 suporta do Java 17 ao 26. O Spring Cloud compatível com o Boot 4.1 é o da linha 2025.1.x (a partir da 2025.1.2).

## Decisão

- **Spring Boot 4.1.1**, **Java 21** (LTS) e **Maven**, com o Maven Wrapper em cada serviço.
- **Spring Cloud 2025.1.x**, usando o BOM gerado pelo Spring Initializr, sem fixar versões à mão.
- Dependências entram **por fase**: segurança (OAuth2 Resource Server) na fase 2, tracing e Prometheus na fase de observabilidade, Kafka na fase assíncrona.
- Para clientes HTTP entre serviços, começar com `RestClient` ou `@HttpExchange` do próprio Spring, avaliando o OpenFeign depois para comparação.

## Alternativas consideradas

- **Spring Boot 3.5.x:** ainda suportado e com mais material de apoio, mas é a geração anterior. O objetivo é aprender algo que valha pelos próximos anos.
- **Java 25:** suportado pelo Boot 4.1, mas o Java 21 tem mais compatibilidade com ferramentas e imagens base.
- **Gradle:** viável, mas o Maven foi escolhido pela familiaridade e pelo suporte do Initializr.

## Consequências

- Parte dos tutoriais da era Boot 3 está desatualizada: starters renomeados (por exemplo `spring-boot-starter-webmvc`, `spring-boot-starter-flyway`), pacotes de autoconfiguração movidos, Spring Security 7, Hibernate 7 e Jackson 3.
- O Gateway usa o starter específico do Gateway 5 (WebFlux ou Web MVC), e não mais o `spring-cloud-starter-gateway`.
- O Boot e o Spring Cloud precisam ser atualizados em conjunto: uma combinação incompatível derruba a aplicação na subida.
- É preciso um **JDK completo** (com `javac`) na versão 21, e não apenas o JRE.

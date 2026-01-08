=# Trainer API — Arquitetura 

Este documento descreve a arquitetura, decisões técnicas, modelos de dados, diagramas, estado atual do projeto e instruções de execução da API Trainer.

## Índice
- [Visão Geral](#visão-geral)
- [Arquitetura](#arquitetura)
- [Tecnologias](#tecnologias)
- [Funcionalidades](#funcionalidades)
- [Segurança](#segurança)
- [Tratamento de Erros](#tratamento-de-erros)
- [Documentação da API](#documentação-da-api)
- [Como Executar](#como-executar)
- [Boas Práticas](#boas-práticas)
- [Próximos Passos](#próximos-passos)

---

## Visão Geral

Sistema de gerenciamento de alunos e agendamentos com autenticação via JWT. Fornece uma API REST para operações CRUD e integração com front-ends web ou mobile.

**Objetivo:** Backend RESTful simples e extensível para gerenciar agendamentos e usuários.

**Contexto de uso:** API HTTP/JSON para consumo por interfaces web, mobile ou testes automatizados.

---

## Arquitetura

### Estrutura em Camadas

Arquitetura monolítica baseada em Spring Boot seguindo o padrão Controller → Service → Repository.

**Camadas:**
- **Controller:** Expõe endpoints HTTP e mapeia requests/responses
- **Service:** Regras de negócio e validações de domínio
- **Repository:** Abstração de acesso a dados via Spring Data JPA
- **DTO:** Objetos de transferência para comunicação com clientes
- **Entity:** Modelos JPA persistidos no banco de dados

### Decisões Técnicas

- Validação centralizada na camada Service usando `Objects.requireNonNull()`
- Uso de `Optional` nos repositórios para representar ausência de recursos
- Exceções de domínio (`ResourceNotFoundException`) tratadas por `GlobalExceptionHandler`
- Autenticação stateless com JWT

---

## Tecnologias

| Categoria | Tecnologia |
|-----------|------------|
| Linguagem | Java 17 |
| Framework | Spring Boot 3.1.x (Web, Data JPA, Security, Validation) |
| Banco de Dados | PostgreSQL |
| Autenticação | JWT (io.jsonwebtoken 0.11.5) |
| Documentação | Springdoc OpenAPI / Swagger |
| Build | Maven |
| Testes | spring-boot-starter-test, spring-security-test |
| Utilitários | Lombok |

---

## Funcionalidades

### Autenticação
```
POST /api/auth/login     - Autenticação e geração de JWT
POST /api/auth/register  - Registro de novo usuário
```

### Alunos
```
GET    /api/alunos       - Listar todos os alunos
GET    /api/alunos/{cpf} - Buscar aluno por CPF
POST   /api/alunos       - Criar novo aluno
PUT    /api/alunos/{cpf} - Atualizar aluno
DELETE /api/alunos/{cpf} - Deletar aluno
```

**Identificador:** O CPF é usado como chave primária (String com 11 dígitos numéricos).

**Exemplo:** `GET /api/alunos/12345678900`

### Agendamentos
```
POST   /agendamentos         - Criar agendamento
GET    /agendamentos         - Buscar por período (query params: start, end)
PUT    /agendamentos/{id}    - Atualizar agendamento
DELETE /agendamentos/{id}    - Deletar agendamento
```

### Regras de Negócio

- Services lançam `ResourceNotFoundException` para recursos não encontrados
- `DataInitializer` cria usuário padrão `admin` com senha `admin123` para facilitar testes locais

---

## Segurança

### Implementação

**Componentes:**
- `JwtTokenProvider` — Criação e validação de tokens JWT
- `JwtAuthenticationFilter` — Extração do token do header `Authorization`
- `UserDetailsServiceImpl` — Carregamento de usuários do banco
- `SecurityConfig` — Configuração de SecurityFilterChain stateless

**Senha:** Armazenamento com BCrypt via `PasswordEncoder`.

### Geração de Segredo JWT

Recomenda-se chave HMAC com mínimo de 256 bits:
```bash
# OpenSSL
openssl rand -base64 32

# Python
python -c "import secrets, base64; print(base64.b64encode(secrets.token_bytes(32)).decode())"
```

### Uso em Desenvolvimento
```bash
export JWT_SECRET="<valor_gerado>"
mvn -Djwt.secret="$JWT_SECRET" spring-boot:run
```

### Produção

⚠️ **IMPORTANTE:** Nunca commite segredos no repositório. Use:
- Variáveis de ambiente
- Secret managers (Azure Key Vault, AWS Secrets Manager, etc.)
- Injeção de secrets em contêineres

---

## Tratamento de Erros

### Validações

Centralizadas nos Services com `Objects.requireNonNull()` para garantir contratos explícitos.

### Exceções

| Exceção | Código HTTP | Uso |
|---------|-------------|-----|
| `ResourceNotFoundException` | 404 | Recurso não encontrado |
| `IllegalArgumentException` | 400 | Argumentos inválidos |
| `NullPointerException` | 400 | Valores nulos não permitidos |
| `Exception` | 500 | Erro interno do servidor |

### Formato de Resposta
```json
{
  "timestamp": "2026-01-08T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Aluno não encontrado",
  "path": "/api/alunos/12345678900"
}
```

---

## Documentação da API

**Swagger UI:** `http://localhost:8081/swagger-ui.html`

**OpenAPI Docs:** `http://localhost:8081/api-docs`

Configuração em `application.properties`:
```properties
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

---

## Como Executar

### Pré-requisitos

- Java 17
- Maven 3.x
- PostgreSQL

### Configuração do Banco

Configuração padrão em `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/dbtrainer
spring.datasource.username=postgres
spring.datasource.password=postgres
```

Criar banco:
```bash
createdb -U postgres dbtrainer
```

### Execução
```bash
# Modo desenvolvimento
mvn spring-boot:run

# Build e execução
mvn package
java -jar target/api-0.0.1-SNAPSHOT.jar
```

**Porta:** A aplicação inicia na porta `8081` (configurável em `application.properties`).

**Usuário padrão:** `admin` / `admin123` (criado automaticamente em desenvolvimento).

---

## Boas Práticas

- Separação clara de responsabilidades entre camadas
- Validação explícita de nulidade nos Services
- Exceções de domínio para tratamento de erros
- Autenticação stateless via JWT
- Senhas criptografadas com BCrypt
- Documentação automatizada via OpenAPI/Swagger
- Uso de DTOs para desacoplamento entre API e modelo de dados

---

## Próximos Passos

### Melhorias Técnicas

1. Padronizar tipo de ID em `Aluno` (usar `String cpf` consistentemente em Repository, Controllers e DTOs)
2. Implementar testes unitários para Services (AlunoService, AgendamentoService, AuthService)
3. Adicionar testes de integração para endpoints protegidos
4. Configurar CI/CD (GitHub Actions) para build, testes e análise estática
5. Implementar validação de contratos OpenAPI em PRs

### Arquivos Principais
```
src/main/java/br/com/trainer/api/
├── entity/          # Entidades JPA
├── controller/      # Controllers REST
├── service/         # Regras de negócio
├── repository/      # Repositórios JPA
├── security/        # JWT Provider e Filtros
└── dto/             # Data Transfer Objects

src/main/resources/
└── application.properties  # Configurações da aplicação
```

---

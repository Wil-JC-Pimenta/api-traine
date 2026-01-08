# Trainer API (api)

Este documento descreve o estado atual do projeto, arquitetura, dependências, execução local e decisões técnicas. É escrito para desenvolvedores e avaliadores técnicos.

---

## 1. Visão Geral do Projeto

O sistema fornece uma API REST para gerenciamento de alunos, agendamentos e autenticação de usuários para um contexto de treinamentos/aulas.

- Problema que resolve: oferece operações CRUD para alunos e agendamentos, além de autenticação baseada em JWT, permitindo que front-ends ou consumidores integrem funcionalidades de agendamento.
- Objetivo: ser um backend RESTful simples e extensível para gerenciar agendamentos e usuários.
- Contexto de uso: API HTTP/JSON, destinada a ser consumida por UI web/mobile ou testes automatizados.

---

## 2. Arquitetura e Estrutura

# Trainer API (api)

Este documento descreve o estado atual do projeto, arquitetura, dependências, execução local e decisões técnicas. É escrito para desenvolvedores e avaliadores técnicos.

---

## 1. Visão Geral do Projeto

O sistema fornece uma API REST para gerenciamento de alunos, agendamentos e autenticação de usuários para um contexto de treinamentos/aulas.

- Problema que resolve: oferece operações CRUD para alunos e agendamentos, além de autenticação baseada em JWT, permitindo que front-ends ou consumidores integrem funcionalidades de agendamento.
- Objetivo: ser um backend RESTful simples e extensível para gerenciar agendamentos e usuários.
- Contexto de uso: API HTTP/JSON, destinada a ser consumida por UI web/mobile ou testes automatizados.

---

## 2. Arquitetura e Estrutura

Arquitetura adotada
- Aplicação monolítica construída com Spring Boot.
- Padrão em camadas: Controller → Service → Repository, com DTOs e Entities para transporte e persistência.

Separação de camadas
- Controller: expõe endpoints HTTP e mapeia requests/responses.
- Service: contém regras de negócio e validações de domínio (validação de nulidade e verificação de existência de recursos).
- Repository: abstração de acesso a dados via Spring Data JPA.
- DTO: objetos de transferência usados nos endpoints.
- Entity: modelos JPA/Hibernate persistidos no banco.

Padrões e decisões relevantes
- Validação e decisões de nulidade centralizadas na camada Service (ex.: `Objects.requireNonNull(...)`).
- Uso de `Optional` nos repositórios para representar ausência de recurso.
- Exceções de domínio (`ResourceNotFoundException`) para recursos inexistentes, tratadas por `GlobalExceptionHandler`.

Observações de consistência
- Algumas inconsistências detectadas no código (ver seção "Próximos Passos"). Elas não são inventadas — constam do estado atual do projeto.

---

## 3. Tecnologias Utilizadas

- Linguagem: Java 17
- Frameworks: Spring Boot 3.1.x (Web, Data JPA, Security, Validation)
- Persistência: PostgreSQL (driver `org.postgresql:postgresql`)
- JWT: jjwt (io.jsonwebtoken, versão 0.11.5)
- Documentação: Springdoc OpenAPI / Swagger (`springdoc-openapi-starter-webmvc-ui`)
- Build: Maven
- Testes: `spring-boot-starter-test`, `spring-security-test`
- Utilitários: Lombok (dependência opcional presente)

---

## 4. Funcionalidades Principais

Endpoints implementados (resumo):

Autenticação
- POST `/api/auth/login` — autentica usuário e retorna JWT (LoginRequest → LoginResponse)
- POST `/api/auth/register` — registra novo usuário (UserDTO)

Alunos
- GET `/api/alunos` — listar todos (requere autenticação conforme `SecurityConfig`)
- GET `/api/alunos/{id}` — buscar por id (retorna 200 ou 404)
- POST `/api/alunos` — criar aluno
- PUT `/api/alunos/{id}` — atualizar aluno (service valida id)
- DELETE `/api/alunos/{id}` — deletar aluno (service valida id)

Agendamentos
- POST `/agendamentos` — criar agendamento
- GET `/agendamentos?start=&end=` — buscar por período (GET público conforme config)
- PUT `/agendamentos/{id}` — atualizar agendamento
- DELETE `/agendamentos/{id}` — deletar agendamento

Regras de negócio notáveis
- Serviços lançam `ResourceNotFoundException` quando não localizam recursos.
- `DataInitializer` cria um usuário `admin` por padrão (para facilitar testes locais).

---

## Identificador de Aluno

O recurso Aluno é identificado de forma única pelo CPF, representado como uma String contendo 11 dígitos numéricos, utilizado como chave primária e como parâmetro nas rotas da API.

Exemplos de endpoint:
- GET `/api/alunos/{cpf}`
- Exemplo: `/api/alunos/12345678900`

---

## 5. Segurança

Resumo da implementação
- Autenticação baseada em JWT. Componentes:
  - `JwtTokenProvider` — cria e valida tokens JWT.
  - `JwtAuthenticationFilter` — extrai token do header `Authorization` e popula `SecurityContext`.
  - `UserDetailsServiceImpl` — carrega usuário do banco e retorna UserDetails.
  - `SecurityConfig` — configura SecurityFilterChain (stateless, permissões e filtros).
- Senhas armazenadas com BCrypt (`PasswordEncoder` configurado).

Observações e recomendações
- O segredo do JWT (`jwt.secret`) atualmente está em `application.properties` para desenvolvimento; não deixe segredos no repositório em produção — use variáveis de ambiente/secret manager.
- Verificar size/força da key para algoritmo HMAC (jjwt `Keys.hmacShaKeyFor(...)`).

Gerando um segredo JWT seguro

- Recomenda-se usar uma chave HMAC com pelo menos 256 bits. Uma forma simples de gerar um segredo seguro (base64) no terminal:

  ```bash
  # OpenSSL (gera 32 bytes e codifica em base64) — resultado pode ser usado como jwt.secret
  openssl rand -base64 32

  # Python — alternativa portátil
  python -c "import secrets, base64; print(base64.b64encode(secrets.token_bytes(32)).decode())"
  ```

- Como usar localmente (não commitar o valor):

  ```bash
  # exporte a variável e inicie a aplicação (exemplo de uso local)
  export JWT_SECRET="<valor_gerado>"
  # executar com propriedade sobrescrevendo o arquivo
  mvn -Djwt.secret="$JWT_SECRET" -Dspring-boot.run.profiles=dev spring-boot:run
  ```

- Em pipelines/produção, armazene a chave em um secret manager (Azure Key Vault, AWS Secrets Manager, etc.) e injete-a como variável de ambiente ou secret no contêiner. Nunca deixe `jwt.secret` com um valor curto ou a string placeholder no repositório.

---

## 6. Tratamento de Erros e Validações

- Validação de entrada: centralizada em Services (ex.: `Objects.requireNonNull(...)` que materializa ids e evita passar valores nulos aos repositórios).
- Exceções de domínio:
  - `ResourceNotFoundException` → lançado quando um recurso não existe; mapeado para HTTP 404.
  - `IllegalArgumentException` / `NullPointerException` → mapeados para HTTP 400 (Bad Request) pelo `GlobalExceptionHandler`.
  - `GlobalExceptionHandler` também mapeia `Exception` para 500 (Internal Server Error).
- Formato de erro padrão: `ErrorResponse` (timestamp, status, error, message, path) para respostas consistentes.

---

## 7. Documentação da API

- Springdoc OpenAPI está configurado. As propriedades relevantes em `application.properties`:
  - `springdoc.api-docs.path=/api-docs`
  - `springdoc.swagger-ui.path=/swagger-ui.html`

- A UI do Swagger normalmente estará em `http://localhost:8081/swagger-ui.html` após inicializar a aplicação.

---

## 8. Como Executar o Projeto (local)

Pré-requisitos
- Java 17, Maven 3.x, PostgreSQL

Configuração do banco
- Banco padrão configurado (em `src/main/resources/application.properties`):
  - URL: `jdbc:postgresql://localhost:5432/dbtrainer`
  - User: `postgres` / Password: `postgres`
- Crie o banco localmente, por ex.: `createdb -U postgres dbtrainer`.

Executando
```bash
# rodar em modo dev
mvn -DskipTests=true spring-boot:run

# empacotar
mvn -DskipTests=true package
java -jar target/api-0.0.1-SNAPSHOT.jar
```

Configuração adicional
- Porta: `server.port=8081` (definida em `application.properties`).
- `DataInitializer` cria usuário `admin` localmente (username `admin`, senha `admin123`) se não existir.

---

## 9. Boas Práticas Adotadas

- Separação de responsabilidades (Controller/Service/Repository/DTO/Entity).
- Validação de nulidade no Service com `Objects.requireNonNull(...)` (mantém contrato do serviço explícito).
- Uso de exceções de domínio e `@RestControllerAdvice` para mapear erros para códigos HTTP apropriados.
- Senhas criptografadas com BCrypt e autenticação stateless via JWT.
- Documentação via OpenAPI/Swagger integrada.

---

## 10. Próximos Passos / Roadmap (prioridades)

Correções e consistência
- Corrigir `AlunoRepository` generic type: `Aluno` tem `@Id String cpf` mas repositório declara `JpaRepository<Aluno, Long>` — ajustar para `JpaRepository<Aluno, String>`.
- Harmonizar nomes de propriedades JWT (`jwt.expiration-ms` vs `jwt.expirationTime`) e garantir leitura consistente das configurações.
- Externalizar JWT secret para variáveis de ambiente/secret manager.

Qualidade e testes
- Adicionar testes unitários para services (validations e exceções) e testes de integração para endpoints protegidos.
- Configurar CI (build + tests).

Segurança e produção
- Rotação e armazenamento seguro de segredos; política de expiração/refresh de tokens se necessário; proteção contra brute-force no endpoint de login.

---
---
<<<<<<< HEAD

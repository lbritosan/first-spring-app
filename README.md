# First Spring App

## Visão Geral
Uma API REST simples desenvolvida com **Spring Boot** para fins de aprendizado. Oferece dois endpoints para explorar conceitos de REST, injeção de dependência, e manipulação de dados via JSON. A API é stateless, não armazena estado entre requisições, e não possui integração com banco de dados.

### Tecnologias
- **Java 21**: Linguagem de programação.
- **Spring Boot 3.5.5**: Framework para APIs REST, com Spring MVC.
- **Lombok**: Reduz boilerplate no código.
- **Jackson**: Desserializa JSON em objetos Java.
- **Maven**: Gerenciamento de dependências e build.

## Endpoints

### 1. Obter Mensagem Hello World
- **Método**: GET
- **URL**: `/hello-world`
- **Descrição**: Retorna uma mensagem "Hello World" com o nome fixo "Leonardo".
- **Exemplo de Requisição**:
  ```bash
  curl http://localhost:8080/hello-world
  ```
- **Resposta**:
  ```
  Hello World Leonardo
  ```

### 2. Criar Mensagem com Usuário
- **Método**: POST
- **URL**: `/hello-world/{id}`
- **Descrição**: Recebe um ID na URL, um parâmetro opcional `filter`, e um objeto `User` (com `name` e `email`), retornando uma mensagem concatenada.
- **Cabeçalhos**:
  - `Content-Type: application/json`
- **Parâmetros**:
  - `id` (path): Identificador na URL.
  - `filter` (query, opcional): Filtro com valor padrão "nenhum".
  - `body` (JSON): Objeto com `name` e `email`.
- **Exemplo de Requisição**:
  ```bash
  curl -X POST \
    http://localhost:8080/hello-world/123?filter=ativo \
    -H 'Content-Type: application/json' \
    -d '{"name": "Leonardo Brito", "email": "lbritosan@gmail.com"}'
  ```
- **Resposta**:
  ```
  Hello World Leonardo Brito id: 123 filter: ativo
  ```

## Como Executar
1. Clone o repositório:
   ```bash
   git clone <URL_DO_REPOSITORIO>
   ```
2. Navegue até o diretório:
   ```bash
   cd first spring-app
   ```
3. Execute com Maven:
   - Perfil `dev` (padrão, porta 8080):
     ```bash
     mvn spring-boot:run
     ```
   - Perfil `prod` (porta 3000):
     ```bash
     mvn spring-boot:run -Dspring-boot.run.profiles=prod
     ```
4. Acesse a API:
   - Perfil `dev`: `http://localhost:8080`
   - Perfil `prod`: `http://localhost:3000`

## Estrutura do Projeto
- **com.lbritosan.first_spring_app.controller**:
  - `HelloWorldController.java`: Gerencia requisições REST (GET `/hello-world`, POST `/hello-world/{id}`).
- **com.lbritosan.first_spring_app.service**:
  - `HelloWorldService.java`: Lógica de negócio para o endpoint GET.
- **com.lbritosan.first_spring_app.domain**:
  - `User.java`: DTO com `name` e `email`, usado no endpoint POST.
- **com.lbritosan.first_spring_app.configuration**:
  - `HelloConfiguration.java`: Placeholder (em branco).
- **resources**:
  - `application.properties`: Configurações gerais (nome da aplicação, porta 3000, perfil ativo).
  - `application-dev.properties`: Configurações do perfil `dev` (porta 8080).
- **pom.xml**: Define dependências (`spring-boot-starter-web`, `lombok`, `spring-boot-devtools`, `spring-boot-starter-test`) e configurações do Maven.

## Configurações
- **application.properties**:
  - `spring.application.name=first spring-app`: Nome da aplicação.
  - `server.port=3000`: Porta padrão (usada no perfil `prod`).
  - `spring.profiles.active=${ACTIVE_PROFILE:dev}`: Perfil padrão `dev`.
- **application-dev.properties**:
  - `server.port=8080`: Porta usada no perfil `dev`.

## Detalhes Técnicos
- **Arquitetura**: Padrão MVC com injeção de dependência via Spring IoC.
- **Injeção de Dependência**: O `HelloWorldService` é injetado no `HelloWorldController` via `@Autowired`.
- **Stateless**: A API não armazena estado entre requisições.
- **Dependências**:
  - `spring-boot-starter-web`: Suporta REST e MVC.
  - `lombok`: Reduz boilerplate no `User.java`.
  - `spring-boot-devtools`: Reinicialização automática em desenvolvimento.
  - `spring-boot-starter-test`: Para testes unitários/integração.

## Próximos Passos
- Adicionar validações ao `User` com Bean Validation.
- Delegar a lógica do endpoint POST ao `HelloWorldService`.
- Retornar respostas em JSON em vez de strings simples.
- Integrar com um banco de dados (ex.: H2 ou MySQL) usando Spring Data JPA.
- Adicionar documentação automática com Springdoc OpenAPI (Swagger).

## Contato
Desenvolvido por Leonardo Brito (lbritosan@gmail.com).

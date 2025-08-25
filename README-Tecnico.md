# README.md Técnico

## Visão Geral Técnica
Este documento fornece uma visão detalhada técnica do projeto "First Spring App", uma API REST simples desenvolvida com Spring Boot para fins de aprendizado. O projeto demonstra conceitos fundamentais como injeção de dependência (Dependency Injection), padrão MVC (Model-View-Controller), manipulação de requisições REST, e configurações de ambientes (perfis). A API é stateless, ou seja, não mantém estado entre requisições, e é projetada para ser escalável e fácil de manter.

O projeto consiste em:
- Dois endpoints REST: Um GET simples para "Hello World" e um POST que processa dados de um usuário.
- Camadas separadas: Controller (apresentação), Service (negócio), Domain (modelo), e Configuration (configurações).
- Suporte a perfis de ambiente (dev e prod) para diferentes configurações, como portas do servidor.

Não há integração com banco de dados ou autenticação, focando em conceitos básicos do Spring Boot.

## Tecnologias Utilizadas
- **Java 21**: Versão da linguagem de programação usada, que oferece recursos modernos como records, sealed classes, pattern matching, e melhorias de performance. Configurada no `pom.xml` via `<java.version>21</java.version>`.
- **Spring Boot 3.5.5**: Framework principal para desenvolvimento de aplicações Java, especialmente APIs REST. Fornece auto-configuração, servidor embutido (Tomcat), e gerenciamento de dependências. Herda do `spring-boot-starter-parent` no `pom.xml`.
- **Spring MVC**: Componente do Spring Boot usado para mapear requisições HTTP em métodos do controller. Suporta anotações como `@RestController`, `@GetMapping`, `@PostMapping`, `@PathVariable`, `@RequestParam`, e `@RequestBody`.
- **Lombok**: Biblioteca para reduzir código boilerplate, gerando getters, setters, e construtores automaticamente via anotações como `@Getter`, `@Setter`, e `@AllArgsConstructor`. Configurada como dependência no `pom.xml` com suporte ao processador de anotações no `maven-compiler-plugin`.
- **Jackson**: Biblioteca incluída no `spring-boot-starter-web` para serialização/desserialização de JSON. Usada para converter o corpo da requisição POST em objetos `User`.
- **Maven**: Ferramenta de build e gerenciamento de dependências. O `pom.xml` define o ciclo de vida do projeto, incluindo plugins como `spring-boot-maven-plugin` para empacotamento em JAR executável.
- **Spring DevTools**: Dependência para desenvolvimento, permitindo reinicialização automática do servidor ao alterar o código.

## Dependências (do pom.xml)
O `pom.xml` é o arquivo central do Maven, definindo o projeto como uma aplicação Spring Boot. Aqui está uma explicação técnica das principais dependências:

- **spring-boot-starter-parent (versão 3.5.5)**: Herda configurações padrão do Spring Boot, incluindo versões compatíveis de bibliotecas e plugins. Garante consistência e evita conflitos de versões.
- **spring-boot-starter-web**: Inicia o suporte a web, incluindo Spring MVC para REST, Jackson para JSON, e Tomcat como servidor embutido. Permite a criação de endpoints com anotações REST.
- **spring-boot-devtools**: Escopo `runtime`, opcional. Adiciona ferramentas de hot-reload para desenvolvimento mais rápido.
- **lombok**: Opcional, escopo `provided`. Processado em tempo de compilação para gerar código (ex.: getters no `User.java`). Excluído do JAR final via `spring-boot-maven-plugin` para evitar dependências desnecessárias em produção.
- **spring-boot-starter-test**: Escopo `test`. Inclui JUnit, Mockito, e outros para testes unitários/integração, embora não haja testes implementados no projeto atual.

Plugins:
- **maven-compiler-plugin**: Configura o compilador Java para processar anotações do Lombok.
- **spring-boot-maven-plugin**: Empacota a aplicação como um JAR executável e exclui Lombok do bundle final.

## Arquivos de Configuração
Os arquivos em `resources/` definem configurações da aplicação, usando o mecanismo de perfis do Spring Boot para ambientes diferentes.

- **application.properties**:
    - `spring.application.name=first spring-app`: Define o nome da aplicação, usado em logs e monitoramento.
    - `server.port=3000`: Porta do servidor Tomcat para o perfil padrão/prod.
    - `spring.profiles.active=${ACTIVE_PROFILE:dev}`: Ativa o perfil `dev` por padrão, ou usa a variável de ambiente `ACTIVE_PROFILE`. Isso carrega configurações adicionais de `application-dev.properties` quando o perfil `dev` está ativo.

- **application-dev.properties**:
    - `server.port=8080`: Sobrescreve a porta para `8080` no perfil `dev`, facilitando o desenvolvimento local sem conflitos com outras aplicações.

Técnica: O Spring Boot carrega propriedades hierarquicamente. O perfil ativo (`dev` ou `prod`) determina quais arquivos são priorizados. Para ativar `prod`: Use `-Dspring-boot.run.profiles=prod` ou defina a variável de ambiente.

## Análise Técnica do Código

### 1. HelloWorldController.java (Pacote: com.lbritosan.first_spring_app.controller)
Esta classe é o controller REST, responsável pela camada de apresentação. Usa Spring MVC para mapear requisições HTTP.

- **Anotações Principais**:
    - `@RestController`: Marca a classe como controlador REST, onde métodos retornam dados diretamente (texto/JSON) no corpo da resposta HTTP, sem renderização de views.
    - `@RequestMapping("/hello-world")`: Prefixo base para todos os endpoints.
    - `@Autowired`: Injeção de dependência do Spring IoC (Inversion of Control). O contêiner Spring cria e injeta uma instância de `HelloWorldService` no atributo `helloWorldService`. Isso promove desacoplamento e testabilidade.

- **Métodos**:
    - `helloWorld()` (`@GetMapping`): Mapeia GET para `/hello-world`. Chama `helloWorldService.helloWorld("Leonardo")` e retorna uma string. Técnica: Delega a lógica de negócio ao serviço, seguindo o padrão MVC.
    - `helloWorldPost(...)` (`@PostMapping("/{id}")`): Mapeia POST para `/hello-world/{id}`. Usa:
        - `@PathVariable("id")`: Extrai o parâmetro da URL (ex.: `/hello-world/123` → `id = "123"`).
        - `@RequestParam(value = "filter", defaultValue = "nenhum")`: Extrai parâmetro da query string (ex.: `?filter=ativo`), com valor padrão.
        - `@RequestBody User body`: Desserializa o JSON do corpo da requisição em um objeto `User` via Jackson. Retorna uma string concatenada usando `body.getName()`.
    - Técnica: O método processa dados de entrada e retorna uma resposta simples. Não usa o serviço para o POST, o que poderia ser melhorado para manter a separação de responsabilidades.

- **Aspectos Técnicos**: O controller segue o princípio de Single Responsibility (SRP) do SOLID, focando em manipulação de HTTP. A injeção por campo (`@Autowired` no atributo) é funcional, mas injeção por construtor é preferível para imutabilidade e testes (ex.: adicionar `final` e construtor).

### 2. User.java (Pacote: com.lbritosan.first_spring_app.domain)
Classe modelo (POJO/DTO) para representar dados do usuário no endpoint POST.

- **Anotações Lombok**:
    - `@Getter` e `@Setter`: Geram métodos `getName()`, `setName(String)`, etc., em tempo de compilação.
    - `@AllArgsConstructor`: Gera construtor `User(String name, String email)`.

- **Atributos**: `private String name;` e `private String email;`.
- **Técnica**: Usado como DTO (Data Transfer Object) para transferir dados entre cliente e servidor. Jackson mapeia nomes de campos JSON diretamente para atributos. O campo `email` não é usado no controller, sugerindo potencial para expansão (ex.: validação ou persistência).

- **Aspectos Técnicos**: Reduz boilerplate com Lombok, melhorando legibilidade. Para robustez, adicionar validações como `@NotNull` e `@Email` (do Bean Validation) e a dependência `spring-boot-starter-validation`.

### 3. HelloWorldService.java (Pacote: com.lbritosan.first_spring_app.service)
Classe de serviço para lógica de negócio.

- **Anotações**:
    - `@Service`: Marca como componente de serviço, criando um bean singleton no contêiner Spring IoC. Disponível para injeção em outros componentes.

- **Método**: `helloWorld(String name)`: Retorna `"Hello World " + name;`. Chamado pelo controller GET com argumento fixo.

- **Técnica**: Segue o padrão Service Layer, centralizando lógica de negócio. Atualmente simples (concatenação de string), but escalável para operações complexas (ex.: chamadas a bancos ou integrações).

- **Aspectos Técnicos**: Bean singleton por padrão, garantindo eficiência. Pode ser expandido para processar o `User` no endpoint POST.

### 4. HelloConfiguration.java (Pacote: com.lbritosan.first_spring_app.configuration)
- **Conteúdo**: Em branco.
- **Técnica**: Placeholder para configurações customizadas. Pode ser anotado com `@Configuration` para definir beans manualmente via `@Bean`. Exemplo: Configurar um bean para `HelloWorldService` se precisar de customização (embora `@Service` já faça isso automaticamente).
- **Aspectos Técnicos**: Útil para configurações avançadas como ObjectMapper customizado (para Jackson) ou beans condicionais por perfil.

## Arquitetura Geral
- **Padrão MVC**:
    - **Model**: `User` (dados).
    - **View**: Não aplicável (respostas REST diretas).
    - **Controller**: `HelloWorldController` (manipula HTTP).
- **Inversão de Controle (IoC)**: O Spring gerencia beans (`@Service`, `@RestController`) e injeta dependências automaticamente.
- **RESTful**: Endpoints seguem convenções REST (GET para leitura, POST para criação/processamento).
- **Perfis de Ambiente**: Suporte a múltiplos ambientes via propriedades, facilitando desenvolvimento vs. produção.
- **Escalabilidade**: Stateless, sem sessões, permitindo deploy em clusters sem sincronização de estado.

## Melhores Práticas e Sugestões Técnicas
- **Injeção de Dependência**: Prefira construtor em vez de campo para melhor testabilidade e imutabilidade.
- **Validações**: Integre Bean Validation para `User` e use `@Valid` no controller.
- **Respostas JSON**: Crie uma classe `Response` para retornar objetos JSON em vez de strings.
- **Testes**: Use `spring-boot-starter-test` para criar testes unitários (ex.: `@WebMvcTest` para controller).
- **Documentação**: Adicione Springdoc OpenAPI para gerar Swagger UI.
- **Segurança**: Para produção, adicione `spring-boot-starter-security` para autenticação.
- **Persistência**: Integre Spring Data JPA para banco de dados, adicionando `@Entity` ao `User` e um repositório.

Este documento pode ser expandido conforme o projeto evolui. Para dúvidas, contate Leonardo Brito (lbritosan@gmail.com).

## Integração com Swagger (Springdoc OpenAPI)

### Visão Geral Técnica
A API "First Spring App" foi documentada usando a especificação **OpenAPI 3.0.3**, definida no arquivo `openapi.yaml`. Esta especificação descreve os endpoints `GET /hello-world` e `POST /hello-world/{id}`, incluindo parâmetros, corpos de requisição, e respostas. Para integrar a documentação Swagger ao projeto e disponibilizar uma interface interativa (Swagger UI), usamos a biblioteca **Springdoc OpenAPI**, que gera automaticamente a documentação com base no código Java Spring e suporta a importação de arquivos YAML.

### Configuração do Springdoc OpenAPI
Para integrar o Swagger ao projeto, adicione a dependência `springdoc-openapi-starter-webmvc-ui` ao `pom.xml`:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.6.0</version>
</dependency>
```

## Integração com Swagger (Springdoc OpenAPI)

### Visão Geral Técnica
A API "First Spring App" foi documentada usando a especificação **OpenAPI 3.0.3**, definida no arquivo `openapi.yaml`. Esta especificação descreve os endpoints `GET /hello-world` e `POST /hello-world/{id}`, incluindo parâmetros, corpos de requisição, e respostas. Para integrar a documentação Swagger ao projeto e disponibilizar uma interface interativa (Swagger UI), usamos a biblioteca **Springdoc OpenAPI**, que gera automaticamente a documentação com base no código Java Spring e suporta a importação de arquivos YAML.

### Configuração do Springdoc OpenAPI
Para integrar o Swagger ao projeto, adicione a dependência `springdoc-openapi-starter-webmvc-ui` ao `pom.xml`:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.6.0</version>
</dependency>
```

- **Versão**: Use a versão mais recente compatível com Spring Boot 3.5.5 (verifique em `https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui`).
- **Função**: Essa dependência habilita o Swagger UI e gera a especificação OpenAPI automaticamente a partir das anotações do Spring MVC.

### Configuração no Projeto
1. **Incluir o openapi.yaml**:
    - Coloque o arquivo `openapi.yaml` em `src/main/resources`.
    - Configure o Springdoc para carregar o arquivo em `application.properties` (ou `application-dev.properties`):
      ```properties
      springdoc.api-docs.path=/v3/api-docs
      springdoc.swagger-ui.path=/swagger-ui.html
      springdoc.swagger-ui.config-url=/v3/api-docs/swagger-config
      springdoc.packages-to-scan=com.lbritosan.first_spring_app.controller
      springdoc.paths-to-match=/hello-world/**
      springdoc.api-docs.enabled=true
      springdoc.swagger-ui.enabled=true
      springdoc.swagger-ui.urls[0].name=First Spring App API
      springdoc.swagger-ui.urls[0].url=/openapi.yaml
      ```

2. **Acessar o Swagger UI**:
    - Após adicionar a dependência e configurar, inicie a aplicação (`mvn spring-boot:run`).
    - Acesse:
        - Perfil `dev` (porta 8080): `http://localhost:8080/swagger-ui.html`
        - Perfil `prod` (porta 3000): `http://localhost:3000/swagger-ui.html`
    - O Swagger UI exibirá os endpoints `GET /hello-world` e `POST /hello-world/{id}`, permitindo testar diretamente na interface.

3. **Customização (Opcional)**:
    - Para usar o `openapi.yaml` como base (em vez de geração automática), as propriedades acima (`springdoc.swagger-ui.urls`) garantem que o arquivo seja carregado diretamente no Swagger UI.

### Alinhamento com o Código Java Spring
A especificação `openapi.yaml` reflete o código da API implementada no projeto:

- **HelloWorldController.java**:
    - **Pacote**: `com.lbritosan.first_spring_app.controller`
    - **Anotações**:
        - `@RestController`: Define a classe como controlador REST, retornando respostas diretamente no corpo HTTP.
        - `@RequestMapping("/hello-world")`: Prefixo base dos endpoints.
        - `@Autowired`: Injeta o `HelloWorldService` via Spring IoC.
    - **Endpoints**:
        - **GET /hello-world**:
            - Mapeado com `@GetMapping`.
            - Chama `HelloWorldService.helloWorld("Leonardo")` e retorna uma string (`text/plain`): `"Hello World Leonardo"`.
            - No Swagger: Definido em `paths: /hello-world: get` com resposta `text/plain`.
        - **POST /hello-world/{id}**:
            - Mapeado com `@PostMapping("/{id}")`.
            - Recebe:
                - `@PathVariable("id") String id`: Identificador na URL (ex.: `32376579840`).
                - `@RequestParam(value = "filter", defaultValue = "nenhum") String filter`: Parâmetro opcional na query (ex.: `Leonardo`).
                - `@RequestBody User body`: Objeto JSON mapeado para a classe `User` (com `name` e `email`).
            - Retorna uma string (`text/plain`): `"Hello World <nome> id: <id> filter: <filter>"` (ex.: `"Hello World Leonardo Brito id: 32376579840 filter: Leonardo"`).
            - No Swagger: Definido em `paths: /hello-world: post` com `requestBody` referenciando `components.schemas.User` e resposta `text/plain`.

- **User.java**:
    - **Pacote**: `com.lbritosan.first_spring_app.domain`
    - Usado como DTO (Data Transfer Object) para o corpo do `POST`.
    - Campos: `name` (string) e `email` (string), mapeados via Jackson.
    - Anotações Lombok (`@Getter`, `@Setter`, `@AllArgsConstructor`) reduzem boilerplate.
    - No Swagger: Definido em `components.schemas.User` com propriedades `name` e `email`, ambos obrigatórios.

- **HelloWorldService.java**:
    - **Pacote**: `com.lbritosan.first_spring_app.service`
    - Anotado com `@Service`, registrado como bean singleton no Spring IoC.
    - Método `helloWorld(String name)`: Usado pelo endpoint `GET`, retorna `"Hello World " + name`.
    - No Swagger: Não aparece diretamente, mas suporta o endpoint `GET`.

- **HelloConfiguration.java**:
    - **Pacote**: `com.lbritosan.first_spring_app.configuration`
    - Atualmente vazio, mas pode ser usado para configurações customizadas (ex.: beans ou Jackson).
    - No Swagger: Não afeta a especificação, mas pode ser usado para customizar o Springdoc.

### Notas Técnicas
- **Injeção de Dependência**: O Spring IoC gerencia a injeção do `HelloWorldService` no `HelloWorldController` via `@Autowired`. Recomenda-se usar injeção por construtor para melhor testabilidade.
- **Serialização JSON**: O Jackson (incluído no `spring-boot-starter-web`) desserializa o corpo do `POST` em `User`. A resposta do `POST` é `text/plain`, mas pode ser alterada para `application/json` criando uma classe `Response`.
- **Perfis de Ambiente**:
    - Perfil `dev` (porta 8080): Configurado em `application-dev.properties`.
    - Perfil `prod` (porta 3000): Configurado em `application.properties`.
    - No Swagger: Refletido na seção `servers` do `openapi.yaml`.

### Solução para Comportamento JSON Inesperado
O endpoint `POST /hello-world/{id}` deveria retornar uma string (`text/plain`), mas retorna um JSON inesperado:

```json
{
  "Hello World Leonardo Brito id": {
    "32376579840 filter": "Leonardo"
  }
}
```

**Causa Possível**:
- Um filtro, interceptor, ou configuração global (ex.: `ResponseBodyAdvice`) está convertendo a `String` em JSON.
- O cliente (ex.: Postman) pode estar interpretando a resposta como JSON devido ao cabeçalho `Accept`.

**Soluções**:
1. **Forçar text/plain**:
    - Modifique o método `helloWorldPost`:
      ```java
      @PostMapping(value = "/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
      public String helloWorldPost(@PathVariable("id") String id, @RequestParam(value = "filter", defaultValue = "nenhum") String filter, @RequestBody User body) {
          return "Hello World " + body.getName() + " id: " + id + " filter: " + filter;
      }
      ```
    - Teste com `curl`:
      ```bash
      curl -X POST \
        http://localhost:8080/hello-world/32376579840?filter=Leonardo \
        -H 'Content-Type: application/json' \
        -H 'Accept: text/plain' \
        -d '{"name": "Leonardo Brito", "email": "lbritosan@gmail.com"}'
      ```

2. **Verificar Configurações**:
    - Confirme se há configurações em `application.properties`, `application-dev.properties`, or `HelloConfiguration.java` que forcem JSON.
    - Verifique se há classes com `@RestControllerAdvice` ou `HttpMessageConverter` customizado.

3. **Mudar para JSON (Opcional)**:
    - Se preferir que o endpoint retorne JSON, modifique o método para retornar um objeto:
      ```java
      @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
      public Map<String, String> helloWorldPost(@PathVariable("id") String id, @RequestParam(value = "filter", defaultValue = "nenhum") String filter, @RequestBody User body) {
          Map<String, String> response = new HashMap<>();
          response.put("message", "Hello World " + body.getName() + " id: " + id + " filter: " + filter);
          return response;
      }
      ```
    - Atualize o `openapi.yaml` para `application/json`:
      ```yaml
      responses:
        '200':
          description: Mensagem concatenada retornada como JSON
          content:
            application/json:
              schema:
                type: object
                properties:
                  message:
                    type: string
                    example: Hello World Leonardo Brito id: 32376579840 filter: Leonardo
      ```

### Próximos Passos
- Teste o Swagger UI após configurar o Springdoc.
- Considere adicionar validações ao `User` com `@NotNull` e `@Email` (dependência `spring-boot-starter-validation`).
- Para persistência, integre Spring Data JPA com um banco de dados (ex.: H2).

Para dúvidas, contate Leonardo Brito (lbritosan@gmail.com).
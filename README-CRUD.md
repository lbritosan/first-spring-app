# Documentação Técnica: Adicionar CRUD Completo para Usuários com Spring Data JPA e H2

## Visão Geral
Esta documentação detalha a implementação de um **CRUD (Create, Read, Update, Delete)** para gerenciamento de usuários na API "First Spring App", utilizando **Spring Data JPA** e o banco de dados **H2** (in-memory). A solução foi desenvolvida como parte de um projeto de aprendizado em Java e Spring, com o objetivo de consolidar conhecimentos em construção de APIs REST, persistência de dados, validações, e integração com Swagger (Springdoc OpenAPI). A funcionalidade adiciona endpoints para criar, listar, buscar, atualizar, e deletar usuários, mapeando a entidade `User` para uma tabela no banco.

### Objetivos
- Criar uma API REST com operações CRUD para a entidade `User`.
- Configurar o banco H2 in-memory para persistência.
- Adicionar validações de entrada usando Jakarta Bean Validation.
- Documentar os endpoints no Swagger UI.
- Resolver problemas técnicos, como o erro "Driver org.h2.Driver is not suitable for jdbc:h2:mem:testdb".
- Preparar o projeto para portfólio e entrevistas, demonstrando habilidades em Spring Boot.

## Tecnologias Utilizadas
- **Spring Boot**: 3.5.5 (framework para construção da API).
- **Java**: 21 (versão compatível com Spring Boot 3.5.5).
- **Spring Data JPA**: Para mapeamento objeto-relacional e acesso ao banco.
- **H2 Database**: Banco in-memory para testes e desenvolvimento.
- **Jakarta Bean Validation**: Para validações de entrada (`@NotNull`, `@Email`).
- **Springdoc OpenAPI**: Para documentação interativa no Swagger UI.
- **Lombok**: Para redução de boilerplate (getters, setters, construtores).
- **Maven**: Para gerenciamento de dependências.

## Estrutura do Projeto
A implementação segue a arquitetura em camadas do Spring:
- **Domain**: Entidade `User` com anotações JPA e validações.
- **Repository**: Interface `UserRepository` para acesso ao banco.
- **Service**: Classe `UserService` para lógica de negócio.
- **Controller**: Classe `UserController` para endpoints REST.
- **Configuration**: Configurações no `application-dev.properties` para H2 e Swagger.

## Passos de Implementação

### 1. Configuração das Dependências
As dependências necessárias foram adicionadas ao `pom.xml` para suportar JPA, H2, validações, e Swagger.

```xml
<dependencies>
    <!-- Spring Boot Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!-- Spring Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <!-- H2 Database -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    <!-- Spring Boot Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <!-- Springdoc OpenAPI -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.6.0</version>
    </dependency>
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

- **Detalhes**:
  - `spring-boot-starter-data-jpa`: Fornece Hibernate e suporte a repositórios JPA.
  - `h2`: Banco in-memory, com escopo `runtime` para execução.
  - `spring-boot-starter-validation`: Inclui Jakarta Bean Validation para `@NotNull` e `@Email`.
  - Após adicionar, executamos `mvn clean install` para atualizar o projeto.

### 2. Configuração do Banco H2
O banco H2 foi configurado como in-memory no `application-dev.properties` para facilitar testes.

```properties
# application-dev.properties
server.port=8080
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.jpa.hibernate.ddl-auto=update
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.config-url=/v3/api-docs/swagger-config
springdoc.packages-to-scan=com.lbritosan.first_spring_app.controller
springdoc.paths-to-match=/hello-world/**,/users/**
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.urls[0].name=First Spring App API
springdoc.swagger-ui.urls[0].url=/openapi.yaml
```

- **Detalhes**:
  - `spring.datasource.url`: Define o banco in-memory `testdb` com parâmetros para manter a conexão ativa.
  - `spring.datasource.password=`: Senha vazia para simplificar.
  - `spring.h2.console.enabled=true`: Habilita o console H2 em `http://localhost:8080/h2-console`.
  - `spring.jpa.hibernate.ddl-auto=update`: Cria/atualiza tabelas com base nas entidades.
  - O perfil `dev` é ativado no `application.properties`:
    ```properties
    spring.application.name=first spring-app
    spring.profiles.active=${ACTIVE_PROFILE:dev}
    ```

### 3. Resolução do Erro do H2
Durante a implementação, enfrentamos o erro **"Driver org.h2.Driver is not suitable for jdbc:h2:mem:testdb 08001/0"** ao acessar o console H2. O problema foi resolvido:
- **Causa**: Configuração incorreta da URL ou driver não carregado.
- **Solução**:
  - Atualizamos a URL para `jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`.
  - Confirmamos a dependência do H2 no `pom.xml`.
  - Limpamos o cache do Maven com `mvn dependency:purge-local-repository` e `mvn clean install`.
  - Verificamos o console H2 em `http://localhost:8080/h2-console` com:
    - **JDBC URL**: `jdbc:h2:mem:testdb`
    - **Username**: `sa`
    - **Password**: (vazio)
  - A tabela `users` foi criada automaticamente após reiniciar a aplicação.

### 4. Definição da Entidade `User`
A classe `User` foi configurada como uma entidade JPA com validações.

```java
package com.lbritosan.first_spring_app.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String name;

    @Email(message = "Email inválido")
    @NotNull(message = "Email é obrigatório")
    @Column(nullable = false)
    private String email;
}
```

- **Detalhes**:
  - `@Entity` e `@Table`: Mapeia a classe para a tabela `users`.
  - `@Id` e `@GeneratedValue`: Define `id` como chave primária auto-incrementada.
  - `@NotNull` e `@Email`: Validações para garantir que `name` e `email` sejam válidos.
  - `@Column(nullable = false)`: Impede valores nulos no banco.
  - **Lombok**: `@Getter`, `@Setter`, `@NoArgsConstructor`, e `@AllArgsConstructor` reduzem boilerplate.

- **Resolução de Problema**: As anotações `@NotNull` e `@Email` não eram reconhecidas devido à ausência da dependência `spring-boot-starter-validation`. Adicionamos a dependência e importamos `jakarta.validation.constraints`.

### 5. Criação do Repositório `UserRepository`
O repositório fornece métodos automáticos para acesso ao banco.

```java
package com.lbritosan.first_spring_app.repository;

import com.lbritosan.first_spring_app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
```

- **Detalhes**:
  - `JpaRepository<User, Long>`: Fornece métodos como `save()`, `findAll()`, `findById()`, e `deleteById()`.
  - Pacote `repository`: Separa a camada de acesso a dados.

### 6. Implementação do Serviço `UserService`
O serviço contém a lógica de negócio para o CRUD.

```java
package com.lbritosan.first_spring_app.service;

import com.lbritosan.first_spring_app.domain.User;
import com.lbritosan.first_spring_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
```

- **Detalhes**:
  - `@Service`: Registra o serviço como bean Spring.
  - `@Autowired`: Injeta o `UserRepository`.
  - Métodos: `createUser`, `getAllUsers`, `getUserById`, `updateUser`, `deleteUser`.
  - Exceção: Lança `RuntimeException` para IDs inválidos (futuramente, pode ser tratado com um `@RestControllerAdvice`).

### 7. Implementação do Controlador `UserController`
O controlador expõe os endpoints REST para o CRUD.

```java
package com.lbritosan.first_spring_app.controller;

import com.lbritosan.first_spring_app.domain.User;
import com.lbritosan.first_spring_app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        return ResponseEntity.ok(userService.updateUser(id, userDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
```

- **Detalhes**:
  - `@RestController` e `@RequestMapping("/users")`: Define o prefixo `/users`.
  - `@Valid`: Ativa validações para `@RequestBody User`.
  - `ResponseEntity`: Retorna status HTTP apropriados (200 OK, 404 Not Found, 204 No Content).
  - Endpoints:
    - `POST /users`: Cria um usuário.
    - `GET /users`: Lista todos os usuários.
    - `GET /users/{id}`: Busca um usuário por ID.
    - `PUT /users/{id}`: Atualiza um usuário.
    - `DELETE /users/{id}`: Deleta um usuário.

### 8. Documentação com Swagger
Os endpoints foram documentados no `openapi.yaml`, integrado ao Springdoc OpenAPI.

```yaml
openapi: 3.0.3
info:
  title: First Spring App API
  version: 1.0.0
  description: API para aprendizado de Java e Spring com CRUD de usuários
servers:
  - url: http://localhost:8080
    description: Servidor de desenvolvimento
  - url: http://localhost:3000
    description: Servidor de produção
paths:
  /users:
    post:
      summary: Criar um novo usuário
      description: Cria um usuário com nome e email, persistindo no banco
      operationId: createUser
      requestBody:
        description: Objeto User contendo nome e email
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/User'
            example:
              name: Leonardo Brito
              email: lbritosan@gmail.com
      responses:
        '200':
          description: Usuário criado com sucesso
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/User'
    get:
      summary: Listar todos os usuários
      description: Retorna a lista de todos os usuários cadastrados
      operationId: getAllUsers
      responses:
        '200':
          description: Lista de usuários retornada com sucesso
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/User'
  /users/{id}:
    get:
      summary: Buscar usuário por ID
      description: Retorna um usuário específico pelo ID
      operationId: getUserById
      parameters:
        - name: id
          in: path
          description: ID do usuário
          required: true
          schema:
            type: integer
            format: int64
            example: 1
      responses:
        '200':
          description: Usuário encontrado
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/User'
        '404':
          description: Usuário não encontrado
    put:
      summary: Atualizar usuário por ID
      description: Atualiza os dados de um usuário existente
      operationId: updateUser
      parameters:
        - name: id
          in: path
          description: ID do usuário
          required: true
          schema:
            type: integer
            format: int64
            example: 1
      requestBody:
        description: Objeto User com dados atualizados
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/User'
            example:
              name: Leonardo Brito Atualizado
              email: lbritosan.updated@gmail.com
      responses:
        '200':
          description: Usuário atualizado com sucesso
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/User'
        '404':
          description: Usuário não encontrado
    delete:
      summary: Deletar usuário por ID
      description: Remove um usuário do banco
      operationId: deleteUser
      parameters:
        - name: id
          in: path
          description: ID do usuário
          required: true
          schema:
            type: integer
            format: int64
            example: 1
      responses:
        '204':
          description: Usuário deletado com sucesso
        '404':
          description: Usuário não encontrado
components:
  schemas:
    User:
      type: object
      required:
        - name
        - email
      properties:
        id:
          type: integer
          format: int64
          example: 1
        name:
          type: string
          example: Leonardo Brito
        email:
          type: string
          format: email
          example: lbritosan@gmail.com
```

- **Detalhes**:
  - Endpoints `/users` e `/users/{id}` foram adicionados ao `openapi.yaml`.
  - O arquivo está em `src/main/resources/openapi.yaml`.
  - O Springdoc carrega a documentação automaticamente no Swagger UI (`http://localhost:8080/swagger-ui.html`).

### 9. Testes Realizados
Os endpoints foram testados com `curl`, console H2, e Swagger UI:
- **Criar usuário**:
  ```bash
  curl -X POST \
    http://localhost:8080/users \
    -H 'Content-Type: application/json' \
    -d '{"name": "Leonardo Brito", "email": "lbritosan@gmail.com"}'
  ```
  - Resposta: `{"id": 1, "name": "Leonardo Brito", "email": "lbritosan@gmail.com"}`
- **Listar usuários**:
  ```bash
  curl http://localhost:8080/users
  ```
  - Resposta: `[{"id": 1, "name": "Leonardo Brito", "email": "lbritosan@gmail.com"}]`
- **Buscar usuário**:
  ```bash
  curl http://localhost:8080/users/1
  ```
  - Resposta: `{"id": 1, "name": "Leonardo Brito", "email": "lbritosan@gmail.com"}`
- **Atualizar usuário**:
  ```bash
  curl -X PUT \
    http://localhost:8080/users/1 \
    -H 'Content-Type: application/json' \
    -d '{"name": "Leonardo Brito Atualizado", "email": "lbritosan.updated@gmail.com"}'
  ```
  - Resposta: `{"id": 1, "name": "Leonardo Brito Atualizado", "email": "lbritosan.updated@gmail.com"}`
- **Deletar usuário**:
  ```bash
  curl -X DELETE http://localhost:8080/users/1
  ```
  - Resposta: Status 204 No Content
- **Console H2**: A tabela `users` foi verificada em `http://localhost:8080/h2-console` com `SELECT * FROM users;`.
- **Swagger UI**: Os endpoints foram testados em `http://localhost:8080/swagger-ui.html`.

### 10. Integração com Endpoints Existentes
Os endpoints originais (`GET /hello-world` e `POST /hello-world/{id}`) foram mantidos, e o `POST /hello-world/{id}` foi corrigido para retornar `text/plain`:
```java
@PostMapping(value = "/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
public String helloWorldPost(@PathVariable("id") String id,
                            @RequestParam(value = "filter", defaultValue = "nenhum") String filter,
                            @Valid @RequestBody User body) {
    return "Hello World " + body.getName() + " id: " + id + " filter: " + filter;
}
```

### Aprendizados
- **Spring Data JPA**: Uso de `@Entity`, `@Id`, `@GeneratedValue`, e `JpaRepository` para persistência.
- **H2 Database**: Configuração de banco in-memory e uso do console H2 para inspeção.
- **Jakarta Bean Validation**: Implementação de `@NotNull` e `@Email` para validações.
- **Spring Boot**: Arquitetura em camadas (controller, service, repository).
- **Swagger**: Documentação de APIs com OpenAPI 3.0.3.
- **Depuração**: Resolução de erros como dependências ausentes e configuração do H2.

### Próximos Passos
- Resolver o erro 404 no Swagger UI (issue aberta no GitHub).
- Adicionar tratamento de exceções com `@RestControllerAdvice`.
- Implementar testes unitários com JUnit e `@WebMvcTest`.
- Adicionar autenticação com Spring Security.
- Fazer deploy da API no Heroku ou Render.

### Contato
Para dúvidas ou sugestões, contate Leonardo Brito (lbritosan@gmail.com).
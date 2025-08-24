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

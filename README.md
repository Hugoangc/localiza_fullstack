# API de Gerenciamento de Frotas (Localiza Backend)

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk)
![Spring](https://img.shields.io/badge/Spring%20Boot-3.x.x-6DB33F?style=for-the-badge&logo=spring)
![Security](https://img.shields.io/badge/Spring%20Security-JWT-6DB33F?style=for-the-badge)
![JPA](https://img.shields.io/badge/Spring%20Data%20JPA-Hibernate-6DB33F?style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql)
![Swagger](https://img.shields.io/badge/Swagger-SpringDoc-85EA2D?style=for-the-badge&logo=swagger)

Este é o repositório do backend para a aplicação de gerenciamento de frotas. É uma API RESTful completa construída com Spring Boot, projetada para ser consumida por um frontend (Angular). O projeto implementa um fluxo de e-commerce completo, desde a autenticação de usuário até o checkout e criação de pedidos.

##  Funcionalidades Principais

O projeto é dividido em quatro módulos principais:

### 1. Módulo de Autenticação e Usuário
* **Autenticação JWT:** Sistema de login (`/api/login`) que utiliza `AuthenticationManager` e retorna um token JWT.
* **Payload Customizado:** O token JWT gerado inclui um payload customizado com `id`, `username` e `role` do usuário, permitindo ao frontend identificar o usuário sem requisições adicionais.
* **Registro de Usuário:** Endpoint público (`/api/auth/register`) para criação de novas contas, com criptografia de senha (BCrypt) e atribuição da role "USER" por padrão.
* **Controle de Acesso (RBAC):** Proteção de endpoints baseada em Roles (habilitado via `@EnableMethodSecurity`).
    * Ex: Métodos de `save` e `delete` em `CarController` exigem `ADMIN` via `@PreAuthorize("hasAuthority('ADMIN')")`.
    * Endpoint para administradores alterarem a `role` de outros usuários (`/api/auth/role/{userId}`).

### 2. Módulo de E-Commerce (Carrinho & Pedidos)
* **Gerenciamento de Carrinho (`/api/cart`):**
    * Cada usuário possui um carrinho de compras persistente (`Cart`).
    * Adiciona/Remove carros e seus acessórios selecionados (`CartItem`).
    * **Lógica de Preço:** O `CartService` calcula o preço final do item, aplicando um multiplicador (`accMultiplier`) do carro sobre o preço dos acessórios.
    * Endpoints para obter o carrinho (`GET /`), adicionar item (`POST /add`), remover item (`DELETE /remove/{id}`) e limpar o carrinho (`DELETE /clear`).
* **Fluxo de Checkout (`/api/orders`):**
    * Endpoint de checkout (`POST /orders/checkout`) que orquestra todo o processo de finalização.
    * **Orquestração de Serviços:** O `OrderService` (transacional):
        1.  Valida o carrinho do usuário.
        2.  Processa o pagamento (simulado) através do `PaymentService`.
        3.  Cria um "snapshot" da compra: converte `CartItems` em `OrderItems`, salvando os nomes e preços exatos da transação.
        4.  Salva o `Order` com o status (ex: `PAID`).
        5.  Limpa o carrinho do usuário (`cartService.clearCart()`).

### 3. Módulo de Gerenciamento (CRUDs)
* **CRUDs Completos:**
    * **Carros:** (`/api/car`)
    * **Marcas:** (`/api/brand`)
    * **Acessórios:** (`/api/acessory`)
* **Exclusão Lógica (Soft Delete):** Carros não são deletados; eles são desativados via `carStatus = false` (`carService.softDelete()`).
* **Busca Avançada e Paginação:**
    * **Paginação:** Listagem de Acessórios é paginada (`/api/acessory/findAll/...`).
    * **Busca (LIKE):** Busca por nome com `ContainingIgnoreCase` para Carros, Marcas e Acessórios.
    * **Filtros:** Busca de carros por faixa de preço (`/findByPriceRange`), ano (`/findByYearGte`) e ID da marca (`/findByBrand`).

### 4. Tratamento de Erros e Validação
* **Global Exception Handler:** Um `@ControllerAdvice` (`GlobalExceptionHandler`) centraliza o tratamento de exceções para uma API semântica.
* **Validação (400):** Captura `MethodArgumentNotValidException` (para DTOs com `@NotBlank`, etc.) e retorna um JSON com os campos e mensagens de erro.
* **Recurso Não Encontrado (404):** Captura a exceção customizada `ResourceNotFoundException`.
* **Conflito (409):** Captura a exceção customizada `DuplicateEntryException` (ex: usuário já existe).

---

##  Tecnologias Utilizadas (Backend)

* **Java 17**
* **Spring Boot 3.x.x**
* **Spring Security:** Autenticação JWT e autorização por Roles (`@EnableMethodSecurity`).
* **Spring Data JPA (Hibernate):** Persistência de dados e queries customizadas.
* **MySQL:** Banco de dados relacional.
* **Jakarta Bean Validation:** Validação de DTOs de entrada (ex: `@NotBlank`).
* **Maven:** Gerenciamento de dependências.
* **Lombok:** Redução de boilerplate.
* **SpringDoc OpenAPI (Swagger):** Documentação da API.
* **io.jsonwebtoken (JJWT):** Biblioteca para criação e validação dos tokens JWT.

---

##  Contexto para o Frontend (Angular)

Este backend foi projetado para se integrar perfeitamente com um frontend Angular:

* **CORS:** Configurado globalmente (`@CrossOrigin("*")`), permitindo requisições de `localhost:4200`.
* **Autenticação:**
    1.  O frontend envia `username` e `password` para `/api/login`.
    2.  Recebe o token JWT e o armazena (ex: `localStorage`).
    3.  **O frontend pode decodificar este token** para obter imediatamente o `id`, `username` e `role` do usuário logado, **eliminando a necessidade de uma chamada extra `/me`**.
* **Interceptor HTTP:** Um `HttpInterceptorFn` deve ser usado para anexar automaticamente o token (`Authorization: Bearar ...`) a todas as requisições, exceto para `/login` e `/register`.
* **Guarda de Rotas:** O frontend deve usar `CanActivateFn` (Guards) para ler a `role` do token decodificado e proteger rotas de admin (ex: `/admin/**`).
* **Tratamento de Erros:** O frontend pode usar o `HttpErrorResponse` do interceptor para ler as mensagens de erro padronizadas enviadas pela API:
    * **Erro 400 (Validation):** Esperar um JSON `Map<String, String>` com os erros de campo.
    * **Erro 404 (Not Found):** Esperar uma mensagem de texto simples.
    * **Erro 409 (Conflict):** Esperar uma mensagem de texto simples.

---

##  Configuração e Execução

### 1. Variáveis de Ambiente
O projeto utiliza um arquivo `application.properties`.

```properties
# Configuração do Banco de Dados
spring.datasource.url=jdbc:mysql://localhost:3306/localiza
spring.datasource.username=seu-usuario-mysql
spring.datasource.password=sua-senha-mysql

# Atualiza o schema do banco ao iniciar
spring.jpa.hibernate.ddl-auto=update

# Configuração do JWT (use uma chave longa e aleatória)
jwt.secret=CHAVE_SECRETA_LONGA_E_ALEATORIA_PARA_GERAR_TOKENS_JWT
jwt.expiration-hours=1
```


```mvn spring-boot:run```

Documentação da API (Swagger)
Após iniciar a aplicação, a documentação completa e interativa do Swagger (com os endpoints publicamente liberados no SecurityConfig) pode ser acessada em:

```http://localhost:8080/swagger-ui/index.html```

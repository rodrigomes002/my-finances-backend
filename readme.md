# My Finances Backend

## 🚀 Configuração do Ambiente

### Pré-requisitos
- Docker instalado
- Docker Compose instalado

### Passo a Passo

#### 1. Configurar variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:

```env
DB_NAME=myfinancesdb
DB_PASSWORD=sua_senha_segura
```

#### 2. Subir o container PostgreSQL

Execute o comando para subir o banco de dados com docker-compose:

```bash
docker-compose up -d
```

**O que acontece:**
- ✅ A imagem `postgres:15-alpine` é baixada (se não estiver presente)
- ✅ Um volume de dados persistente é criado (`postgres_data`)
- ✅ O container `myfinances-db` é iniciado na porta `5432`
- ✅ O container está pronto para receber conexões

#### 3. Verificar o status do container

```bash
docker ps --filter name=myfinances-db
```

**Saída esperada:**
```
CONTAINER ID   IMAGE                COMMAND                  CREATED         STATUS                    PORTS                    NAMES
c1b52b77a48a   postgres:15-alpine   "docker-entrypoint.s…"   5 seconds ago   Up 4 seconds (healthy)    0.0.0.0:5432->5432/tcp   myfinances-db
```

### 📝 Configuração do docker-compose.yml

O arquivo `docker-compose.yml` foi configurado com:
- **Imagem:** `postgres:15-alpine` (versão leve do PostgreSQL 15)
- **Porta:** `5432` (padrão do PostgreSQL)
- **Variáveis de ambiente:** User, Password e Database carregados do arquivo `.env`
- **Volume:** `postgres_data` para persistência de dados
- **Healthcheck:** Verifica se o banco está saudável a cada 10 segundos

### 🔧 Comandos úteis

```bash
# Ver logs do container
docker logs -f myfinances-db

# Conectar ao banco via psql
docker exec -it myfinances-db psql -U postgres -d myfinancesdb

# Parar o container
docker-compose down

# Parar e remover volumes (cuidado!)
docker-compose down -v

# Reiniciar o container
docker-compose restart
```

### 📊 Conectar ao banco de dados

**Credenciais:**
- **Host:** localhost
- **Porta:** 5432
- **User:** postgres
- **Password:** valor de `DB_PASSWORD` no `.env`
- **Database:** valor de `DB_NAME` no `.env`

### 📁 Estrutura do projeto

```
my-finances-backend/
├── docker-compose.yml  # Orquestração do container PostgreSQL
├── pom.xml             # Dependências Maven
├── .env                # Variáveis de ambiente (não commitar)
├── readme.md           # Este arquivo
└── src/
    └── main/
        ├── java/com/myfinances/
        │   ├── MyFinancesApplication.java    # Classe principal
        │   ├── controller/                   # Controllers REST
        │   ├── service/                      # Serviços de negócio
        │   ├── repository/                   # Acesso a dados (JPA)
        │   ├── entity/                       # Entidades JPA
        │   ├── dto/                          # Data Transfer Objects
        │   ├── config/                       # Configurações (Security, JWT)
        │   └── exception/                    # Tratamento de exceções
        └── resources/
            └── application.properties        # Configuração da aplicação

---

## 🚀 API REST com Spring Boot

### Pré-requisitos

- Java 21+
- Maven 3.8+
- Docker e Docker Compose (para o banco PostgreSQL)

### Compilar a aplicação

```bash
mvn clean install
```

### Rodar a aplicação

**Opção 1: Via Maven**
```bash
mvn spring-boot:run
```

**Opção 2: Executar o JAR**
```bash
mvn clean package
java -jar target/my-finances-backend-1.0.0.jar
```

**Opção 3: Com Docker**
```bash
docker build -t myfinances-api .
docker run -p 8080:8080 --env-file .env myfinances-api
```

### Acessar a aplicação

- **URL Base:** `http://localhost:8080/api`
- **Swagger UI:** `http://localhost:8080/api/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8080/api/v3/api-docs`

### Endpoints da API

#### Usuários (Users)

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| GET | `/users` | Listar todos os usuários | ADMIN |
| GET | `/users/{id}` | Obter usuário por ID | USER |
| GET | `/users/email/{email}` | Buscar usuário por email | USER |
| POST | `/users` | Criar novo usuário | Pública |
| PUT | `/users/{id}` | Atualizar usuário | USER |
| DELETE | `/users/{id}` | Deletar usuário | ADMIN |

**Exemplo de requisição (criar usuário):**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@example.com",
    "password": "senha123"
  }'
```

### Configuração de Security

⚠️ **Spring Security foi removido** - Todos os endpoints estão públicos e acessíveis sem autenticação.

Para adicionar autenticação e autorização no futuro:
- Adicione a dependência `spring-boot-starter-security` ao `pom.xml`
- Configure as classes de segurança (`SecurityConfig`, `JwtAuthenticationFilter`)
- Adicione as anotações `@PreAuthorize` aos endpoints

### 📚 Documentação da API com Swagger/OpenAPI

A API está documentada automaticamente com Swagger/OpenAPI 3.0.

**Acessar a documentação:**
- **Swagger UI:** `http://localhost:8080/api/swagger-ui.html`
- **OpenAPI Spec (JSON):** `http://localhost:8080/api/v3/api-docs`
- **OpenAPI Spec (YAML):** `http://localhost:8080/api/v3/api-docs.yaml`

**Recursos do Swagger UI:**
- ✅ Visualização interativa de todos os endpoints
- ✅ Esquemas de request/response
- ✅ Códigos de resposta HTTP documentados
- ✅ Possibilidade de testar os endpoints diretamente pela UI
- ✅ Suporte a autenticação Bearer Token

**Como testar um endpoint no Swagger:**

1. Abra `http://localhost:8080/api/swagger-ui.html`
2. Clique em um endpoint (ex: POST /users)
3. Clique em "Try it out"
4. Preencha os dados solicitados
5. Clique em "Execute" para fazer a requisição

**Nota:** A autenticação foi removida da aplicação. Todos os endpoints estão públicos.

### Banco de Dados

#### Estrutura de Tabelas

**Tabela: users**
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

A tabela é criada automaticamente pelo Hibernate (configurado no `application.properties`)

### 🔧 Configuração da Aplicação

Arquivo: `src/main/resources/application.properties`

**Variáveis principais:**
- `spring.datasource.url` - URL do PostgreSQL
- `spring.jpa.hibernate.ddl-auto` - Estratégia de atualização do schema
- `server.port` - Porta da aplicação (padrão: 8080)
- `jwt.secret` - Chave para assinar JWTs
- `jwt.expiration` - Tempo de expiração do token (em ms)

### 📊 Conectar ao banco de dados

**Credenciais:**
- **Host:** localhost
- **Porta:** 5432
- **User:** postgres
- **Password:** valor de `DB_PASSWORD` no `.env`
- **Database:** valor de `DB_NAME` no `.env`

---

**Status:** ✅ API REST com Spring Boot, JPA e Spring Security configurada e pronta para desenvolvimento
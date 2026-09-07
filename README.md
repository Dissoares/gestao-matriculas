# Gestão de Matrizes

Sistema acadêmico de gestão de matrizes curriculares e matrícula de alunos em aulas.

---

## Stack

| Front      | Angular 20 / Nx 23 / PrimeNG 20/ RxJS              |
| Back       | Quarkus 3.33 / Java 21 / JPA / Flyway            |
| Banco      | PostgreSQL 16                                    |
| Segurança  | Keycloak 26 (OIDC · realm roles)                 |
| Node.js    | 20.20.0                                          |
| TypeScript | 5.8.3                                            |
---

## Pré-requisitos

- [Docker](https://www.docker.com/) e Docker Compose instalados
- Portas `5432`, `8080`, `8180` e `4200` disponíveis

---

## Executar o comando no diretório principal: gestao-matrizes
```bash
docker compose up --build 
```

Aguardar todos os containers inicializarem, depois acessar: http://localhost:4200.

## URLs de acesso

| Front      | http://localhost:4200                |
| Back       | http://localhost:8080                |
| Swagger UI     | http://localhost:8080/q/swagger-ui   |
| Keycloak Admin | http://localhost:8180                |

---

## Usuários de teste

| Perfil       | Usuário                                        | Senha      |
|--------------|------------------------------------------------|------------|
| Coordenador  | `coordenador1`, `coordenador2`, `coordenador3` | `senha123` |
| Aluno        | `aluno1`, `aluno2`, `aluno3`, `aluno4`, `aluno5` | `senha123` |
| Admin keycloak | `keycloakadmin`                           | `acess0x789xyz` |

---

## Testando via Swagger UI

1. Acesse http://localhost:8080/q/swagger-ui
2. Clique em **Authorize** 
3. Preencha os campos, com os dados abaixo:

| Campo       | dados                  |
|-------------|------------------------|
| `username`  | `coordenador1`         |
| `password`  | `senha123`             |
| `client_id` | `get-matriculas-front` |

4. Clique em **Authorize, depois Close**
5. Expanda qualquer endpoint → **Try it out → Execute**

> O token expira em 5 minutos.

---

## Rodando os testes unitários do backend

```bash
cd back
./mvnw test        
mvnw.cmd test     
```

Os testes cobrem as regras de negócio críticas:

- `MatrizCurricularServiceTest` 
impede oferta da mesma disciplina no mesmo horário, valida edição e exclusão

- `MatriculaServiceTest` 
valida curso autorizado, vagas esgotadas, choque de horário e matrícula bem-sucedida

---

## Arquitetura

```
gestao-matrizes/
├── back/              # API REST (Quarkus)
│   ├── controller/    # Endpoints JAX-RS
│   ├── service/       # Regras de negócio
│   ├── repository/    # Panache + JPQL customizado
│   ├── entity/        # Entidades JPA
│   ├── dto/           # Records de request/response
│   ├── enums/         # DiaSemanaEnum, PeriodoEnum
│   └── exception/     # ExcecaoNegocio + mapper global
├── front/             # SPA Angular (Nx monorepo)
│   ├── apps/webApp/   # Aplicação principal
│   └── libs/shared/   # Models, services, guards, enums
├── infra/             # Realm Keycloak + init SQL
└── docker-compose.yml
```

---

## Endpoints da API

Documentação completa no Swagger UI.

### Coordenador — `/api/matrizes` · `role: coordenador`

| Método | Path                        | Descrição                              |
|--------|-----------------------------|----------------------------------------|
| POST   | `/api/matrizes`             | Criar matriz curricular                |
| GET    | `/api/matrizes`             | Listar e filtrar matrizes do coordenador |
| GET    | `/api/matrizes/referencias` | Dados pré-cadastrados do formulário    |
| GET    | `/api/matrizes/{id}`        | Detalhar uma matriz curricular         |
| PUT    | `/api/matrizes/{id}`        | Editar professor, horário e cursos     |
| DELETE | `/api/matrizes/{id}`        | Exclusão lógica (bloqueada se há matriculados) |

### Aluno — `/api/aluno` · `role: aluno`

| Método | Path                           | Descrição                          |
|--------|--------------------------------|------------------------------------|
| GET    | `/api/aluno/aulas-disponiveis` | Aulas disponíveis para o curso     |
| GET    | `/api/aluno/matriculas`        | Minhas matrículas                  |
| POST   | `/api/aluno/matriculas/{id}`   | Realizar matrícula em uma aula     |

### Geral

| Método | Path      | Descrição              |
|--------|-----------|------------------------|
| GET    | `/cursos` | Listar todos os cursos |

---

## Decisões técnicas

**Controle de concorrência na matrícula (Pessimistic Locking):** ao iniciar uma matrícula, o sistema executa `SELECT ... FOR UPDATE` na linha da aula no banco (`LockModeType.PESSIMISTIC_WRITE` via JPA, no método `buscarPorIdParaAtualizacao`). Esse lock exclusivo é adquirido antes de qualquer validação. As requisições concorrentes ficam bloqueadas pelo próprio banco até a liberação do lock no final da transação. O que garante que a verificação e inserção da matrícula. Isso garante que a verificação de disponibilidade e a inserção da matrícula sejam executadas uma por vez. 2 alunos disputando a última vaga ao mesmo tempo nunca passam pela validação ao mesmo tempo.

**Exclusão lógica:** aulas excluídas são inativadas (ativo = false) e deixam de ser retornadas pelas consultas. Assim o histórico de matrículas é preservado.

**Flyway Migration com seed e migrations separados:** os dados iniciais como: disciplinas, professores, horários, cursos, alunos e coordenadores ficam em uma migration própria (V0010) separada das migrations de estrutura. Isso mantém a evolução do schema organizada e torna a carga inicial reproduzível.

**Nx monorepo no frontend:** código compartilhado, como models, services, guards e enums, fica em libs/shared e é consumido com o alias @front/shared/*, evita duplicação e facilita a reutilização entre os módulos e aplicações.

**CORS configurável por ambiente:** a origem permitida pelo CORS é definida pela variável QUARKUS_HTTP_CORS_ORIGINS no docker-compose.yml, permitindo sua configuração por ambiente sem alterar o código ou reconstruir a imagem.

**Configuração dos ambientes no back:** O Quarkus inicia com o application.properties como configuração base e usa o application-dev.properties no ambiente de desenvolvimento. As configurações específicas do ambiente, como URL do banco, URL do Keycloak e credenciais, usam ${VAR:default} no desenvolvimento e, em produção, são injetadas por variáveis de ambiente no docker-compose. Assim, os dados de conexão não ficam expostos no application.properties.

**Configuração por ambiente front:** criação dos arquivos de environment para cada ambiente (environment.ts para dev, environment.hml.ts para homologação e environment.prod.ts para produção). As URLs da API e do Keycloak são configuradas nesses arquivos, garantindo que cada build aponte para os endereços corretos de acordo com o ambiente.

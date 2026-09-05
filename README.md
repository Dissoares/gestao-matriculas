# Gestão de Matrículas

Sistema acadêmico de gestão de matrizes curriculares e matrícula de alunos em aulas, com controle de acesso por perfil e consistência transacional de vagas.

---

## Stack

| Camada     | Tecnologia                                      |
|------------|-------------------------------------------------|
| Backend    | Java 21 · Quarkus 3.33 · JPA/Panache · Flyway  |
| Banco      | PostgreSQL 16                                   |
| Segurança  | Keycloak 26 (OIDC · realm roles)                |
| Frontend   | Angular 18 · Nx · PrimeNG · RxJS               |
| Container  | Docker · Docker Compose                         |

---

## Pré-requisitos

- [Docker](https://www.docker.com/) e Docker Compose instalados
- Portas `5432`, `8080`, `8180` e `4200` disponíveis

---

## Como executar

```bash
docker-compose up --build
```

Aguarde **~90 segundos** para o Keycloak inicializar completamente antes de acessar o frontend.

---

## URLs de acesso

| Serviço        | URL                                  |
|----------------|--------------------------------------|
| Frontend       | http://localhost:4200                |
| Backend        | http://localhost:8080                |
| Swagger UI     | http://localhost:8080/q/swagger-ui   |
| Keycloak Admin | http://localhost:8180                |

---

## Usuários de teste

| Perfil       | Usuários                                     | Senha      |
|--------------|----------------------------------------------|------------|
| Coordenador  | `coordenador1`, `coordenador2`, `coordenador3` | `senha123` |
| Aluno        | `aluno1`, `aluno2`, `aluno3`, `aluno4`, `aluno5` | `senha123` |
| Admin KC     | `keycloakadmin`                              | `acess0x789xyz` |

Cada aluno já está vinculado a um curso no banco de dados (seed). As aulas disponíveis exibidas no frontend correspondem apenas ao curso do aluno autenticado.

---

## Testando via Swagger UI

1. Acesse http://localhost:8080/q/swagger-ui
2. Clique em **Authorize** (cadeado no topo)
3. Preencha os campos:

| Campo       | Valor                  |
|-------------|------------------------|
| `username`  | `coordenador1`         |
| `password`  | `senha123`             |
| `client_id` | `get-matriculas-front` |

4. Clique em **Authorize → Close**
5. Expanda qualquer endpoint → **Try it out → Execute**

> O token expira em 5 minutos. Se receber `401`, clique em **Authorize** novamente.

---

## Rodando os testes unitários do backend

```bash
cd back
./mvnw test
```

Os testes cobrem as regras de negócio críticas:

- `MatrizCurricularServiceTest` — impede oferta da mesma disciplina no mesmo horário
- `MatriculaServiceTest` — valida curso autorizado, vagas esgotadas e matrícula bem-sucedida

---

## Arquitetura

```
gestao-matriculas/
├── back/          # API REST (Quarkus)
│   ├── controller/    # Endpoints JAX-RS
│   ├── service/       # Regras de negócio
│   ├── repository/    # Panache + JPQL customizado
│   ├── entity/        # Entidades JPA
│   ├── dto/           # Records de request/response
│   ├── enums/         # DiaSemanaEnum, PeriodoEnum
│   └── exception/     # ExcecaoNegocio + mapper global
├── front/         # SPA Angular (Nx monorepo)
│   ├── apps/webApp/   # Aplicação principal
│   └── libs/shared/   # Models, services, guards, enums
├── infra/             # Realm Keycloak + init SQL
└── docker-compose.yml
```

---

## Endpoints da API

A documentação completa (payloads, erros, códigos HTTP) está disponível no Swagger UI. Resumo:

### Coordenador — `/api/matrizes` · `role: coordenador`

| Método | Path                    | Descrição                              |
|--------|-------------------------|----------------------------------------|
| POST   | `/api/matrizes`         | Criar aula da matriz curricular        |
| GET    | `/api/matrizes`         | Listar e filtrar aulas do coordenador  |
| GET    | `/api/matrizes/referencias` | Dados pré-cadastrados do formulário |
| GET    | `/api/matrizes/{id}`    | Detalhar uma aula                      |
| PUT    | `/api/matrizes/{id}`    | Editar professor, horário e cursos     |
| DELETE | `/api/matrizes/{id}`    | Exclusão lógica (sem matrículas)       |

### Aluno — `/api/aluno` · `role: aluno`

| Método | Path                             | Descrição                          |
|--------|----------------------------------|------------------------------------|
| GET    | `/api/aluno/aulas-disponiveis`   | Aulas disponíveis para o curso     |
| GET    | `/api/aluno/matriculas`          | Minhas matrículas                  |
| POST   | `/api/aluno/matriculas/{id}`     | Realizar matrícula em uma aula     |

### Geral

| Método | Path      | Descrição          |
|--------|-----------|--------------------|
| GET    | `/cursos` | Listar todos os cursos |

---

## Regras de negócio implementadas

- Uma disciplina pode ser ofertada em horários distintos, nunca no mesmo horário
- Coordenador acessa apenas as matrizes que criou
- Aluno só vê aulas autorizadas para o seu curso
- Aluno só vê e altera suas próprias matrículas
- Vagas controladas com **pessimistic write lock** — dois alunos não ocupam a mesma vaga simultaneamente
- Conflito de horário considera dia da semana, hora de início e hora de fim
- Edição de cursos autorizados não remove alunos já matriculados
- Exclusão lógica bloqueada se existirem matrículas ativas

---

## Decisões técnicas

**Pessimistic locking na matrícula:** a operação de matrícula faz `SELECT ... FOR UPDATE` na aula antes de validar vagas, serializando requisições concorrentes para a mesma aula e eliminando race conditions sem necessidade de retry.

**Exclusão lógica:** aulas excluídas recebem `ativo = false` e são filtradas em todas as queries, preservando o histórico de matrículas.

**Flyway com seed separado:** dados fixos (disciplinas, professores, horários, cursos, alunos, coordenadores) ficam em uma migration dedicada (`V0010`), isolados do schema, facilitando resets em desenvolvimento.

**Nx monorepo no frontend:** models, services, guards e enums ficam em `libs/shared`, reutilizáveis por qualquer app do workspace, com imports via path alias `@front/shared/*`.

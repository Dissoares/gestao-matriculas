# Gestão de Matrículas

Sistema de gestão de matriz curricular com controle de acesso por perfil (Coordenador / Aluno), autenticação via Keycloak e controle transacional de vagas.

---

## Pré-requisitos

- [Docker](https://www.docker.com/) e Docker Compose instalados

---

## Como executar

### Pré-requisito

- Docker e Docker Compose instalados

### Subir tudo

```bash
docker-compose up --build
```

Aguarde ~90 segundos para o Keycloak inicializar completamente.

---

## Acessos

| Serviço        | URL                                  |
|----------------|--------------------------------------|
| Frontend       | http://localhost:4200                |
| Backend        | http://localhost:8080                |
| Swagger UI     | http://localhost:8080/q/swagger-ui   |
| Keycloak Admin | http://localhost:8180                |

---

## Testando via Swagger UI

### 1. Acesse o Swagger

http://localhost:8080/q/swagger-ui

### 2. Autentique-se

Clique em **Authorize** (cadeado no topo da página) e preencha:

| Campo     | Valor                  |
|-----------|------------------------|
| username  | `coordenador1`         |
| password  | `senha123`             |
| client_id | `get-matriculas-front` |

Clique em **Authorize** → **Close**.

### 3. Execute os endpoints

Com o token ativo, expanda qualquer endpoint → **Try it out** → **Execute**.

> O token expira em 5 minutos. Se receber `401 Unauthorized`, clique em **Authorize** novamente e gere um novo token.

---

## Usuários de teste

**Coordenadores:** `coordenador1`, `coordenador2`, `coordenador3` — senha `senha123`

**Alunos:** `aluno1`, `aluno2`, `aluno3`, `aluno4`, `aluno5` — senha `senha123`

**Admin Keycloak:** usuário `keycloakadmin` — senha `acess0x789xyz`

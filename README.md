# Gestão de Matrículas

Sistema de gestão de matriz curricular com controle de acesso por perfil (Coordenador / Aluno), autenticação via Keycloak e controle transacional de vagas.

---

## Pré-requisitos

- [Docker](https://www.docker.com/) e Docker Compose instalados

---

## Como executar

```bash
docker-compose up --build
```

## URLs de acesso

| Frontend   | http://localhost:4200                    |
| Backend    | http://localhost:8080                    |
| Swagger UI | http://localhost:8080/q/swagger-ui       |
| OpenAPI    | http://localhost:8080/q/openapi          |
| Keycloak   | http://localhost:8180                    |

## Configuração inicial do Keycloak

Após subir os serviços, acesse o painel do Keycloak`http://localhost:8180`:

- **Usuário:** `keycloakadmin`
- **Senha:** `acess0x789xyz`



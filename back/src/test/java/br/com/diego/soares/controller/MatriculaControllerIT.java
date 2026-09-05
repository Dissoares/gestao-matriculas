package br.com.diego.soares.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
class MatriculaControllerIT {

    static final String ALUNO_1 = "a0000001-0000-0000-0000-000000000001";
    static final String COORDENADOR_1 = "c0000001-0000-0000-0000-000000000001";

    @Test
    @TestSecurity(user = ALUNO_1, roles = "aluno")
    void deveListarAulasDisponiveis() {
        given().when().get("/api/aluno/aulas-disponiveis").then().statusCode(200).body("size()", greaterThan(0));
    }

    @Test
    @TestSecurity(user = ALUNO_1, roles = "aluno")
    void deveListarMinhasMatriculas() {
        given().when().get("/api/aluno/matriculas").then().statusCode(200);
    }

    @Test
    @TestSecurity(user = ALUNO_1, roles = "aluno")
    void deveRealizarMatricula() {
        given().when().post("/api/aluno/matriculas/1").then().statusCode(201).body("disciplina.nome", equalTo("Programacao"));
    }

    @Test
    @TestSecurity(user = ALUNO_1, roles = "aluno")
    void deveImpedirMatriculaDuplicada() {
        given().when().post("/api/aluno/matriculas/2").then().statusCode(201);
        given().when().post("/api/aluno/matriculas/2").then().statusCode(400);
    }

    @Test
    @TestSecurity(user = "a0000001-0000-0000-0000-000000000005", roles = "aluno")
    void deveImpedirMatriculaEmAulaNaoAutorizadaParaOCurso() {
        given().when().post("/api/aluno/matriculas/5").then().statusCode(403);
    }

    @Test
    void deveRetornar401SemAutenticacao() {
        given().when().get("/api/aluno/aulas-disponiveis").then().statusCode(401);
    }

    @Test
    @TestSecurity(user = COORDENADOR_1, roles = "coordenador")
    void deveRetornar403ComRoleCoordenador() {
        given().when().get("/api/aluno/aulas-disponiveis").then().statusCode(403);
    }
}

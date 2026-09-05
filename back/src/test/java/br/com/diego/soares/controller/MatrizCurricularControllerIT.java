package br.com.diego.soares.controller;

import br.com.diego.soares.PostgreSQLTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
@QuarkusTestResource(PostgreSQLTestResource.class)
class MatrizCurricularControllerIT {

    static final String COORDENADOR_1 = "c0000001-0000-0000-0000-000000000001";
    static final String COORDENADOR_2 = "c0000001-0000-0000-0000-000000000002";

    @Test
    @TestSecurity(user = COORDENADOR_1, roles = "coordenador")
    void deveListarMatrizesDoCoordenador() {
        given().when().get("/api/matrizes").then().statusCode(200).body("size()", greaterThanOrEqualTo(2));
    }

    @Test
    @TestSecurity(user = COORDENADOR_1, roles = "coordenador")
    void deveBuscarMatrizPorId() {
        given().when().get("/api/matrizes/1").then().statusCode(200).body("id", equalTo(1)).body("ativo", equalTo(true));
    }

    @Test
    @TestSecurity(user = COORDENADOR_1, roles = "coordenador")
    void deveRetornar404ParaMatrizDeOutroCoordenador() {
        given().when().get("/api/matrizes/3").then().statusCode(404);
    }

    @Test
    @TestSecurity(user = COORDENADOR_1, roles = "coordenador")
    void deveCriarMatriz() {
        given().contentType(ContentType.JSON)
            .body("""
                {"disciplinaId":1,"professorId":1,"horarioId":2,"cursosAutorizadosIds":[1],"quantidadeMaximaAlunos":30}
                """)
            .when().post("/api/matrizes")
            .then().statusCode(201)
            .body("ativo", equalTo(true))
            .body("quantidadeMaximaAlunos",
            equalTo(30));
    }

    @Test
    @TestSecurity(user = COORDENADOR_2, roles = "coordenador")
    void deveExcluirMatrizSemMatriculas() {
        Integer id = given().contentType(ContentType.JSON)
            .body("""
                {"disciplinaId":2,"professorId":2,"horarioId":10,"cursosAutorizadosIds":[5],"quantidadeMaximaAlunos":20}
                """)
            .when().post("/api/matrizes")
            .then().statusCode(201)
            .extract().path("id");

        given().when().delete("/api/matrizes/" + id).then().statusCode(204);
    }

    @Test
    @TestSecurity(user = COORDENADOR_1, roles = "coordenador")
    void deveImpedirOfertaDaMesmaDisciplinaNoMesmoHorario() {
        given().contentType(ContentType.JSON)
            .body("""
                {"disciplinaId":11,"professorId":1,"horarioId":1,"cursosAutorizadosIds":[1],"quantidadeMaximaAlunos":30}
                """)
            .when().post("/api/matrizes")
            .then().statusCode(400);
    }

    @Test
    void deveRetornar401SemAutenticacao() {
        given().when().get("/api/matrizes").then().statusCode(401);
    }

    @Test
    @TestSecurity(user = "a0000001-0000-0000-0000-000000000001", roles = "aluno")
    void deveRetornar403ComRoleAluno() {
        given().when().get("/api/matrizes").then().statusCode(403);
    }
}

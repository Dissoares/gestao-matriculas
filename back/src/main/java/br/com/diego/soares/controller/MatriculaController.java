package br.com.diego.soares.controller;

import br.com.diego.soares.dto.AulaDisponivelResposta;
import br.com.diego.soares.dto.MatriculaResposta;
import br.com.diego.soares.service.MatriculaService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/aluno")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("aluno")
@SecurityRequirement(name = "keycloak")
@Tag(name = "Matrículas", description = "Operações restritas ao aluno autenticado")
public class MatriculaController {

    @Inject
    MatriculaService servico;

    @Inject
    JsonWebToken tokenJwt;

    @GET
    @Path("/aulas-disponiveis")
    @Operation(summary = "Listar aulas disponíveis para o curso do aluno")
    public List<AulaDisponivelResposta> listarAulasDisponiveis() {
        return servico.listarAulasDisponiveis(tokenJwt.getSubject());
    }

    @GET
    @Path("/matriculas")
    @Operation(summary = "Listar as próprias matrículas")
    public List<MatriculaResposta> listarMinhasMatriculas() {
        return servico.listarMinhasMatriculas(tokenJwt.getSubject());
    }

    @POST
    @Path("/matriculas/{matrizId}")
    @Operation(summary = "Realizar matrícula em uma aula")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Matrícula realizada"),
            @APIResponse(responseCode = "400", description = "Vaga, conflito ou duplicidade"),
            @APIResponse(responseCode = "403", description = "Aula não autorizada para o curso"),
            @APIResponse(responseCode = "404", description = "Aluno ou aula não encontrados")
    })
    public Response matricular(@PathParam("matrizId") Long idMatriz) {
        return Response.status(Response.Status.CREATED).entity(servico.matricular(idMatriz, tokenJwt.getSubject())).build();
    }
}

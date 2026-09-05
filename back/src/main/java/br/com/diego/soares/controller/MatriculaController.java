package br.com.diego.soares.controller;

import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import br.com.diego.soares.dto.AulaDisponivelResposta;
import io.quarkus.security.identity.SecurityIdentity;
import br.com.diego.soares.service.MatriculaService;
import br.com.diego.soares.dto.MatriculaResposta;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.GET;
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
    SecurityIdentity identity;

    @GET
    @Path("/aulas-disponiveis")
    @Operation(summary = "Listar aulas disponíveis para o curso do aluno")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Lista de aulas disponíveis para o curso do aluno"),
            @APIResponse(responseCode = "401", description = "Token ausente ou inválido"),
            @APIResponse(responseCode = "403", description = "Usuário não possui perfil aluno")
    })
    public List<AulaDisponivelResposta> listarAulasDisponiveis() {
        return servico.listarAulasDisponiveis(identity.getPrincipal().getName());
    }

    @GET
    @Path("/matriculas")
    @Operation(summary = "Listar as próprias matrículas")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Lista de matrículas do aluno autenticado"),
            @APIResponse(responseCode = "401", description = "Token ausente ou inválido"),
            @APIResponse(responseCode = "403", description = "Usuário não possui perfil aluno")
    })
    public List<MatriculaResposta> listarMinhasMatriculas() {
        return servico.listarMinhasMatriculas(identity.getPrincipal().getName());
    }

    @POST
    @Path("/matriculas/{matrizId}")
    @Operation(summary = "Realizar matrícula em uma aula")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Matrícula realizada com sucesso",
                    content = @Content(schema = @Schema(implementation = MatriculaResposta.class))),
            @APIResponse(responseCode = "400", description = "Vaga esgotada, conflito de horário ou matrícula duplicada"),
            @APIResponse(responseCode = "401", description = "Token ausente ou inválido"),
            @APIResponse(responseCode = "403", description = "Aula não autorizada para o curso do aluno"),
            @APIResponse(responseCode = "404", description = "Aluno ou aula não encontrados")
    })
    public Response matricular(@PathParam("matrizId") Long idMatriz) {
        return Response.status(Response.Status.CREATED)
                .entity(servico.matricular(idMatriz, identity.getPrincipal().getName()))
                .build();
    }
}

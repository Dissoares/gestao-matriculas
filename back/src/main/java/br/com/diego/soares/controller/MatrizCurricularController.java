package br.com.diego.soares.controller;

import br.com.diego.soares.dto.MatrizResposta;
import br.com.diego.soares.dto.ReferenciasMatrizResposta;
import br.com.diego.soares.dto.RequisicaoAtualizarMatriz;
import br.com.diego.soares.dto.RequisicaoCriarMatriz;
import br.com.diego.soares.service.MatrizCurricularService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.LocalTime;
import java.util.List;

@Path("/api/matrizes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("coordenador")
@SecurityRequirement(name = "keycloak")
@Tag(name = "Matrizes curriculares", description = "Operações restritas ao coordenador autenticado")
public class MatrizCurricularController {

    @Inject
    MatrizCurricularService servico;

    @Inject
    JsonWebToken tokenJwt;

    @POST
    @Operation(summary = "Criar uma aula da matriz curricular")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Aula criada"),
            @APIResponse(responseCode = "400", description = "Dados ou regra de negócio inválidos"),
            @APIResponse(responseCode = "401", description = "Token ausente ou inválido"),
            @APIResponse(responseCode = "403", description = "Usuário não é coordenador")
    })
    public Response criar(@Valid RequisicaoCriarMatriz requisicao) {
        return Response.status(Response.Status.CREATED).entity(servico.criar(requisicao, tokenJwt.getSubject())).build();
    }

    @GET
    @Operation(summary = "Listar e filtrar as aulas do coordenador")
    public List<MatrizResposta> listar(
            @QueryParam("horaInicio") LocalTime horaInicio,
            @QueryParam("horaFim") LocalTime horaFim,
            @QueryParam("periodo") Integer periodo,
            @QueryParam("cursoId") Long cursoId,
            @QueryParam("quantidadeMaxima") Integer quantidadeMaxima) {
        return servico.listar(tokenJwt.getSubject(), horaInicio, horaFim, periodo, cursoId, quantidadeMaxima);
    }

    @GET
    @Path("/referencias")
    @Operation(summary = "Listar referências pré-cadastradas do formulário")
    public ReferenciasMatrizResposta listarReferencias() {
        return servico.listarReferencias();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Consultar uma aula da própria matriz")
    @APIResponse(responseCode = "404", description = "Aula não encontrada ou sem acesso")
    public MatrizResposta buscarPorId(@PathParam("id") Long idMatriz) {
        return servico.buscarPorId(idMatriz, tokenJwt.getSubject());
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Editar professor, horário e cursos autorizados de uma aula")
    public MatrizResposta atualizar(@PathParam("id") Long idMatriz, @Valid RequisicaoAtualizarMatriz requisicao) {
        return servico.atualizar(requisicao, idMatriz, tokenJwt.getSubject());
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Excluir logicamente uma aula sem matrículas")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Aula desativada"),
            @APIResponse(responseCode = "400", description = "Existem alunos matriculados"),
            @APIResponse(responseCode = "404", description = "Aula não encontrada ou sem acesso")
    })
    public Response excluir(@PathParam("id") Long idMatriz) {
        servico.excluir(idMatriz, tokenJwt.getSubject());
        return Response.noContent().build();
    }
}

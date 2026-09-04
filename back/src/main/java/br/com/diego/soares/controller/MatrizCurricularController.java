package br.com.diego.soares.controller;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import br.com.diego.soares.service.MatrizCurricularService;
import br.com.diego.soares.entity.MatrizCurricular;
import org.eclipse.microprofile.jwt.JsonWebToken;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

@Path("/matriz-curricular")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("coordenador")
@Tag(name = "Matriz Curricular", description = "Gestão de matrizes curriculares pelo coordenador")
public class MatrizCurricularController {

    @Inject
    MatrizCurricularService service;

    @Inject
    JsonWebToken jwt;

    @POST
    @Operation(summary = "Criar matriz curricular")
    @APIResponses({
        @APIResponse(responseCode = "201", description = "Criada com sucesso"),
        @APIResponse(responseCode = "404", description = "Entidade referenciada não encontrada"),
        @APIResponse(responseCode = "401", description = "Não autenticado"),
        @APIResponse(responseCode = "403", description = "Sem permissão")
    })
    public Response criar(MatrizCurricular dados) {
        MatrizCurricular resultado = service.criar(dados, jwt.getSubject());
        return Response.status(Response.Status.CREATED).entity(resultado).build();
    }
}

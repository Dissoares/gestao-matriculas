package br.com.diego.soares.controller;

import br.com.diego.soares.entity.Curso;
import br.com.diego.soares.service.CursoService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/cursos")
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
@Tag(name = "Cursos", description = "Listagem de cursos disponíveis")
public class CursoController {

    @Inject
    CursoService cursoService;

    @GET
    @Operation(summary = "Listar cursos", description = "Retorna todos os cursos cadastrados no sistema")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Lista de cursos retornada com sucesso"),
        @APIResponse(responseCode = "401", description = "Não autenticado")
    })
    public List<Curso> listar() {
        return cursoService.listarTudos();
    }
}

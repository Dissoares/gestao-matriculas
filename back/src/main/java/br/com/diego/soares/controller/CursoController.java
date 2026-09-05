package br.com.diego.soares.controller;

import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import br.com.diego.soares.service.CursoService;
import io.quarkus.security.Authenticated;
import br.com.diego.soares.entity.Curso;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.GET;
import java.util.List;

@Path("/cursos")
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
@SecurityRequirement(name = "keycloak")
@Tag(name = "Cursos", description = "Dados de referência disponíveis para qualquer usuário autenticado")
public class CursoController {

    @Inject
    CursoService cursoService;

    @GET
    @Operation(summary = "Listar todos os cursos")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Lista de cursos"),
            @APIResponse(responseCode = "401", description = "Token ausente ou inválido")
    })
    public List<Curso> listar() {
        return cursoService.listarTudos();
    }
}

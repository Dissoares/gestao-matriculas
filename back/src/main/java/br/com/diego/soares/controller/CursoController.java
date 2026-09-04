package br.com.diego.soares.controller;

import br.com.diego.soares.entity.Curso;
import br.com.diego.soares.service.CursoService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/cursos")
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class CursoController {
    @Inject CursoService cursoService;

    @GET
    public List<Curso> listar() {
        return cursoService.listarTudos();
    }
}

package br.com.diego.soares.resource;

import br.com.diego.soares.dto.response.CursoResponse;
import br.com.diego.soares.service.CursoService;
import io.quarkus.security.Authenticated;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.GET;
import java.util.List;

@Path("/cursos")
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class CursoResource {

    @Inject
    CursoService cursoService;

}

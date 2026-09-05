package br.com.diego.soares.exception;

import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import java.time.Instant;

@Provider
public class MapeadorGlobalExcecao implements ExceptionMapper<ExcecaoNegocio> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ExcecaoNegocio excecao) {
        return Response.status(excecao.obterStatus()).type(MediaType.APPLICATION_JSON).entity(new RespostaErro(Instant.now(), excecao.obterStatus().getStatusCode(), excecao.obterCodigo(), excecao.getMessage(), uriInfo.getPath())).build();
    }
}

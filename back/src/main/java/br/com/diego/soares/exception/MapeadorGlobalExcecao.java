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
        RespostaErro corpo = new RespostaErro(
                Instant.now(),
                excecao.getStatus().getStatusCode(),
                excecao.getCodigo(),
                excecao.getMessage(),
                uriInfo.getPath());
        return Response.status(excecao.getStatus())
                .type(MediaType.APPLICATION_JSON)
                .entity(corpo)
                .build();
    }
}

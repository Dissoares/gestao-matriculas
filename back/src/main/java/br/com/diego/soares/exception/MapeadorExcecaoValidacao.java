package br.com.diego.soares.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import java.time.Instant;

@Provider
public class MapeadorExcecaoValidacao implements ExceptionMapper<ConstraintViolationException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ConstraintViolationException excecao) {
        String mensagem = excecao.getConstraintViolations().stream()
                .map(violacao -> {
                    String caminho = violacao.getPropertyPath().toString();
                    String campo = caminho.contains(".")
                            ? caminho.substring(caminho.lastIndexOf('.') + 1)
                            : caminho;
                    return campo + ": " + violacao.getMessage();
                })
                .findFirst()
                .orElse("Dados inválidos.");

        RespostaErro corpo = new RespostaErro(Instant.now(), 400, "erro_de_validacao", mensagem, uriInfo.getPath());
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(corpo)
                .build();
    }
}

package br.com.diego.soares.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<BusinessException> {

    @Override
    public Response toResponse(BusinessException e) {
        return Response.status(Response.Status.UNPROCESSABLE_ENTITY)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse(e.getMessage()))
                .build();
    }
}

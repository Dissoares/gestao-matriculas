package br.com.diego.soares.exception;

import jakarta.ws.rs.core.Response;

public class ExcecaoNegocio extends RuntimeException {

    private final Response.Status status;
    private final String codigo;

    public ExcecaoNegocio(String mensagem) {
        this(Response.Status.BAD_REQUEST, "regra_de_negocio", mensagem);
    }

    public ExcecaoNegocio(Response.Status status, String codigo, String mensagem) {
        super(mensagem);
        this.status = status;
        this.codigo = codigo;
    }

    public Response.Status getStatus() {
        return status;
    }

    public String getCodigo() {
        return codigo;
    }
}

package br.com.diego.soares.exception;

import java.time.Instant;

public record RespostaErro(
        Instant momento,
        int status,
        String codigo,
        String mensagem,
        String caminho
) {
}

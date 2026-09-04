package br.com.diego.soares.dto.response;

import br.com.diego.soares.domain.entity.Curso;

public record CursoResponse(Long id, String nome) {

    public static CursoResponse from(Curso curso) {
        return new CursoResponse(curso);
    }
}

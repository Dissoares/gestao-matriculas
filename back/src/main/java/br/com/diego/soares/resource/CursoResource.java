package br.com.diego.soares.resource;

import br.com.diego.soares.domain.entity.Curso;

public class CursoResource {
    public static CursoResource from(Curso curso) {
        return new CursoResource(curso);
    }
}

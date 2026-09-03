package br.com.diego.soares.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PeriodoEnum {
    MANHA(1,"MANHÃ"),
    TARDE(2,"TARDE"),
    NOITE(3,"NOITE");

    private final Integer codigo;
    private final String descricao;

    PeriodoEnum(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    @JsonValue
    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    @JsonCreator
    public static PeriodoEnum fromCodigo(Integer codigo) {
        for (PeriodoEnum dia : values()) {
            if (dia.codigo.equals(codigo)) {
                return dia;
            }
        }

        throw new IllegalArgumentException(
                "Código inválido para PeriodoEnum: " + codigo
        );
    }
}

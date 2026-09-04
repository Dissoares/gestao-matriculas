package br.com.diego.soares.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
public enum DiaSemanaEnum {
    DOMINGO(1, "DOMINGO"),
    SEGUNDA(2, "SEGUNDA"),
    TERCA(3, "TERCA"),
    QUARTA(4, "QUARTA"),
    QUINTA(5, "QUINTA"),
    SEXTA(6, "SEXTA"),
    SABADO(7, "SABADO");

    private final Integer codigo;
    private final String descricao;

    DiaSemanaEnum(Integer codigo, String descricao) {
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
    public static DiaSemanaEnum fromCodigo(Integer codigo) {
        for (DiaSemanaEnum dia : values()) {
            if (dia.codigo.equals(codigo)) {
                return dia;
            }
        }

        throw new IllegalArgumentException(
                "Código inválido para DiaSemanaEnum: " + codigo
        );
    }
}

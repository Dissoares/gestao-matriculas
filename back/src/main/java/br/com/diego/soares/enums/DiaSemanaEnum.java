package br.com.diego.soares.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
public enum DiaSemanaEnum {
    DOMINGO(1, "DOMINGO"),
    SEGUNDA(2, "SEGUNDA"),
    TERCA(3, "TERCA"),
    QUARTA(4, "QUARTA"),
    QUINTA(5, "QUINTA"),
    SEXTA(6, "SEXTA"),
    SABADO(7, "SABADO");

    private static final Map<Integer, DiaSemanaEnum> MAPA = Arrays.stream(values())
            .collect(Collectors.toMap(DiaSemanaEnum::getCodigo, Function.identity()));

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
        DiaSemanaEnum resultado = MAPA.get(codigo);
        if (resultado == null) {
            throw new IllegalArgumentException("Código inválido para DiaSemanaEnum: " + codigo);
        }
        return resultado;
    }
}

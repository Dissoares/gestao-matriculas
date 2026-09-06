package br.com.diego.soares.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
public enum PeriodoEnum {
    MANHA(1, "MANHÃ"),
    TARDE(2, "TARDE"),
    NOITE(3, "NOITE");

    private static final Map<Integer, PeriodoEnum> MAPA = Arrays.stream(values())
            .collect(Collectors.toMap(PeriodoEnum::getCodigo, Function.identity()));

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

    public LocalTime[] faixaHorario() {
        return switch (this) {
            case MANHA -> new LocalTime[]{LocalTime.of(6, 0), LocalTime.of(12, 0)};
            case TARDE -> new LocalTime[]{LocalTime.of(12, 0), LocalTime.of(18, 0)};
            case NOITE -> new LocalTime[]{LocalTime.of(18, 0), LocalTime.MAX};
        };
    }

    @JsonCreator
    public static PeriodoEnum fromCodigo(Integer codigo) {
        PeriodoEnum resultado = MAPA.get(codigo);
        if (resultado == null) {
            throw new IllegalArgumentException("Código inválido para PeriodoEnum: " + codigo);
        }
        return resultado;
    }
}

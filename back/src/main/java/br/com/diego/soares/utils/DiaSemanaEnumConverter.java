package br.com.diego.soares.utils;

import br.com.diego.soares.enums.DiaSemanaEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DiaSemanaEnumConverter implements AttributeConverter<DiaSemanaEnum, Integer> {
    @Override
    public Integer convertToDatabaseColumn(DiaSemanaEnum dia) {
        return dia != null ? dia.getCodigo() : null;
    }

    @Override
    public DiaSemanaEnum convertToEntityAttribute(Integer codigo) {
        return codigo != null ? DiaSemanaEnum.fromCodigo(codigo) : null;
    }
}

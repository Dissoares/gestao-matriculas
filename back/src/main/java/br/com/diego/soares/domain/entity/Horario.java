package br.com.diego.soares.domain.entity;

import br.com.diego.soares.converter.DiaSemanaEnumConverter;
import br.com.diego.soares.domain.enums.DiaSemanaEnum;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "horario")
@Getter
@Setter
@NoArgsConstructor
public class Horario extends PanacheEntity {

    @NotNull
    @Convert(converter = DiaSemanaEnumConverter.class)
    @Column(name = "dia_semana", nullable = false)
    private DiaSemanaEnum diaSemana;

    @NotNull
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @NotNull
    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;
}

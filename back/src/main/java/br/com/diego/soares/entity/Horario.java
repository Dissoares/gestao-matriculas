package br.com.diego.soares.entity;

import br.com.diego.soares.enums.DiaSemanaEnum;
import br.com.diego.soares.utils.DiaSemanaEnumConverter;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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

package br.com.diego.soares.dto;

import br.com.diego.soares.enums.DiaSemanaEnum;
import java.time.LocalTime;

public record HorarioResposta(
        Long id,
        DiaSemanaEnum diaSemana,
        LocalTime horaInicio,
        LocalTime horaFim
) {
}

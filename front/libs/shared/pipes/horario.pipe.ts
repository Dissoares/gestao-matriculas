import { Pipe, PipeTransform } from '@angular/core';
import { DiaSemanaEnum } from '@front/shared/enums';

@Pipe({
  name: 'horario',
  standalone: true,
})
export class HorarioPipe implements PipeTransform {
  public transform(
    horario: { diaSemana: number; horaInicio: string; horaFim: string },
    formato: 'tabela' | 'select' = 'tabela',
  ): string {
    const dia = DiaSemanaEnum.buscarPorCodigo(horario.diaSemana)?.descricao ?? '';
    if (formato === 'select') {
      return `${dia} - ${horario.horaInicio} às ${horario.horaFim}`;
    }
    return `${dia} ${horario.horaInicio}–${horario.horaFim}`;
  }
}

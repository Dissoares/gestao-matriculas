import { DiaSemana } from './dia-semana.enum';

export interface Horario {
  id: number;
  diaSemana: DiaSemana;
  horaInicio: string;
  horaFim: string;
}

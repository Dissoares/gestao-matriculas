import { Disciplina } from './disciplina.interface';
import { Horario } from './horario.interface';
import { Professor } from './professor.interface';

export interface AulaDisponivel {
  id: number;
  disciplina: Disciplina;
  professor: Professor;
  horario: Horario;
  vagasDisponiveis: number;
}

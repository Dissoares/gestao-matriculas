import { Disciplina } from './disciplina.model';
import { Horario } from './horario.model';
import { Professor } from './professor.model';

export interface AulaDisponivel {
  id: number;
  disciplina: Disciplina;
  professor: Professor;
  horario: Horario;
  vagasDisponiveis: number;
}

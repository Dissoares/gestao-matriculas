import { Disciplina } from './.interface';
import { Horario } from './.interface';
import { Professor } from './.interface';

export interface AulaDisponivel {
  id: number;
  disciplina: Disciplina;
  professor: Professor;
  horario: Horario;
  vagasDisponiveis: number;
}

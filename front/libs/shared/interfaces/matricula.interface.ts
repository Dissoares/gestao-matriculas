import { Disciplina } from './.interface';
import { Horario } from './.interface';
import { Professor } from './.interface';

export interface Matricula {
  id: number;
  disciplina: Disciplina;
  professor: Professor;
  horario: Horario;
  dataMatricula: string;
}

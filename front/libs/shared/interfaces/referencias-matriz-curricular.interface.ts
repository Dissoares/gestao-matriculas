import { Curso } from './.interface';
import { Disciplina } from './.interface';
import { Horario } from './.interface';
import { Professor } from './.interface';

export interface ReferenciasMatrizCurricular {
  disciplinas: Array<Disciplina>;
  professores: Array<Professor>;
  horarios: Array<Horario>;
  cursos: Array<Curso>;
}

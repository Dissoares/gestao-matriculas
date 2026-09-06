import { Curso } from './curso.interface';
import { Disciplina } from './disciplina.interface';
import { Horario } from './horario.interface';
import { Professor } from './professor.interface';

export interface ReferenciasMatrizCurricular {
  disciplinas: Array<Disciplina>;
  professores: Array<Professor>;
  horarios: Array<Horario>;
  cursos: Array<Curso>;
}

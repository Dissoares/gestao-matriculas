import { Curso } from './curso.model';
import { Disciplina } from './disciplina.model';
import { Horario } from './horario.model';
import { Professor } from './professor.model';

export interface ReferenciasMatrizCurricular {
  disciplinas: Array<Disciplina>;
  professores: Array<Professor>;
  horarios: Array<Horario>;
  cursos: Array<Curso>;
}

import { Curso } from './curso.model';
import { Disciplina } from './disciplina.model';
import { Horario } from './horario.model';
import { Professor } from './professor.model';

export interface MatrizCurricular {
  id: number;
  disciplina: Disciplina;
  professor: Professor;
  horario: Horario;
  cursosAutorizados: Curso[];
  quantidadeMaximaAlunos: number;
  vagasOcupadas: number;
  ativo: boolean;
}

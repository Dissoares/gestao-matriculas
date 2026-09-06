import { Curso } from './curso.interface';
import { Disciplina } from './disciplina.interface';
import { Horario } from './horario.interface';
import { Professor } from './professor.interface';

export interface MatrizCurricular {
  id: number;
  disciplina: Disciplina;
  professor: Professor;
  horario: Horario;
  cursosAutorizados: Array<Curso>;
  quantidadeMaximaAlunos: number;
  vagasOcupadas: number;
  ativo: boolean;
}

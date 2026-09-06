import { Curso } from './.interface';
import { Disciplina } from './.interface';
import { Horario } from './.interface';
import { Professor } from './.interface';

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

import { Aluno } from './aluno.model';
import { MatrizCurricular } from './matriz-curricular.model';

export interface Matricula {
  id: number;
  aluno: Aluno;
  matrizCurricular: MatrizCurricular;
  dataMatricula: string;
}

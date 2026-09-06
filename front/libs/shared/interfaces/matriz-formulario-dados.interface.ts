import { ReferenciasMatrizCurricular } from './referencias-matriz-curricular.interface';
import { MatrizCurricular } from './matriz-curricular.interface';

export interface MatrizFormularioDados {
  referencias: ReferenciasMatrizCurricular;
  matriz: MatrizCurricular | null;
}

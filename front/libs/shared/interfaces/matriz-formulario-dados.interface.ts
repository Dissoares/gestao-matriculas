import { ReferenciasMatrizCurricular } from './.interface';
import { MatrizCurricular } from './.interface';

export interface MatrizFormularioDados {
  referencias: ReferenciasMatrizCurricular;
  matriz: MatrizCurricular | null;
}

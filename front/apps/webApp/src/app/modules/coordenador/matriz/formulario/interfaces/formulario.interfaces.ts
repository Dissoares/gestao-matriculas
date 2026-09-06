import { ReferenciasMatrizCurricular, MatrizCurricular } from '@front/shared/models';
import { FormControl, FormGroup } from '@angular/forms';

export interface MatrizFormularioDados {
  referencias: ReferenciasMatrizCurricular;
  matriz: MatrizCurricular | null;
}

export type MatrizForm = FormGroup<{
  disciplinaId: FormControl<number>;
  professorId: FormControl<number>;
  horarioId: FormControl<number>;
  cursosAutorizadosIds: FormControl<Array<number>>;
  quantidadeMaximaAlunos: FormControl<number>;
}>;

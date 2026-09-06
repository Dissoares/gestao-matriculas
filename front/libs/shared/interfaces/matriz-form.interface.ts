import { FormControl, FormGroup } from '@angular/forms';

export type MatrizForm = FormGroup<{
  disciplinaId: FormControl<number>;
  professorId: FormControl<number>;
  horarioId: FormControl<number>;
  cursosAutorizadosIds: FormControl<Array<number>>;
  quantidadeMaximaAlunos: FormControl<number>;
}>;

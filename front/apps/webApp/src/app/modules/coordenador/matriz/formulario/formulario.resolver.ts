import {
  ReferenciasMatrizCurricular,
  MatrizCurricular,
} from '@front/shared/models';
import { MatrizCurricularService } from '@front/shared/services';
import { ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { forkJoin, of } from 'rxjs';

export interface MatrizFormularioDados {
  referencias: ReferenciasMatrizCurricular;
  matriz: MatrizCurricular | null;
}

export const matrizFormularioResolver: ResolveFn<MatrizFormularioDados> = (
  route,
) => {
  const servicoMatriz = inject(MatrizCurricularService);
  const idDaRota = Number(route.paramMap.get('id'));
  const idMatriz = Number.isInteger(idDaRota) && idDaRota > 0 ? idDaRota : null;

  return forkJoin({
    referencias: servicoMatriz.listarReferencias(),
    matriz: idMatriz ? servicoMatriz.buscarPorId(idMatriz) : of(null),
  });
};

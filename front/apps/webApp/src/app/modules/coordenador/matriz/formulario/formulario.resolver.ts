import { MatrizFormularioDados } from '@front/shared/interfaces';
import { MatrizCurricularService } from '@front/shared/services';
import { ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { forkJoin, of } from 'rxjs';

export const matrizFormularioResolver: ResolveFn<MatrizFormularioDados> = (
  route,
) => {
  const servicoMatriz: MatrizCurricularService = inject(
    MatrizCurricularService,
  );
  const idDaRota: number = Number(route.paramMap.get('id'));
  const idMatriz: number | null =
    Number.isInteger(idDaRota) && idDaRota > 0 ? idDaRota : null;

  return forkJoin({
    referencias: servicoMatriz.listarReferencias(),
    matriz: idMatriz ? servicoMatriz.buscarPorId(idMatriz) : of(null),
  });
};

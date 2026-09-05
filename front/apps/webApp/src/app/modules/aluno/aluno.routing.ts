import { RotasEnum } from '@front/shared/enums';
import { Routes } from '@angular/router';

export const ROTAS_ALUNO: Routes = [
  {
    path: RotasEnum.ALUNO.AULAS,
    loadComponent: () =>
      import('./aulas/aulas.component').then((m) => m.AulasComponent),
  },
  {
    path: RotasEnum.ALUNO.MATRICULAS.LISTAR,
    loadComponent: () =>
      import('./matriculas/listagem/listagem.component').then(
        (m) => m.ListagemComponent,
      ),
  },
  {
    path: RotasEnum.ALUNO.MATRICULAS.NOVA,
    redirectTo: RotasEnum.ALUNO.AULAS,
    pathMatch: 'full',
  },
];

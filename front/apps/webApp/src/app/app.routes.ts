import {
  coordenadorGuard,
  alunoGuard,
} from '../../../../libs/auth/guards/index';
import { RotasEnum } from '../../../../libs/shared/enums';
import { Route } from '@angular/router';

export const appRoutes: Route[] = [
  {
    path: RotasEnum.LOGIN,
    loadComponent: () =>
      import('./login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: RotasEnum.ROTA.COORDENADOR,
    canActivate: [coordenadorGuard],
  },
  {
    path: RotasEnum.ROTA.ALUNO,
    canActivate: [alunoGuard],
  },
  {
    path: '**',
    redirectTo: RotasEnum.LOGIN,
    pathMatch: 'full',
  },
];

import {
  CoordenadorGuard,
  AlunoGuard,
} from '../../../../libs/auth/guards/index';
import { RotasEnum } from '../../../../libs/shared/enums';
import { Route } from '@angular/router';

export const appRoutes: Array<Route> = [
  {
    path: RotasEnum.LOGIN,
    loadComponent: () =>
      import('./login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: '',
    loadComponent: () =>
      import('./layout/content/content.component').then(
        (m) => m.ContentComponent,
      ),
    children: [
      {
        path: '',
        redirectTo: RotasEnum.LOGIN,
        pathMatch: 'full',
      },
      {
        path: RotasEnum.ROTA.COORDENADOR,
        canActivate: [CoordenadorGuard],
        loadChildren: () =>
          import('./coordenador/coordenador.routing').then(
            (m) => m.ROTAS_COORDENADOR,
          ),
      },
      {
        path: RotasEnum.ROTA.ALUNO,
        canActivate: [AlunoGuard],
        loadChildren: () =>
          import('./aluno/aluno.routing').then((m) => m.ROTAS_ALUNO),
      },
    ],
  },

  {
    path: '**',
    redirectTo: RotasEnum.LOGIN,
    pathMatch: 'full',
  },
];

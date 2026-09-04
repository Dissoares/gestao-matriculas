import { AlunoGuard, CoordenadorGuard } from '../../../../libs/auth/guards/index';
import { RotasEnum } from '../../../../libs/shared/enums';
import { Route } from '@angular/router';

export const appRoutes: Route[] = [
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
          import('./coordenador/coordenador.routing').then(
            (m) => m.ROTAS_COORDENADOR,
          ),
      },
    ],
  },

  {
    path: '**',
    redirectTo: RotasEnum.LOGIN,
    pathMatch: 'full',
  },
];

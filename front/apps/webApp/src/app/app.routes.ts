import { CoordenadorGuard, AlunoGuard } from '@front/auth/guards';
import { RotasEnum } from '@front/shared/enums';
import { Route } from '@angular/router';

export const appRoutes: Array<Route> = [
  {
    path: RotasEnum.LOGIN,
    loadComponent: () =>
      import('./modules/login/login.component').then((m) => m.LoginComponent),
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
          import('./modules/coordenador/coordenador.routing').then(
            (m) => m.ROTAS_COORDENADOR,
          ),
      },
      {
        path: RotasEnum.ROTA.ALUNO,
        canActivate: [AlunoGuard],
        loadChildren: () =>
          import('./modules/aluno/aluno.routing').then((m) => m.ROTAS_ALUNO),
      },
    ],
  },

  {
    path: '**',
    redirectTo: RotasEnum.LOGIN,
    pathMatch: 'full',
  },
];

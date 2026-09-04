import {
  coordenadorGuard,
  alunoGuard,
} from '../../../../libs/auth/guards/index';
import { RotasEnum } from '../../../../libs/shared/enums';
import { Route } from '@angular/router';

export const appRoutes: Route[] = [
  {
    path: '',
    redirectTo: RotasEnum.LOGIN,
    pathMatch: 'full',
  },
  {
    path: RotasEnum.LOGIN,
    loadComponent: () =>
      import('./login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: RotasEnum.ROTA.COORDENADOR,
    canActivate: [coordenadorGuard],
    children: [
      {
        path: RotasEnum.COORDENADOR.MATRIZ.LISTAR,
        loadComponent: () =>
          import('../app/coordenador/matriz/listagem/listagem.component').then(
            (m) => m.ListagemComponent,
          ),
      },
      {
        path: RotasEnum.COORDENADOR.MATRIZ.NOVA,
        loadComponent: () =>
          import(
            '../app/coordenador/matriz/formulario/formulario.component'
          ).then((m) => m.FormularioComponent),
      },
      {
        path: `${RotasEnum.COORDENADOR.MATRIZ.LISTAR}/:id/${RotasEnum.COORDENADOR.MATRIZ.EDITAR}`,
        loadComponent: () =>
          import(
            '../app/coordenador/matriz/formulario/formulario.component'
          ).then((m) => m.FormularioComponent),
      },
    ],
  },
  {
    path: RotasEnum.ROTA.ALUNO,
    canActivate: [alunoGuard],
    children: [],
  },
  {
    path: '**',
    redirectTo: RotasEnum.LOGIN,
    pathMatch: 'full',
  },
];

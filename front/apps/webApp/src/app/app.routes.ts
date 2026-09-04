import { coordenadorGuard } from '../../../../libs/auth/guards/coordenador.guard';
import { alunoGuard } from '../../../../libs/auth/guards/aluno.guard';
import { Route } from '@angular/router';

export const appRoutes: Route[] = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () =>
      import('./login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: 'coordenador',
    canActivate: [coordenadorGuard],
    children: [{ path: '', redirectTo: 'matrizes', pathMatch: 'full' }],
  },
  {
    path: 'aluno',
    canActivate: [alunoGuard],
    children: [{ path: '', redirectTo: 'disponiveis', pathMatch: 'full' }],
  },
  { path: '**', redirectTo: 'login' },
];

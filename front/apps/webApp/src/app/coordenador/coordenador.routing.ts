import { RotasEnum } from '../../../../../libs/shared/enums';
import { Routes } from '@angular/router';

export const ROTAS_COORDENADOR: Routes = [
  {
    path: RotasEnum.COORDENADOR.MATRIZ.LISTAR,
    loadComponent: () =>
      import('./matriz/listagem/listagem.component').then(
        (m) => m.ListagemComponent,
      ),
  },
  {
    path: RotasEnum.COORDENADOR.MATRIZ.NOVA,
    loadComponent: () =>
      import('./matriz/formulario/formulario.component').then(
        (m) => m.FormularioComponent,
      ),
  },
  {
    path: RotasEnum.COORDENADOR.MATRIZ.EDITAR,
    loadComponent: () =>
      import('./matriz/formulario/formulario.component').then(
        (m) => m.FormularioComponent,
      ),
  },
];

import { AutenticacaoService } from '../services/autenticacao.service';
import { CanActivateFn, Router } from '@angular/router';
import { PerfilEnum, RotasEnum } from '../enums';
import { inject } from '@angular/core';

export const CoordenadorGuard: CanActivateFn = () => {
  const autenticacaoService = inject(AutenticacaoService);
  const router = inject(Router);

  if (autenticacaoService.possuiPerfilValido(PerfilEnum.COORDENADOR)) {
    return true;
  }

  return router.createUrlTree([RotasEnum.LOGIN]);
};

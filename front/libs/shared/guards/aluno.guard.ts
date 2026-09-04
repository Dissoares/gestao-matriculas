import { AutenticacaoService } from '../services/index';
import { CanActivateFn, Router } from '@angular/router';
import { PerfilEnum, RotasEnum } from '../enums';
import { inject } from '@angular/core';

export const AlunoGuard: CanActivateFn = () => {
  const autenticacaoService = inject(AutenticacaoService);
  const router = inject(Router);

  if (autenticacaoService.possuiPerfilValido(PerfilEnum.ALUNO)) {
    return true;
  }

  return router.createUrlTree([RotasEnum.LOGIN]);
};

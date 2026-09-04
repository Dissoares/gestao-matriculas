import { AutenticacaoService } from '../../shared/services/index';
import { PerfilEnum, RotasEnum } from '../../shared/enums/index';
import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const alunoGuard: CanActivateFn = () => {
  const autenticacaoService = inject(AutenticacaoService);
  const router = inject(Router);

  if (autenticacaoService.possuiPerfilValido(PerfilEnum.ALUNO)) {
    return true;
  }

  return router.createUrlTree([RotasEnum.LOGIN]);
};

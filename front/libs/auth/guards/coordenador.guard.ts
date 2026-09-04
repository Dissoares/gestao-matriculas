import { AutenticacaoService } from '../../shared/services/autenticacao.service';
import { PerfilEnum, RotasEnum } from '../../shared/enums/index';
import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const coordenadorGuard: CanActivateFn = () => {
  const autenticacaoService = inject(AutenticacaoService);
  const router = inject(Router);

  if (autenticacaoService.possuiPerfilValido(PerfilEnum.COORDENADOR)) {
    return true;
  }

  return router.createUrlTree([RotasEnum.LOGIN]);
};

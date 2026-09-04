import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AutenticacaoService } from '../../shared/services/autenticacao.service';

export const coordenadorGuard: CanActivateFn = () => {
  const autenticacaoService = inject(AutenticacaoService);
  const router = inject(Router);

  if (autenticacaoService.possuiPerfilValido('coordenador')) {
    return true;
  }

  return router.createUrlTree(['/login']);
};

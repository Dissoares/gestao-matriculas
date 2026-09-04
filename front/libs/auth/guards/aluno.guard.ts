import { AutenticacaoService } from '../../shared/services/autenticacao.service';
import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const alunoGuard: CanActivateFn = () => {
  const autenticacaoService = inject(AutenticacaoService);
  const router = inject(Router);

  if (autenticacaoService.possuiPerfilValido('aluno')) {
    return true;
  }

  return router.createUrlTree(['/login']);
};

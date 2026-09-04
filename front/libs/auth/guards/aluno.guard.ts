import { AutenticacaoService } from '../../shared/services/autenticacao.service';
import { PerfilEnum } from '../../shared/enums/perfil.enum';
import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const alunoGuard: CanActivateFn = () => {
  const autenticacaoService = inject(AutenticacaoService);
  const router = inject(Router);

  if (autenticacaoService.possuiPerfilValido(PerfilEnum.ALUNO)) {
    return true;
  }

  return router.createUrlTree(['/login']);
};

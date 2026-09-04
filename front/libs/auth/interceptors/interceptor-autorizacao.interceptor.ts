import {
  HttpInterceptorFn,
  HttpHandlerFn,
  HttpRequest,
  HttpEvent,
} from '@angular/common/http';
import { AutenticacaoService } from '../../shared/services/autenticacao.service';
import { from, Observable, switchMap } from 'rxjs';
import { inject } from '@angular/core';

export const interceptorAutorizacao: HttpInterceptorFn = (
  requisicao: HttpRequest<unknown>,
  proximo: HttpHandlerFn,
): Observable<HttpEvent<unknown>> => {
  const autenticacaoService: AutenticacaoService = inject(AutenticacaoService);

  return from(autenticacaoService.renovarTokenAcesso()).pipe(
    switchMap(() => {
      const token: string | undefined = autenticacaoService.obterTokenAcesso();

      if (!token) {
        return proximo(requisicao);
      }

      const requisicaoAutenticada: HttpRequest<unknown> = requisicao.clone({
        setHeaders: { Authorization: `Bearer ${token}` },
      });

      return proximo(requisicaoAutenticada);
    }),
  );
};

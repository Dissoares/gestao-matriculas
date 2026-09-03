import {
  HttpEvent,
  HttpHandlerFn,
  HttpInterceptorFn,
  HttpRequest,
} from '@angular/common/http';
import { inject } from '@angular/core';
import { from, Observable, switchMap } from 'rxjs';
import { AutenticacaoService } from './autenticacao.service';

export const interceptorAutorizacao: HttpInterceptorFn = (
  requisicao: HttpRequest<unknown>,
  proximo: HttpHandlerFn,
): Observable<HttpEvent<unknown>> => {
  const autenticacaoService = inject(AutenticacaoService);

  return from(autenticacaoService.renovarToken()).pipe(
    switchMap(() => {
      const token: string | undefined = autenticacaoService.obterToken();

      if (!token) {
        return proximo(requisicao);
      }

      const requisicaoAutenticada = requisicao.clone({
        setHeaders: { Authorization: `Bearer ${token}` },
      });

      return proximo(requisicaoAutenticada);
    }),
  );
};

import {
  HttpErrorResponse,
  HttpInterceptorFn,
  HttpHandlerFn,
  HttpRequest,
} from '@angular/common/http';
import { Observable, EMPTY, catchError } from 'rxjs';
import { MessageService } from 'primeng/api';
import { inject } from '@angular/core';

export const interceptorErro: HttpInterceptorFn = (
  requisicao: HttpRequest<unknown>,
  proximo: HttpHandlerFn,
): Observable<never> => {
  const messageService: MessageService = inject(MessageService);

  return proximo(requisicao).pipe(
    catchError((erro: HttpErrorResponse) => {
      const mensagem: string = obterMensagem(erro);
      messageService.add({
        severity: 'error',
        summary: 'Erro',
        detail: mensagem,
        life: 5000,
      });
      return EMPTY;
    }),
  ) as Observable<never>;
};

function obterMensagem(erro: HttpErrorResponse): string {
  switch (erro.status) {
    case 401:
      return 'Sessão expirada. Faça login novamente.';
    case 403:
      return 'Você não tem permissão para realizar esta operação.';
    case 0:
      return 'Não foi possível conectar ao servidor.';
    default:
      return erro.error?.mensagem ?? 'Não foi possível concluir a operação.';
  }
}

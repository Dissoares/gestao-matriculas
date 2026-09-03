import {
  provideBrowserGlobalErrorListeners,
  ApplicationConfig,
  APP_INITIALIZER,
} from '@angular/core';
import { interceptorAutorizacao } from '../../../../libs/auth/interceptors/interceptor-autorizacao.interceptor';
import { AutenticacaoService } from '../../../../libs/auth/services/autenticacao.service';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { appRoutes } from './app.routes';

function inicializarAutenticacao(
  autenticacaoService: AutenticacaoService,
): () => Promise<boolean> {
  return () => autenticacaoService.inicializarKeyCloak();
}

export const configuracaoApp: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(appRoutes),
    provideHttpClient(withInterceptors([interceptorAutorizacao])),
    {
      provide: APP_INITIALIZER,
      useFactory: inicializarAutenticacao,
      deps: [AutenticacaoService],
      multi: true,
    },
  ],
};

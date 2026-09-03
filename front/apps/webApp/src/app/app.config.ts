import {
  provideBrowserGlobalErrorListeners,
  ApplicationConfig,
  APP_INITIALIZER,
} from '@angular/core';
import { interceptorAutorizacao } from './core/interceptor-autorizacao.interceptor';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { AutenticacaoService } from './core/autenticacao.service';
import { provideRouter } from '@angular/router';
import { appRoutes } from './app.routes';

function inicializarAutenticacao(
  autenticacaoService: AutenticacaoService,
): () => Promise<boolean> {
  return () => autenticacaoService.inicializar();
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

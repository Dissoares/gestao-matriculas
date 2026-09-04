import {
  provideBrowserGlobalErrorListeners,
  provideZonelessChangeDetection,
  ApplicationConfig,
  APP_INITIALIZER,
} from '@angular/core';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { interceptorAutorizacao } from '../../../../libs/auth/interceptors/index';
import { AutenticacaoService } from '../../../../libs/shared/services/index';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { providePrimeNG } from 'primeng/config';
import { appRoutes } from './app.routes';
import Aura from '@primeng/themes/aura';

function inicializarAutenticacao(
  autenticacaoService: AutenticacaoService,
): () => Promise<boolean> {
  return () => autenticacaoService.inicializarKeyCloak();
}

export const configuracaoApp: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZonelessChangeDetection(),
    provideAnimationsAsync(),
    provideRouter(appRoutes),
    provideHttpClient(withInterceptors([interceptorAutorizacao])),
    providePrimeNG({
      theme: { preset: Aura, options: { darkModeSelector: false } },
    }),
    {
      provide: APP_INITIALIZER,
      useFactory: inicializarAutenticacao,
      deps: [AutenticacaoService],
      multi: true,
    },
  ],
};

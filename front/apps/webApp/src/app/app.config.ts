import {
  provideBrowserGlobalErrorListeners,
  provideZonelessChangeDetection,
  ApplicationConfig,
  APP_INITIALIZER,
} from '@angular/core';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { interceptorAutorizacao } from '@front/auth/interceptors';
import { AutenticacaoService } from '@front/shared/services';
import { provideRouter } from '@angular/router';
import { providePrimeNG } from 'primeng/config';
import { appRoutes } from './app.routes';
import Aura from '@primeuix/themes/aura';

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
      theme: {
        preset: Aura,
      },
    }),
    {
      provide: APP_INITIALIZER,
      useFactory: inicializarAutenticacao,
      deps: [AutenticacaoService],
      multi: true,
    },
  ],
};

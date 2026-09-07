import {
  provideBrowserGlobalErrorListeners,
  provideZonelessChangeDetection,
  ApplicationConfig,
  APP_INITIALIZER,
} from '@angular/core';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import {
  interceptorAutorizacao,
  interceptorErro,
} from '@front/auth/interceptors';
import { ConfirmationService, MessageService } from 'primeng/api';
import { AutenticacaoService } from '@front/shared/services';
import { provideRouter } from '@angular/router';
import { providePrimeNG } from 'primeng/config';
import { definePreset } from '@primeuix/themes';
import { appRoutes } from './app.routes';
import Aura from '@primeuix/themes/aura';

const temaUniversidade = definePreset(Aura, {
  semantic: {
    primary: {
      50: '{indigo.50}',
      100: '{indigo.100}',
      200: '{indigo.200}',
      300: '{indigo.300}',
      400: '{indigo.400}',
      500: '{indigo.500}',
      600: '{indigo.600}',
      700: '{indigo.700}',
      800: '{indigo.800}',
      900: '{indigo.900}',
      950: '{indigo.950}',
    },
  },
});

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
    provideHttpClient(
      withInterceptors([interceptorAutorizacao, interceptorErro]),
    ),
    MessageService,
    ConfirmationService,
    providePrimeNG({
      theme: {
        preset: temaUniversidade,
        options: { darkModeSelector: false },
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

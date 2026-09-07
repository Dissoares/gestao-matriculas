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
      50: '{blue.50}',
      100: '{blue.100}',
      200: '{blue.200}',
      300: '{blue.300}',
      400: '{blue.400}',
      500: '{blue.500}',
      600: '{blue.600}',
      700: '{blue.700}',
      800: '{blue.800}',
      900: '{blue.900}',
      950: '{blue.950}',
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

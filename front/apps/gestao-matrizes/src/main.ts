import { bootstrapApplication } from '@angular/platform-browser';
import { configuracaoApp } from './app/app.config';
import { App } from './app/app';

bootstrapApplication(App, configuracaoApp).catch((erro) => console.error(erro));

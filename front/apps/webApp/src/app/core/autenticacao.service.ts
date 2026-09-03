import Keycloak, { KeycloakInitOptions } from 'keycloak-js';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AutenticacaoService {
  private readonly instanciaKeycloak: Keycloak = new Keycloak({
    url: 'http://localhost:8180',
    realm: 'get-matriculas',
    clientId: 'get-matriculas-front',
  });

  public inicializarKeyCloak(): Promise<boolean> {
    const opcoes: KeycloakInitOptions = {
      onLoad: 'login-required',
      checkLoginIframe: false,
    };
    return this.instanciaKeycloak.init(opcoes);
  }
}

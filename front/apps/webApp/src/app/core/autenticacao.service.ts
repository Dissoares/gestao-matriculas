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

  public obterTokenAutenticacao(): string | undefined {
    return this.instanciaKeycloak.token;
  }

  public renovarTokenAutenticacao(): Promise<boolean> {
    return this.instanciaKeycloak.updateToken(30);
  }

  public ehUsuarioAutenticado(): boolean {
    return !!this.instanciaKeycloak.authenticated;
  }

  public possuiPerfilValido(perfil: string): boolean {
    return this.instanciaKeycloak.hasRealmRole(perfil);
  }

  finalizarSessao(): void {
    this.instanciaKeycloak.logout({ redirectUri: window.location.origin });
  }
}

import Keycloak, { KeycloakInitOptions } from 'keycloak-js';
import { PerfilEnum } from '../enums/perfil.enum';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AutenticacaoService {
  private readonly instanciaKeycloak: Keycloak = new Keycloak({
    url: 'http://localhost:8180',
    realm: 'get-matriculas',
    clientId: 'get-matriculas-front',
  });

  public async inicializarKeyCloak(): Promise<boolean> {
    try {
      const opcoes: KeycloakInitOptions = {
        onLoad: 'login-required',
        pkceMethod: 'S256',
        checkLoginIframe: false,
      };
      return await this.instanciaKeycloak.init(opcoes);
    } catch {
      return false;
    }
  }

  public obterTokenAcesso(): string | undefined {
    return this.instanciaKeycloak.token;
  }

  public renovarTokenAcesso(): Promise<boolean> {
    return this.instanciaKeycloak.updateToken(30);
  }

  public ehUsuarioAutenticado(): boolean {
    return !!this.instanciaKeycloak.authenticated;
  }

  public possuiPerfilValido(perfil: PerfilEnum): boolean {
    return this.instanciaKeycloak.hasRealmRole(perfil.descricao);
  }

  public obterNomeUsuario(): string {
    return (
      (this.instanciaKeycloak.tokenParsed?.['preferred_username'] as string) ??
      'Usuário'
    );
  }

  public autenticarUsuario(): void {
    this.instanciaKeycloak.login();
  }

  public finalizarSessao(): void {
    this.instanciaKeycloak.logout({ redirectUri: window.location.origin });
  }
}

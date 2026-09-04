import { AutenticacaoService } from '../../../../../libs/shared/services/index';
import { PerfilEnum, RotasEnum } from '../../../../../libs/shared/enums/index';
import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [],
  templateUrl: './login.component.html',
})
export class LoginComponent implements OnInit {
  private readonly autenticacaoService = inject(AutenticacaoService);
  private readonly router = inject(Router);

  public carregando: boolean = true;
  public erroAutenticacao: boolean = false;

  public ngOnInit(): void {
    if (!this.autenticacaoService.ehUsuarioAutenticado()) {
      this.carregando = false;
      this.erroAutenticacao = true;
      return;
    }

    if (this.autenticacaoService.possuiPerfilValido(PerfilEnum.COORDENADOR)) {
      this.router.navigate([
        RotasEnum.ROTA.COORDENADOR,
        RotasEnum.COORDENADOR.MATRIZ.LISTAR,
      ]);
    } else if (this.autenticacaoService.possuiPerfilValido(PerfilEnum.ALUNO)) {
      this.router.navigate([RotasEnum.ROTA.ALUNO, RotasEnum.ALUNO.AULAS]);
    } else {
      this.carregando = false;
      this.erroAutenticacao = true;
    }
  }

  public entrar(): void {
    this.autenticacaoService.autenticarUsuario();
  }
}

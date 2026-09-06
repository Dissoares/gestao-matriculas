import { Component, inject, OnInit, signal } from '@angular/core';
import { AutenticacaoService } from '@front/shared/services';
import { PerfilEnum, RotasEnum } from '@front/shared/enums';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ButtonModule, CardModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent implements OnInit {
  private readonly autenticacaoService = inject(AutenticacaoService);
  private readonly router = inject(Router);

  public readonly carregando = signal(true);
  public readonly erroConexao = signal(false);
  public readonly semPermissao = signal(false);

  public ngOnInit(): void {
    this.redirecionarPorPerfil();
  }

  private redirecionarPorPerfil(): void {
    if (!this.autenticacaoService.ehUsuarioAutenticado()) {
      this.erroConexao.set(true);
      this.carregando.set(false);
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
      this.semPermissao.set(true);
      this.carregando.set(false);
    }
  }

  public entrar(): void {
    this.autenticacaoService.autenticarUsuario();
  }

  public sair(): void {
    this.autenticacaoService.finalizarSessao();
  }
}

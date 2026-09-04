import { AutenticacaoService } from '../../../../../libs/shared/services/index';
import { PerfilEnum } from '../../../../../libs/shared/enums/index';
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

  public ngOnInit(): void {
    if (this.autenticacaoService.possuiPerfilValido(PerfilEnum.COORDENADOR)) {
      this.router.navigate(['/coordenador/matrizes']);
    } else if (this.autenticacaoService.possuiPerfilValido(PerfilEnum.ALUNO)) {
      this.router.navigate(['/aluno/disponiveis']);
    }
  }
}

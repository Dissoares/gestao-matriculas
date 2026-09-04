import { AutenticacaoService } from '../../../../../libs/shared/services/autenticacao.service';
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
    if (this.autenticacaoService.possuiPerfilValido('coordenador')) {
      this.router.navigate(['/coordenador/matrizes']);
    } else if (this.autenticacaoService.possuiPerfilValido('aluno')) {
      this.router.navigate(['/aluno/disponiveis']);
    }
  }
}

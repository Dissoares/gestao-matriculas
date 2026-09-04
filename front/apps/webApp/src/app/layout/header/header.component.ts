import { AutenticacaoService } from '@front/shared/services';
import { Component, inject, OnInit } from '@angular/core';
import { ToolbarModule } from 'primeng/toolbar';
import { ButtonModule } from 'primeng/button';
import { AvatarModule } from 'primeng/avatar';

@Component({
  standalone: true,
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  imports: [ToolbarModule, ButtonModule, AvatarModule],
})
export class HeaderComponent implements OnInit {
  public nomeUsuario: string = '';
  private readonly autenticacaoService = inject(AutenticacaoService);

  public ngOnInit(): void {
    this.nomeUsuario = this.autenticacaoService.obterNomeUsuario();
  }

  public sair(): void {
    this.autenticacaoService.finalizarSessao();
  }
}

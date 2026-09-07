import { AutenticacaoService } from '@front/shared/services';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { PerfilEnum, RotasEnum } from '@front/shared/enums';
import { ItemMenu } from '@front/shared/interfaces';
import { Component, inject, OnInit } from '@angular/core';

@Component({
  standalone: true,
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
  imports: [RouterLink, RouterLinkActive],
})
export class SidebarComponent implements OnInit {
  private readonly autenticacaoService = inject(AutenticacaoService);

  public itensMenu: Array<ItemMenu> = [];

  public ngOnInit(): void {
    this.itensMenu = this.montarMenu();
  }

  private montarMenu(): Array<ItemMenu> {
    if (this.autenticacaoService.possuiPerfilValido(PerfilEnum.COORDENADOR)) {
      return [
        {
          label: 'Matrizes',
          icon: 'pi pi-list',
          rota: `/${RotasEnum.ROTA.COORDENADOR}/${RotasEnum.COORDENADOR.MATRIZ.LISTAR}`,
        },
        {
          label: 'Nova Aula',
          icon: 'pi pi-plus',
          rota: `/${RotasEnum.ROTA.COORDENADOR}/${RotasEnum.COORDENADOR.MATRIZ.NOVA}`,
        },
      ];
    }

    if (this.autenticacaoService.possuiPerfilValido(PerfilEnum.ALUNO)) {
      return [
        {
          label: 'Aulas',
          icon: 'pi pi-video',
          rota: `/${RotasEnum.ROTA.ALUNO}/${RotasEnum.ALUNO.AULAS}`,
        },
        {
          label: 'Minhas Matrículas',
          icon: 'pi pi-id-card',
          rota: `/${RotasEnum.ROTA.ALUNO}/${RotasEnum.ALUNO.MATRICULAS.LISTAR}`,
        },
      ];
    }

    return [];
  }
}

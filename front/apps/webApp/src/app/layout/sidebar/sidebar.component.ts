import { AutenticacaoService } from '@front/shared/services';
import { PerfilEnum, RotasEnum } from '@front/shared/enums';
import { Component, inject, OnInit } from '@angular/core';
import { PanelMenuModule } from 'primeng/panelmenu';
import { MenuItem } from 'primeng/api';

@Component({
  standalone: true,
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
  imports: [PanelMenuModule],
})
export class SidebarComponent implements OnInit {
  private readonly autenticacaoService = inject(AutenticacaoService);

  public itensMenu: Array<MenuItem> = [];

  public ngOnInit(): void {
    this.itensMenu = this.montarMenu();
  }

  private montarMenu(): MenuItem[] {
    if (this.autenticacaoService.possuiPerfilValido(PerfilEnum.COORDENADOR)) {
      return [
        {
          label: 'Matrizes Curriculares',
          icon: 'pi pi-book',
          items: [
            {
              label: 'Listar',
              icon: 'pi pi-list',
              routerLink: `/${RotasEnum.ROTA.COORDENADOR}/${RotasEnum.COORDENADOR.MATRIZ.LISTAR}`,
            },
            {
              label: 'Nova Matriz',
              icon: 'pi pi-plus',
              routerLink: `/${RotasEnum.ROTA.COORDENADOR}/${RotasEnum.COORDENADOR.MATRIZ.NOVA}`,
            },
          ],
        },
      ];
    }

    if (this.autenticacaoService.possuiPerfilValido(PerfilEnum.ALUNO)) {
      return [
        {
          label: 'Aulas',
          icon: 'pi pi-video',
          routerLink: `/${RotasEnum.ROTA.ALUNO}/${RotasEnum.ALUNO.AULAS}`,
        },
        {
          label: 'Matrículas',
          icon: 'pi pi-id-card',
          items: [
            {
              label: 'Minhas Matrículas',
              icon: 'pi pi-list',
              routerLink: `/${RotasEnum.ROTA.ALUNO}/${RotasEnum.ALUNO.MATRICULAS.LISTAR}`,
            },
            {
              label: 'Nova Matrícula',
              icon: 'pi pi-plus',
              routerLink: `/${RotasEnum.ROTA.ALUNO}/${RotasEnum.ALUNO.MATRICULAS.NOVA}`,
            },
          ],
        },
      ];
    }

    return [];
  }
}

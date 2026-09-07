import {
  ReferenciasMatrizCurricular,
  FiltrosMatrizCurricular,
  MatrizCurricular,
} from '@front/shared/interfaces';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { MatrizCurricularService } from '@front/shared/services';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { RotasEnum } from '@front/shared/enums';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { FiltrosComponent } from './filtros/filtros.component';
import { Router, RouterLink } from '@angular/router';
import { HorarioPipe } from '@front/shared/pipes';
import { TooltipModule } from 'primeng/tooltip';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { Observable, finalize } from 'rxjs';
import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';

@Component({
  standalone: true,
  selector: 'app-listagem',
  templateUrl: './listagem.component.html',
  styleUrl: './listagem.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    ButtonModule,
    CardModule,
    ConfirmDialogModule,
    FiltrosComponent,
    HorarioPipe,
    ProgressSpinnerModule,
    RouterLink,
    TableModule,
    TagModule,
    TooltipModule,
  ],
})
export class ListagemComponent implements OnInit {
  private readonly servicoMatriz = inject(MatrizCurricularService);
  private readonly roteador = inject(Router);
  private readonly messageService = inject(MessageService);
  private readonly confirmationService = inject(ConfirmationService);

  public readonly matrizes = signal<Array<MatrizCurricular>>([]);
  public readonly referencias = signal<ReferenciasMatrizCurricular | null>(null);
  public readonly carregando = signal<boolean>(false);
  public readonly rotaNovaMatriz = `/${RotasEnum.ROTA.COORDENADOR}/${RotasEnum.COORDENADOR.MATRIZ.NOVA}`;

  public ngOnInit(): void {
    this.carregarReferencias();
    this.buscar();
  }

  public buscar(filtros: FiltrosMatrizCurricular = {}): void {
    this.carregando.set(true);

    const operacao: Observable<Array<MatrizCurricular>> =
      this.servicoMatriz.listar(filtros);

    operacao.pipe(finalize(() => this.carregando.set(false))).subscribe({
      next: (matrizes: Array<MatrizCurricular>): void => {
        this.matrizes.set(matrizes);
      },
    });
  }

  public editar(matriz: MatrizCurricular): void {
    this.roteador.navigate([
      '/',
      RotasEnum.ROTA.COORDENADOR,
      RotasEnum.COORDENADOR.MATRIZ.LISTAR,
      matriz.id,
      'editar',
    ]);
  }

  public excluir(matriz: MatrizCurricular): void {
    this.confirmationService.confirm({
      message: `Deseja excluir a matriz de <strong>${matriz.disciplina.nome}</strong>? Esta ação não pode ser desfeita.`,
      header: 'Confirmar exclusão',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Excluir',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: (): void => this.confirmarExclusao(matriz),
    });
  }

  public severidadeVagas(matriz: MatrizCurricular): 'success' | 'danger' {
    return matriz.vagasOcupadas < matriz.quantidadeMaximaAlunos
      ? 'success'
      : 'danger';
  }

  private carregarReferencias(): void {
    const operacao: Observable<ReferenciasMatrizCurricular> =
      this.servicoMatriz.listarReferencias();

    operacao.subscribe({
      next: (referencias: ReferenciasMatrizCurricular): void => {
        this.referencias.set(referencias);
      },
    });
  }

  private confirmarExclusao(matriz: MatrizCurricular): void {
    const operacao: Observable<void> = this.servicoMatriz.excluir(matriz.id);

    operacao.subscribe({
      next: (): void => {
        this.messageService.add({
          severity: 'success',
          summary: 'Sucesso',
          detail: 'Matriz excluída com sucesso.',
          life: 4000,
        });
        this.buscar();
      },
    });
  }
}

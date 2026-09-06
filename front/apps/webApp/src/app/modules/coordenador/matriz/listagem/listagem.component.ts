import {
  ReferenciasMatrizCurricular,
  FiltrosMatrizCurricular,
  MatrizCurricular,
} from '@front/shared/models';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { MatrizCurricularService } from '@front/shared/services';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { HttpErrorResponse } from '@angular/common/http';
import { InputNumberModule } from 'primeng/inputnumber';
import { Router, RouterLink } from '@angular/router';
import { PeriodoEnum } from '@front/shared/enums';
import { HorarioPipe } from '@front/shared/pipes';
import { MessageModule } from 'primeng/message';
import { TooltipModule } from 'primeng/tooltip';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { FormsModule } from '@angular/forms';
import { ToastModule } from 'primeng/toast';
import { TableModule } from 'primeng/table';
import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';
import { finalize } from 'rxjs';

@Component({
  standalone: true,
  selector: 'app-listagem',
  templateUrl: './listagem.component.html',
  styleUrls: ['./listagem.component.scss'],
  imports: [
    ButtonModule,
    CardModule,
    ConfirmDialogModule,
    FormsModule,
    HorarioPipe,
    InputNumberModule,
    MessageModule,
    ProgressSpinnerModule,
    RouterLink,
    SelectModule,
    TableModule,
    TagModule,
    ToastModule,
    TooltipModule,
  ],
  providers: [MessageService, ConfirmationService],
})
export class ListagemComponent implements OnInit {
  private readonly servicoMatriz = inject(MatrizCurricularService);
  private readonly roteador = inject(Router);
  private readonly messageService = inject(MessageService);
  private readonly confirmationService = inject(ConfirmationService);

  public readonly matrizes = signal<Array<MatrizCurricular>>([]);
  public readonly referencias = signal<ReferenciasMatrizCurricular | null>(null);
  public readonly carregando = signal<boolean>(false);
  public readonly mensagemErro = signal<string>('');
  public readonly periodos = PeriodoEnum.obterTodos();
  public filtros: FiltrosMatrizCurricular = {};

  public ngOnInit(): void {
    this.carregarReferencias();
    this.buscar();
  }

  public buscar(): void {
    this.carregando.set(true);
    this.mensagemErro.set('');
    this.servicoMatriz
      .listar(this.filtros)
      .pipe(finalize(() => this.carregando.set(false)))
      .subscribe({
        next: (matrizes: Array<MatrizCurricular>): void => this.matrizes.set(matrizes),
        error: (resposta: HttpErrorResponse): void => this.mensagemErro.set(this.obterMensagemErro(resposta)),
      });
  }

  public limparFiltros(): void {
    this.filtros = {};
    this.buscar();
  }

  public editar(matriz: MatrizCurricular): void {
    this.roteador.navigate(['/coordenador/matrizes', matriz.id, 'editar']);
  }

  public excluir(matriz: MatrizCurricular): void {
    this.confirmationService.confirm({
      message: `Deseja excluir logicamente a aula de <strong>${matriz.disciplina.nome}</strong>? Esta ação não pode ser desfeita.`,
      header: 'Confirmar exclusão',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Excluir',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: (): void => this.confirmarExclusao(matriz),
    });
  }

  public severidadeVagas(matriz: MatrizCurricular): 'success' | 'danger' {
    return matriz.vagasOcupadas < matriz.quantidadeMaximaAlunos ? 'success' : 'danger';
  }

  public descreverCursos(matriz: MatrizCurricular): string {
    return matriz.cursosAutorizados.map((curso) => curso.nome).join(', ');
  }

  private carregarReferencias(): void {
    this.servicoMatriz.listarReferencias().subscribe({
      next: (referencias: ReferenciasMatrizCurricular): void => this.referencias.set(referencias),
      error: (resposta: HttpErrorResponse): void => this.mensagemErro.set(this.obterMensagemErro(resposta)),
    });
  }

  private confirmarExclusao(matriz: MatrizCurricular): void {
    this.servicoMatriz.excluir(matriz.id).subscribe({
      next: (): void => {
        this.messageService.add({
          severity: 'success',
          summary: 'Sucesso',
          detail: 'Aula excluída com sucesso.',
          life: 4000,
        });
        this.buscar();
      },
      error: (resposta: HttpErrorResponse): void => {
        this.messageService.add({
          severity: 'error',
          summary: 'Erro',
          detail: this.obterMensagemErro(resposta),
          life: 5000,
        });
      },
    });
  }

  private obterMensagemErro(resposta: HttpErrorResponse): string {
    return resposta.error?.mensagem ?? 'Não foi possível concluir a operação.';
  }
}

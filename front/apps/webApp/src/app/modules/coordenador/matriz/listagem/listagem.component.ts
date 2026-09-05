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
import { InputNumberModule } from 'primeng/inputnumber';
import { Router, RouterLink } from '@angular/router';
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

  public readonly matrizes = signal<MatrizCurricular[]>([]);
  public readonly referencias = signal<ReferenciasMatrizCurricular | null>(
    null,
  );
  public readonly carregando = signal(false);
  public readonly mensagemErro = signal('');
  public filtros: FiltrosMatrizCurricular = {};

  public ngOnInit(): void {
    this.servicoMatriz.listarReferencias().subscribe({
      next: (referencias) => this.referencias.set(referencias),
      error: (erro) => this.mensagemErro.set(this.obterMensagemErro(erro)),
    });
    this.buscar();
  }

  public buscar(): void {
    this.carregando.set(true);
    this.mensagemErro.set('');
    this.servicoMatriz
      .listar(this.filtros)
      .pipe(finalize(() => this.carregando.set(false)))
      .subscribe({
        next: (matrizes) => this.matrizes.set(matrizes),
        error: (erro) => this.mensagemErro.set(this.obterMensagemErro(erro)),
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
      accept: () => {
        this.servicoMatriz.excluir(matriz.id).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Sucesso',
              detail: 'Aula excluída com sucesso.',
              life: 4000,
            });
            this.buscar();
          },
          error: (erro) =>
            this.messageService.add({
              severity: 'error',
              summary: 'Erro',
              detail: this.obterMensagemErro(erro),
              life: 5000,
            }),
        });
      },
    });
  }

  public descreverHorario(matriz: MatrizCurricular): string {
    return this.descreverDiaEHorario(matriz.horario);
  }

  public descreverCursos(matriz: MatrizCurricular): string {
    return matriz.cursosAutorizados.map((curso) => curso.nome).join(', ');
  }

  private descreverDiaEHorario(horario: {
    diaSemana: number;
    horaInicio: string;
    horaFim: string;
  }): string {
    const dias = [
      '',
      'Domingo',
      'Segunda',
      'Terça',
      'Quarta',
      'Quinta',
      'Sexta',
      'Sábado',
    ];
    return `${dias[horario.diaSemana]} ${horario.horaInicio}–${horario.horaFim}`;
  }

  private obterMensagemErro(erro: { error?: { mensagem?: string } }): string {
    return erro.error?.mensagem ?? 'Não foi possível concluir a operação.';
  }
}

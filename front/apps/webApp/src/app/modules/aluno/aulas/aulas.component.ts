import { Component, OnInit, inject, signal } from '@angular/core';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { MatriculaService } from '@front/shared/services';
import { AulaDisponivel } from '@front/shared/models';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';
import { finalize } from 'rxjs';

@Component({
  standalone: true,
  selector: 'app-aulas',
  templateUrl: './aulas.component.html',
  styleUrl: './aulas.component.scss',
  imports: [
    ButtonModule,
    CardModule,
    ProgressSpinnerModule,
    TableModule,
    TagModule,
    ToastModule,
  ],
  providers: [MessageService],
})
export class AulasComponent implements OnInit {
  private readonly servicoMatricula = inject(MatriculaService);
  private readonly messageService = inject(MessageService);

  public readonly aulas = signal<AulaDisponivel[]>([]);
  public readonly carregando = signal(false);
  public readonly matriculando = signal<number | null>(null);

  public ngOnInit(): void {
    this.buscarAulas();
  }

  public buscarAulas(): void {
    this.carregando.set(true);
    this.servicoMatricula
      .listarAulasDisponiveis()
      .pipe(finalize(() => this.carregando.set(false)))
      .subscribe({
        next: (aulas) => this.aulas.set(aulas),
        error: (erro) =>
          this.messageService.add({
            severity: 'error',
            summary: 'Erro',
            detail: this.obterMensagemErro(erro),
            life: 5000,
          }),
      });
  }

  public matricular(aula: AulaDisponivel): void {
    this.matriculando.set(aula.id);
    this.servicoMatricula
      .matricular(aula.id)
      .pipe(finalize(() => this.matriculando.set(null)))
      .subscribe({
        next: () => {
          this.messageService.add({
            severity: 'success',
            summary: 'Sucesso',
            detail: 'Matrícula realizada com sucesso!',
            life: 4000,
          });
          this.buscarAulas();
        },
        error: (erro) =>
          this.messageService.add({
            severity: 'error',
            summary: 'Erro',
            detail: this.obterMensagemErro(erro),
            life: 5000,
          }),
      });
  }

  public descreverHorario(aula: AulaDisponivel): string {
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
    return `${dias[aula.horario.diaSemana]} ${aula.horario.horaInicio}–${aula.horario.horaFim}`;
  }

  private obterMensagemErro(erro: { error?: { mensagem?: string } }): string {
    return erro.error?.mensagem ?? 'Não foi possível concluir a operação.';
  }
}

import { Component, OnInit, inject, signal } from '@angular/core';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { MatriculaService } from '@front/shared/services';
import { Matricula } from '@front/shared/models';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { DatePipe } from '@angular/common';
import { CardModule } from 'primeng/card';
import { finalize } from 'rxjs';

@Component({
  standalone: true,
  selector: 'app-matriculas-listagem',
  templateUrl: './listagem.component.html',
  styleUrl: './listagem.component.scss',
  imports: [
    ButtonModule,
    CardModule,
    DatePipe,
    ProgressSpinnerModule,
    TableModule,
    ToastModule,
  ],
  providers: [MessageService],
})
export class ListagemComponent implements OnInit {
  private readonly servicoMatricula = inject(MatriculaService);
  private readonly messageService = inject(MessageService);

  public readonly matriculas = signal<Matricula[]>([]);
  public readonly carregando = signal(false);

  public ngOnInit(): void {
    this.buscarMatriculas();
  }

  public buscarMatriculas(): void {
    this.carregando.set(true);
    this.servicoMatricula
      .listarMinhasMatriculas()
      .pipe(finalize(() => this.carregando.set(false)))
      .subscribe({
        next: (matriculas) => this.matriculas.set(matriculas),
        error: (erro) =>
          this.messageService.add({
            severity: 'error',
            summary: 'Erro',
            detail:
              erro.error?.mensagem ??
              'Não foi possível carregar as matrículas.',
            life: 5000,
          }),
      });
  }

  public descreverHorario(matricula: Matricula): string {
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
    return `${dias[matricula.horario.diaSemana]} ${matricula.horario.horaInicio}–${matricula.horario.horaFim}`;
  }
}

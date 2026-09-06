import { Component, OnInit, inject, signal } from '@angular/core';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { MatriculaService } from '@front/shared/services';
import { HttpErrorResponse } from '@angular/common/http';
import { Matricula } from '@front/shared/models';
import { HorarioPipe } from '@front/shared/pipes';
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
    HorarioPipe,
    ProgressSpinnerModule,
    TableModule,
    ToastModule,
  ],
  providers: [MessageService],
})
export class ListagemComponent implements OnInit {
  private readonly servicoMatricula = inject(MatriculaService);
  private readonly messageService = inject(MessageService);

  public readonly matriculas = signal<Array<Matricula>>([]);
  public readonly carregando = signal<boolean>(false);

  public ngOnInit(): void {
    this.buscarMatriculas();
  }

  public buscarMatriculas(): void {
    this.carregando.set(true);
    this.servicoMatricula
      .listarMinhasMatriculas()
      .pipe(finalize(() => this.carregando.set(false)))
      .subscribe({
        next: (matriculas: Array<Matricula>): void => this.matriculas.set(matriculas),
        error: (resposta: HttpErrorResponse): void => {
          this.messageService.add({
            severity: 'error',
            summary: 'Erro',
            detail: resposta.error?.mensagem ?? 'Não foi possível carregar as matrículas.',
            life: 5000,
          });
        },
      });
  }
}

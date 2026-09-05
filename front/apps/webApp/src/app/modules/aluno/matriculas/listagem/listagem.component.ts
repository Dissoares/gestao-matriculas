import { Component, OnInit, inject, signal } from '@angular/core';
import { MatriculaService } from '@front/shared/services';
import { Matricula } from '@front/shared/models';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { DatePipe } from '@angular/common';
import { finalize } from 'rxjs';

@Component({
  standalone: true,
  selector: 'app-matriculas-listagem',
  templateUrl: './listagem.component.html',
  styleUrl: './listagem.component.scss',
  imports: [ButtonModule, DatePipe, TableModule],
})
export class ListagemComponent implements OnInit {
  private readonly servicoMatricula = inject(MatriculaService);

  public readonly matriculas = signal<Matricula[]>([]);
  public readonly carregando = signal(false);
  public readonly mensagemErro = signal('');

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
          this.mensagemErro.set(
            erro.error?.mensagem ?? 'Não foi possível carregar as matrículas.',
          ),
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
    return `${dias[matricula.horario.diaSemana]} - ${matricula.horario.horaInicio} às ${matricula.horario.horaFim}`;
  }
}

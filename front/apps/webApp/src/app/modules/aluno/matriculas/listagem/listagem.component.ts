import { Component, OnInit, inject, signal } from '@angular/core';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { MatriculaService } from '@front/shared/services';
import { Matricula } from '@front/shared/interfaces';
import { HorarioPipe } from '@front/shared/pipes';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { Observable, finalize } from 'rxjs';
import { DatePipe } from '@angular/common';
import { CardModule } from 'primeng/card';

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
  ],
})
export class ListagemComponent implements OnInit {
  private readonly servicoMatricula = inject(MatriculaService);

  public readonly matriculas = signal<Array<Matricula>>([]);
  public readonly carregando = signal<boolean>(false);

  public ngOnInit(): void {
    this.buscarMatriculas();
  }

  public buscarMatriculas(): void {
    this.carregando.set(true);
    const operacao: Observable<Array<Matricula>> =
      this.servicoMatricula.listarMinhasMatriculas();

    operacao.pipe(finalize(() => this.carregando.set(false))).subscribe({
      next: (matriculas: Array<Matricula>): void => {
        this.matriculas.set(matriculas);
      },
    });
  }
}

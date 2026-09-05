import {
  ReferenciasMatrizCurricular,
  FiltrosMatrizCurricular,
  MatrizCurricular,
} from '@front/shared/models';
import { Component, OnInit, inject, signal } from '@angular/core';
import { MatrizCurricularService } from '@front/shared/services';
import { InputNumberModule } from 'primeng/inputnumber';
import { Router, RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { finalize } from 'rxjs';

@Component({
  standalone: true,
  selector: 'app-listagem',
  templateUrl: './listagem.component.html',
  styleUrls: ['./listagem.component.scss'],
  imports: [
    ButtonModule,
    FormsModule,
    InputNumberModule,
    RouterLink,
    SelectModule,
    TableModule,
  ],
})
export class ListagemComponent implements OnInit {
  private readonly servicoMatriz = inject(MatrizCurricularService);
  private readonly roteador = inject(Router);

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
    if (
      !window.confirm(
        `Deseja excluir logicamente a aula de ${matriz.disciplina.nome}?`,
      )
    ) {
      return;
    }
    this.servicoMatriz.excluir(matriz.id).subscribe({
      next: () => this.buscar(),
      error: (erro) => this.mensagemErro.set(this.obterMensagemErro(erro)),
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
    return `${dias[horario.diaSemana]} - ${horario.horaInicio} às ${horario.horaFim}`;
  }

  private obterMensagemErro(erro: { error?: { mensagem?: string } }): string {
    return erro.error?.mensagem ?? 'Não foi possível concluir a operação.';
  }
}

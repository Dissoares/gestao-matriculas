import {
  ReferenciasMatrizCurricular,
  RequisicaoAtualizarMatriz,
  RequisicaoCriarMatriz,
  MatrizCurricular,
} from '@front/shared/models';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Component, OnInit, inject, signal } from '@angular/core';
import { MatrizCurricularService } from '@front/shared/services';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { InputNumberModule } from 'primeng/inputnumber';
import { MultiSelectModule } from 'primeng/multiselect';
import { MessageModule } from 'primeng/message';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { CardModule } from 'primeng/card';
import { finalize, forkJoin, of } from 'rxjs';

@Component({
  standalone: true,
  selector: 'app-formulario',
  templateUrl: './formulario.component.html',
  styleUrls: ['./formulario.component.scss'],
  imports: [
    ButtonModule,
    CardModule,
    InputNumberModule,
    MessageModule,
    MultiSelectModule,
    ProgressSpinnerModule,
    ReactiveFormsModule,
    RouterLink,
    SelectModule,
  ],
})
export class FormularioComponent implements OnInit {
  private readonly servicoMatriz = inject(MatrizCurricularService);
  private readonly rotaAtiva = inject(ActivatedRoute);
  private readonly roteador = inject(Router);
  private readonly construtorFormulario = inject(FormBuilder);

  public readonly referencias = signal<ReferenciasMatrizCurricular | null>(
    null,
  );
  public readonly carregando = signal(true);
  public readonly salvando = signal(false);
  public readonly mensagemErro = signal('');
  public idMatriz: number | null = null;
  public readonly formulario = this.construtorFormulario.nonNullable.group({
    disciplinaId: [0, [Validators.required, Validators.min(1)]],
    professorId: [0, [Validators.required, Validators.min(1)]],
    horarioId: [0, [Validators.required, Validators.min(1)]],
    cursosAutorizadosIds: this.construtorFormulario.nonNullable.control<
      number[]
    >([], [Validators.required, Validators.minLength(1)]),
    quantidadeMaximaAlunos: [0, [Validators.required, Validators.min(1)]],
  });

  public ngOnInit(): void {
    const idDaRota = Number(this.rotaAtiva.snapshot.paramMap.get('id'));
    this.idMatriz =
      Number.isInteger(idDaRota) && idDaRota > 0 ? idDaRota : null;

    forkJoin({
      referencias: this.servicoMatriz.listarReferencias(),
      matriz: this.idMatriz
        ? this.servicoMatriz.buscarPorId(this.idMatriz)
        : of(null),
    }).subscribe({
      next: ({ referencias, matriz }) => {
        this.referencias.set(referencias);
        if (matriz) this.preencherParaEdicao(matriz);
        this.carregando.set(false);
      },
      error: (erro) => {
        this.mensagemErro.set(this.obterMensagemErro(erro));
        this.carregando.set(false);
      },
    });
  }

  public salvar(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    this.salvando.set(true);
    const valores = this.formulario.getRawValue();
    const requisicao = this.idMatriz
      ? this.servicoMatriz.atualizar(this.idMatriz, {
          professorId: valores.professorId,
          horarioId: valores.horarioId,
          cursosAutorizadosIds: valores.cursosAutorizadosIds,
        } satisfies RequisicaoAtualizarMatriz)
      : this.servicoMatriz.criar({
          disciplinaId: valores.disciplinaId,
          professorId: valores.professorId,
          horarioId: valores.horarioId,
          cursosAutorizadosIds: valores.cursosAutorizadosIds,
          quantidadeMaximaAlunos: valores.quantidadeMaximaAlunos,
        } satisfies RequisicaoCriarMatriz);

    requisicao.pipe(finalize(() => this.salvando.set(false))).subscribe({
      next: () => this.roteador.navigate(['/coordenador/matrizes']),
      error: (erro) => this.mensagemErro.set(this.obterMensagemErro(erro)),
    });
  }

  public campoInvalido(nomeCampo: string): boolean {
    const campo = this.formulario.get(nomeCampo);
    return !!campo && campo.invalid && (campo.touched || campo.dirty);
  }

  public descreverHorario(horario: {
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

  private preencherParaEdicao(matriz: MatrizCurricular): void {
    this.formulario.patchValue({
      disciplinaId: matriz.disciplina.id,
      professorId: matriz.professor.id,
      horarioId: matriz.horario.id,
      cursosAutorizadosIds: matriz.cursosAutorizados.map((curso) => curso.id),
      quantidadeMaximaAlunos: matriz.quantidadeMaximaAlunos,
    });
    this.formulario.controls.disciplinaId.disable();
    this.formulario.controls.quantidadeMaximaAlunos.disable();
  }

  private obterMensagemErro(erro: { error?: { mensagem?: string } }): string {
    return erro.error?.mensagem ?? 'Não foi possível concluir a operação.';
  }
}

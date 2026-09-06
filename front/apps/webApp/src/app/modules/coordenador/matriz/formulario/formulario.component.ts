import {
  ReferenciasMatrizCurricular,
  RequisicaoAtualizarMatriz,
  RequisicaoCriarMatriz,
  MatrizCurricular,
} from '@front/shared/models';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Component, OnInit, inject, signal } from '@angular/core';
import { MatrizCurricularService } from '@front/shared/services';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { HttpErrorResponse } from '@angular/common/http';
import { InputNumberModule } from 'primeng/inputnumber';
import { MultiSelectModule } from 'primeng/multiselect';
import { HorarioPipe } from '@front/shared/pipes';
import { MessageModule } from 'primeng/message';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { finalize, forkJoin, of } from 'rxjs';
import { CardModule } from 'primeng/card';

@Component({
  standalone: true,
  selector: 'app-formulario',
  templateUrl: './formulario.component.html',
  styleUrls: ['./formulario.component.scss'],
  imports: [
    ButtonModule,
    CardModule,
    HorarioPipe,
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
  private readonly fb = inject(FormBuilder);
  private readonly rotaAtiva = inject(ActivatedRoute);
  private readonly roteador = inject(Router);

  public readonly referencias = signal<ReferenciasMatrizCurricular | null>(null);
  public readonly carregando = signal(true);
  public readonly salvando = signal(false);
  public readonly mensagemErro = signal('');
  public idMatriz: number | null = null;
  public formulario!: FormGroup;

  public ngOnInit(): void {
    this.criarFormulario();

    const idDaRota = Number(this.rotaAtiva.snapshot.paramMap.get('id'));
    this.idMatriz = Number.isInteger(idDaRota) && idDaRota > 0 ? idDaRota : null;

    forkJoin({
      referencias: this.servicoMatriz.listarReferencias(),
      matriz: this.idMatriz ? this.servicoMatriz.buscarPorId(this.idMatriz) : of(null),
    }).subscribe({
      next: ({ referencias, matriz }) => {
        this.referencias.set(referencias);
        if (matriz) this.preencherParaEdicao(matriz);
        this.carregando.set(false);
      },
      error: (resposta: HttpErrorResponse) => {
        this.mensagemErro.set(this.obterMensagemErro(resposta));
        this.carregando.set(false);
      },
    });
  }

  public salvar(): void {
    const valores = this.formulario.getRawValue();

    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    this.salvando.set(true);

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
      error: (resposta: HttpErrorResponse) => {
        this.mensagemErro.set(this.obterMensagemErro(resposta));
      },
    });
  }

  public campoInvalido(nomeCampo: string): boolean {
    const campo = this.formulario.get(nomeCampo);
    return !!campo && campo.invalid && (campo.touched || campo.dirty);
  }

  private criarFormulario(): void {
    this.formulario = this.fb.nonNullable.group({
      disciplinaId: [0, [Validators.required, Validators.min(1)]],
      professorId: [0, [Validators.required, Validators.min(1)]],
      horarioId: [0, [Validators.required, Validators.min(1)]],
      cursosAutorizadosIds: this.fb.nonNullable.control<number[]>(
        [],
        [Validators.required, Validators.minLength(1)],
      ),
      quantidadeMaximaAlunos: [0, [Validators.required, Validators.min(1)]],
    });
  }

  private preencherParaEdicao(matriz: MatrizCurricular): void {
    this.formulario.patchValue({
      disciplinaId: matriz.disciplina.id,
      professorId: matriz.professor.id,
      horarioId: matriz.horario.id,
      cursosAutorizadosIds: matriz.cursosAutorizados.map((curso) => curso.id),
      quantidadeMaximaAlunos: matriz.quantidadeMaximaAlunos,
    });
    this.formulario.get('disciplinaId')?.disable();
    this.formulario.get('quantidadeMaximaAlunos')?.disable();
  }

  private obterMensagemErro(resposta: HttpErrorResponse): string {
    return resposta.error?.mensagem ?? 'Não foi possível concluir a operação.';
  }
}

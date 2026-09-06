import {
  ReferenciasMatrizCurricular,
  RequisicaoAtualizarMatriz,
  RequisicaoCriarMatriz,
  MatrizFormularioDados,
  MatrizCurricular,
  MatrizForm,
} from '@front/shared/interfaces';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  AbstractControl,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Component, OnInit, inject, signal } from '@angular/core';
import { MatrizCurricularService } from '@front/shared/services';
import { InputNumberModule } from 'primeng/inputnumber';
import { MultiSelectModule } from 'primeng/multiselect';
import { HorarioPipe } from '@front/shared/pipes';
import { RotasEnum } from '@front/shared/enums';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { Observable, finalize } from 'rxjs';
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
    MultiSelectModule,
    ReactiveFormsModule,
    RouterLink,
    SelectModule,
  ],
})
export class FormularioComponent implements OnInit {
  private readonly servicoMatriz = inject(MatrizCurricularService);
  private readonly fb = inject(NonNullableFormBuilder);
  private readonly rotaAtiva = inject(ActivatedRoute);
  private readonly roteador = inject(Router);

  public readonly referencias = signal<ReferenciasMatrizCurricular | null>(
    null,
  );
  public readonly rotaMatrizes = `/${RotasEnum.ROTA.COORDENADOR}/${RotasEnum.COORDENADOR.MATRIZ.LISTAR}`;
  public readonly salvando = signal<boolean>(false);

  public idMatriz: number | null = null;
  public formulario!: MatrizForm;

  public ngOnInit(): void {
    this.criarFormulario();
    this.carregarDadosDaRota();
  }

  private criarFormulario(): void {
    this.formulario = this.fb.group({
      disciplinaId: [0, [Validators.required, Validators.min(1)]],
      professorId: [0, [Validators.required, Validators.min(1)]],
      horarioId: [0, [Validators.required, Validators.min(1)]],
      cursosAutorizadosIds: this.fb.control<Array<number>>(
        [],
        [Validators.required, Validators.minLength(1)],
      ),
      quantidadeMaximaAlunos: [0, [Validators.required, Validators.min(1)]],
    });
  }

  public salvar(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    this.salvando.set(true);

    const operacao: Observable<MatrizCurricular> = this.idMatriz
      ? this.executarAtualizacao()
      : this.executarCriacao();

    operacao.pipe(finalize(() => this.salvando.set(false))).subscribe({
      next: (): void => {
        this.navegarParaListagem();
      },
    });
  }

  private executarCriacao(): Observable<MatrizCurricular> {
    const requisicao: RequisicaoCriarMatriz = this.formulario.getRawValue();
    return this.servicoMatriz.criar(requisicao);
  }

  private executarAtualizacao(): Observable<MatrizCurricular> {
    const { professorId, horarioId, cursosAutorizadosIds } =
      this.formulario.getRawValue();

    const requisicao: RequisicaoAtualizarMatriz = {
      id: this.idMatriz!,
      professorId,
      horarioId,
      cursosAutorizadosIds,
    };

    return this.servicoMatriz.atualizar(requisicao);
  }

  public campoInvalido(nomeCampo: string): boolean {
    const campo: AbstractControl<any, any, any> | null =
      this.formulario.get(nomeCampo);
    return !!campo && campo.invalid && (campo.touched || campo.dirty);
  }

  private carregarDadosDaRota(): void {
    const dados: MatrizFormularioDados =
      this.rotaAtiva.snapshot.data['formulario'];
    this.referencias.set(dados.referencias);

    if (dados.matriz) {
      this.idMatriz = dados.matriz.id;
      this.preencherParaEdicao(dados.matriz);
    }
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

  private navegarParaListagem(): void {
    this.roteador.navigate([this.rotaMatrizes]);
  }
}

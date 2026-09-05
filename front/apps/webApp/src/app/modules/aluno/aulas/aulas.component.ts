import { Component, OnInit, inject, signal } from '@angular/core';
import { MatriculaService } from '@front/shared/services';
import { AulaDisponivel } from '@front/shared/models';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { finalize } from 'rxjs';

@Component({
  standalone: true,
  selector: 'app-aulas',
  templateUrl: './aulas.component.html',
  styleUrl: './aulas.component.scss',
  imports: [ButtonModule, TableModule],
})
export class AulasComponent implements OnInit {
  private readonly servicoMatricula = inject(MatriculaService);

  public readonly aulas = signal<AulaDisponivel[]>([]);
  public readonly carregando = signal(false);
  public readonly matriculando = signal<number | null>(null);
  public readonly mensagemErro = signal('');
  public readonly mensagemSucesso = signal('');

  public ngOnInit(): void {
    this.buscarAulas();
  }

  public buscarAulas(): void {
    this.carregando.set(true);
    this.mensagemErro.set('');
    this.servicoMatricula
      .listarAulasDisponiveis()
      .pipe(finalize(() => this.carregando.set(false)))
      .subscribe({
        next: (aulas) => this.aulas.set(aulas),
        error: (erro) => this.mensagemErro.set(this.obterMensagemErro(erro)),
      });
  }

  public matricular(aula: AulaDisponivel): void {
    this.matriculando.set(aula.id);
    this.mensagemErro.set('');
    this.mensagemSucesso.set('');
    this.servicoMatricula
      .matricular(aula.id)
      .pipe(finalize(() => this.matriculando.set(null)))
      .subscribe({
        next: () => {
          this.mensagemSucesso.set('Matrícula realizada com sucesso.');
          this.buscarAulas();
        },
        error: (erro) => this.mensagemErro.set(this.obterMensagemErro(erro)),
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
    return `${dias[aula.horario.diaSemana]} - ${aula.horario.horaInicio} às ${aula.horario.horaFim}`;
  }

  private obterMensagemErro(erro: { error?: { mensagem?: string } }): string {
    return erro.error?.mensagem ?? 'Não foi possível concluir a matrícula.';
  }
}

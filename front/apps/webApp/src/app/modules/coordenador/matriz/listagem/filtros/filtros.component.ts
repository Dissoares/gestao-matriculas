import {
  ReferenciasMatrizCurricular,
  FiltrosMatrizCurricular,
} from '@front/shared/interfaces';
import {
  ChangeDetectionStrategy,
  Component,
  input,
  output,
} from '@angular/core';
import { InputNumberModule } from 'primeng/inputnumber';
import { PeriodoEnum } from '@front/shared/enums';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'app-filtros-matriz',
  templateUrl: './filtros.component.html',
  styleUrl: './filtros.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ButtonModule, FormsModule, InputNumberModule, SelectModule],
})
export class FiltrosComponent {
  public readonly referencias = input<ReferenciasMatrizCurricular | null>(null);
  public readonly buscar = output<FiltrosMatrizCurricular>();

  public readonly periodos = PeriodoEnum.obterTodos();
  public filtros: FiltrosMatrizCurricular = {};

  public filtrar(): void {
    this.buscar.emit({ ...this.filtros });
  }

  public limpar(): void {
    this.filtros = {};
    this.buscar.emit({});
  }
}

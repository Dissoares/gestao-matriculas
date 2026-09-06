import { environment } from '@front/environments';
import { AulaDisponivel, Matricula } from '@front/shared/models';
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class MatriculaService {
  private readonly clienteHttp = inject(HttpClient);
  private readonly urlBase = `${environment.apiUrl}/api/aluno`;

  public listarAulasDisponiveis(): Observable<Array<AulaDisponivel>> {
    return this.clienteHttp.get<Array<AulaDisponivel>>(
      `${this.urlBase}/aulas-disponiveis`,
    );
  }

  public listarMinhasMatriculas(): Observable<Array<Matricula>> {
    return this.clienteHttp.get<Array<Matricula>>(`${this.urlBase}/matriculas`);
  }

  public matricular(idMatriz: number): Observable<Matricula> {
    return this.clienteHttp.post<Matricula>(
      `${this.urlBase}/matriculas/${idMatriz}`,
      null,
    );
  }
}

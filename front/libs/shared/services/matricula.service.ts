import { AulaDisponivel, Matricula } from '@front/shared/models';
import { environment } from '@front/shared/environments';
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class MatriculaService {
  private readonly clienteHttp = inject(HttpClient);
  private readonly urlBase = `${environment.apiUrl}/api/aluno`;

  public listarAulasDisponiveis(): Observable<AulaDisponivel[]> {
    return this.clienteHttp.get<AulaDisponivel[]>(
      `${this.urlBase}/aulas-disponiveis`,
    );
  }

  public listarMinhasMatriculas(): Observable<Matricula[]> {
    return this.clienteHttp.get<Matricula[]>(`${this.urlBase}/matriculas`);
  }

  public matricular(idMatriz: number): Observable<Matricula> {
    return this.clienteHttp.post<Matricula>(
      `${this.urlBase}/matriculas/${idMatriz}`,
      null,
    );
  }
}

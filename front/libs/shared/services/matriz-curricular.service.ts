import {
  ReferenciasMatrizCurricular,
  RequisicaoAtualizarMatriz,
  FiltrosMatrizCurricular,
  RequisicaoCriarMatriz,
  MatrizCurricular,
} from '@front/shared/models';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class MatrizCurricularService {
  private readonly clienteHttp = inject(HttpClient);
  private readonly urlBase = 'http://localhost:8080/api/matrizes';

  public listar(
    filtros: FiltrosMatrizCurricular = {},
  ): Observable<MatrizCurricular[]> {
    let parametros = new HttpParams();
    Object.entries(filtros).forEach(([chave, valor]) => {
      if (valor !== undefined && valor !== null && valor !== '') {
        parametros = parametros.set(chave, String(valor));
      }
    });
    return this.clienteHttp.get<MatrizCurricular[]>(this.urlBase, {
      params: parametros,
    });
  }

  public buscarPorId(idMatriz: number): Observable<MatrizCurricular> {
    return this.clienteHttp.get<MatrizCurricular>(
      `${this.urlBase}/${idMatriz}`,
    );
  }

  public listarReferencias(): Observable<ReferenciasMatrizCurricular> {
    return this.clienteHttp.get<ReferenciasMatrizCurricular>(
      `${this.urlBase}/referencias`,
    );
  }

  public criar(
    requisicao: RequisicaoCriarMatriz,
  ): Observable<MatrizCurricular> {
    return this.clienteHttp.post<MatrizCurricular>(this.urlBase, requisicao);
  }

  public atualizar(
    idMatriz: number,
    requisicao: RequisicaoAtualizarMatriz,
  ): Observable<MatrizCurricular> {
    return this.clienteHttp.put<MatrizCurricular>(
      `${this.urlBase}/${idMatriz}`,
      requisicao,
    );
  }

  public excluir(idMatriz: number): Observable<void> {
    return this.clienteHttp.delete<void>(`${this.urlBase}/${idMatriz}`);
  }
}

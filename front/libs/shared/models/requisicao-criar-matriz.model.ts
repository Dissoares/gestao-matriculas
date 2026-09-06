export interface RequisicaoCriarMatriz {
  disciplinaId: number;
  professorId: number;
  horarioId: number;
  cursosAutorizadosIds: Array<number>;
  quantidadeMaximaAlunos: number;
}

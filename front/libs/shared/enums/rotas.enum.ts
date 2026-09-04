export class RotasEnum {
  static readonly LOGIN: string = 'login';

  static readonly ROTA = {
    COORDENADOR: 'coordenador',
    ALUNO: 'aluno',
  };

  static readonly COORDENADOR = {
    MATRIZ: {
      LISTAR: 'matrizes',
      NOVA: 'matrizes/nova',
      EDITAR: 'editar',
    },
  };

  static readonly ALUNO = {
    MATRICULAS: {
      LISTAR: 'matriculas',
      NOVA: 'matricula/nova',
    },
    AULAS: 'aulas',
  };
}

export class RotasEnum {
  static readonly LOGIN: string = 'login';

  static readonly ROTA = {
    COORDENADOR: 'coordenador',
    ALUNO: 'aluno',
  };

  static readonly COORDENADOR = {
    MATRIZ: {
      LISTAR: 'matriz',
      NOVA: 'matriz/nova',
      EDITAR: 'matriz/editar',
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

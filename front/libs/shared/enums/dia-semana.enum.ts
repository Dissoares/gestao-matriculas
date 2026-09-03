export class DiaSemanaEnum {
  public static DOMINGO = new DiaSemanaEnum(1, 'DOMINGO');
  public static SEGUNDA = new DiaSemanaEnum(2, 'SEGUNDA');
  public static TERCA = new DiaSemanaEnum(3, 'TERCA');
  public static QUARTA = new DiaSemanaEnum(4, 'QUARTA');
  public static QUINTA = new DiaSemanaEnum(5, 'QUINTA');
  public static SEXTA = new DiaSemanaEnum(6, 'SEXTA');
  public static SABADO = new DiaSemanaEnum(7, 'SABADO');

  private constructor(
    public readonly codigo: number,
    public readonly descricao: string,
  ) {}

  public static obterTodos(): Array<DiaSemanaEnum> {
    return [
      this.DOMINGO,
      this.SEGUNDA,
      this.TERCA,
      this.QUARTA,
      this.QUINTA,
      this.SEXTA,
      this.SABADO,
    ];
  }

  public static buscarPorCodigo(codigo: number): DiaSemanaEnum | undefined {
    return this.obterTodos().find((skill) => skill.codigo === codigo);
  }
}

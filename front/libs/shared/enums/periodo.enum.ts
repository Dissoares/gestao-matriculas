export class PeriodoEnum {
  public static MANHA = new PeriodoEnum(1, 'MANHÃ');
  public static TARDE = new PeriodoEnum(2, 'TARDE');
  public static NOITE = new PeriodoEnum(3, 'NOITE');

  private constructor(
    public readonly codigo: number,
    public readonly descricao: string,
  ) {}

  public static obterTodos(): Array<PeriodoEnum> {
    return [this.MANHA, this.TARDE, this.NOITE];
  }

  public static buscarPorCodigo(codigo: number): PeriodoEnum | undefined {
    return this.obterTodos().find((skill) => skill.codigo === codigo);
  }
}

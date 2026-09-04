export class PerfilEnum {
  public static ALUNO = new PerfilEnum(1, 'aluno');
  public static COORDENADOR = new PerfilEnum(2, 'coordenador');

  private constructor(
    public readonly codigo: number,
    public readonly descricao: string,
  ) {}

  public static obterTodos(): Array<PerfilEnum> {
    return [this.ALUNO, this.COORDENADOR];
  }

  public static buscarPorCodigo(codigo: number): PerfilEnum | undefined {
    return this.obterTodos().find((skill) => skill.codigo === codigo);
  }
}

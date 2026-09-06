import { Curso } from './.interface';

export interface Aluno {
  id: number;
  nome: string;
  keycloakId: string;
  curso: Curso;
}

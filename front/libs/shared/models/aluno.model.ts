import { Curso } from './curso.model';

export interface Aluno {
  id: number;
  nome: string;
  keycloakId: string;
  curso: Curso;
}

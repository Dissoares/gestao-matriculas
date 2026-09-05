CREATE INDEX idx_matriz_coordenador_ativo ON matriz_curricular (coordenador_id, ativo);
CREATE INDEX idx_matricula_matriz ON matricula (matriz_curricular_id);
CREATE INDEX idx_matricula_aluno ON matricula (aluno_id);

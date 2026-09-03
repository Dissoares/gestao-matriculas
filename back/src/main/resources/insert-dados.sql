INSERT INTO disciplina (id, nome) VALUES
(1, 'Matemática'),
(2, 'Física'),
(3, 'Química'),
(4, 'Biologia'),
(5, 'História'),
(6, 'Geografia'),
(7, 'Português'),
(8, 'Inglês'),
(9, 'Filosofia'),
(10, 'Sociologia'),
(11, 'Programação'),
(12, 'Estrutura de Dados'),
(13, 'Algoritmos'),
(14, 'Cálculo'),
(15, 'Banco de Dados');

INSERT INTO professor (id, nome) VALUES
(1, 'Carlos Andrade'),
(2, 'Ana Lima'),
(3, 'Roberto Santos'),
(4, 'Fernanda Oliveira'),
(5, 'Marcelo Costa');

INSERT INTO horario (id, dia_semana, hora_inicio, hora_fim) VALUES
(1, 'SEGUNDA', '08:00:00', '10:00:00'),
(2, 'SEGUNDA', '19:00:00', '21:00:00'),
(3, 'TERCA', '08:00:00', '10:00:00'),
(4, 'TERCA', '14:00:00', '16:00:00'),
(5, 'QUARTA', '10:00:00', '12:00:00'),
(6, 'QUARTA', '19:00:00', '21:00:00'),
(7, 'QUINTA', '08:00:00', '10:00:00'),
(8, 'QUINTA', '14:00:00', '16:00:00'),
(9, 'SEXTA', '10:00:00', '12:00:00'),
(10, 'SEXTA', '19:00:00', '21:00:00');

INSERT INTO curso (id, nome) VALUES
(1, 'Engenharia de Software'),
(2, 'Ciência da Computação'),
(3, 'Sistemas de Informação'),
(4, 'Análise e Desenvolvimento de Sistemas'),
(5, 'Engenharia da Computação'),
(6, 'Matemática'),
(7, 'Física'),
(8, 'Administração'),
(9, 'Pedagogia');

INSERT INTO coordenador (id, nome, keycloak_id) VALUES
(1, 'João Coordenador', 'coord-uuid-1'),
(2, 'Maria Coordenadora', 'coord-uuid-2'),
(3, 'Paulo Coordenador', 'coord-uuid-3');

INSERT INTO aluno (id, nome, keycloak_id, curso_id) VALUES
(1, 'Lucas Almeida', 'aluno-uuid-1', 1),
(2, 'Juliana Ferreira', 'aluno-uuid-2', 2),
(3, 'Bruno Nascimento', 'aluno-uuid-3', 3),
(4, 'Camila Rodrigues', 'aluno-uuid-4', 1),
(5, 'Rafael Pereira', 'aluno-uuid-5', 4);

INSERT INTO matriz_curricular (id, disciplina_id, professor_id, horario_id, quantidade_maxima_alunos, coordenador_id, ativo) VALUES
(1, 11, 1, 1, 40, 1, true),
(2, 12, 2, 3, 35, 1, true),
(3, 14, 3, 5, 50, 2, true),
(4, 15, 4, 6, 30, 2, true),
(5, 13, 5, 9, 40, 3, true);

INSERT INTO matriz_curso (matriz_id, curso_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5),
(2, 1), (2, 2), (2, 3),
(3, 1), (3, 2), (3, 6),
(4, 1), (4, 2), (4, 3), (4, 4),
(5, 1), (5, 2), (5, 5);

ALTER SEQUENCE disciplina_seq RESTART WITH 16;
ALTER SEQUENCE professor_seq RESTART WITH 6;
ALTER SEQUENCE horario_seq RESTART WITH 11;
ALTER SEQUENCE curso_seq RESTART WITH 10;
ALTER SEQUENCE coordenador_seq RESTART WITH 4;
ALTER SEQUENCE aluno_seq RESTART WITH 6;
ALTER SEQUENCE matriz_curricular_seq RESTART WITH 6;

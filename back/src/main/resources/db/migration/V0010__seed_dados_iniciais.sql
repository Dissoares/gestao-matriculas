INSERT INTO disciplina (id, nome) VALUES
(1, 'Matematica'), (2, 'Fisica'), (3, 'Quimica'), (4, 'Biologia'), (5, 'Historia'),
(6, 'Geografia'), (7, 'Portugues'), (8, 'Ingles'), (9, 'Filosofia'), (10, 'Sociologia'),
(11, 'Programacao'), (12, 'Estrutura de Dados'), (13, 'Algoritmos'), (14, 'Calculo'), (15, 'Banco de Dados');

INSERT INTO professor (id, nome) VALUES
(1, 'Carlos Andrade'), (2, 'Ana Lima'), (3, 'Roberto Santos'),
(4, 'Fernanda Oliveira'), (5, 'Marcelo Costa');

INSERT INTO horario (id, dia_semana, hora_inicio, hora_fim) VALUES
(1, 2, '08:00:00', '10:00:00'), (2, 2, '19:00:00', '21:00:00'),
(3, 3, '08:00:00', '10:00:00'), (4, 3, '14:00:00', '16:00:00'),
(5, 4, '10:00:00', '12:00:00'), (6, 4, '19:00:00', '21:00:00'),
(7, 5, '08:00:00', '10:00:00'), (8, 5, '14:00:00', '16:00:00'),
(9, 6, '10:00:00', '12:00:00'), (10, 6, '19:00:00', '21:00:00');

INSERT INTO curso (id, nome) VALUES
(1, 'Engenharia de Software'), (2, 'Ciencia da Computacao'), (3, 'Sistemas de Informacao'),
(4, 'Analise e Desenvolvimento de Sistemas'), (5, 'Engenharia da Computacao'),
(6, 'Matematica'), (7, 'Fisica'), (8, 'Administracao'), (9, 'Pedagogia');

INSERT INTO coordenador (id, nome, keycloak_id) VALUES
(1, 'Joao Coordenador',   'c0000001-0000-0000-0000-000000000001'),
(2, 'Maria Coordenadora', 'c0000001-0000-0000-0000-000000000002'),
(3, 'Paulo Coordenador',  'c0000001-0000-0000-0000-000000000003');

INSERT INTO aluno (id, nome, keycloak_id, curso_id) VALUES
(1, 'Lucas Almeida',    'a0000001-0000-0000-0000-000000000001', 1),
(2, 'Juliana Ferreira', 'a0000001-0000-0000-0000-000000000002', 2),
(3, 'Bruno Nascimento', 'a0000001-0000-0000-0000-000000000003', 3),
(4, 'Camila Rodrigues', 'a0000001-0000-0000-0000-000000000004', 1),
(5, 'Rafael Pereira',   'a0000001-0000-0000-0000-000000000005', 4);

INSERT INTO matriz_curricular (id, disciplina_id, professor_id, horario_id, quantidade_maxima_alunos, coordenador_id, ativo) VALUES
(1, 11, 1, 1, 40, 1, true), (2, 12, 2, 3, 35, 1, true),
(3, 14, 3, 5, 50, 2, true), (4, 15, 4, 6, 30, 2, true),
(5, 13, 5, 9, 40, 3, true);

INSERT INTO matriz_curso (matriz_id, curso_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5),
(2, 1), (2, 2), (2, 3),
(3, 1), (3, 2), (3, 6),
(4, 1), (4, 2), (4, 3), (4, 4),
(5, 1), (5, 2), (5, 5);

-- Posiciona as sequences acima dos IDs do seed (próxima alocação: 51-100)
SELECT setval('disciplina_seq', 50);
SELECT setval('professor_seq', 50);
SELECT setval('horario_seq', 50);
SELECT setval('curso_seq', 50);
SELECT setval('coordenador_seq', 50);
SELECT setval('aluno_seq', 50);
SELECT setval('matriz_curricular_seq', 50);
SELECT setval('matricula_seq', 50);

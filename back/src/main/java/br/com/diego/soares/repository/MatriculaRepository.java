package br.com.diego.soares.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import br.com.diego.soares.enums.DiaSemanaEnum;
import br.com.diego.soares.entity.Matricula;
import java.time.LocalTime;
import java.util.List;

@ApplicationScoped
public class MatriculaRepository implements PanacheRepository<Matricula> {

    public long contarPorIdMatriz(Long idMatriz) {
        return count("matrizCurricular.id", idMatriz);
    }

    public boolean existePorAlunoEMatriz(Long idAluno, Long idMatriz) {
        return count("aluno.id = ?1 and matrizCurricular.id = ?2", idAluno, idMatriz) > 0;
    }

    public boolean existeConflitoDeHorario(String idKeycloakAluno, DiaSemanaEnum diaSemana, LocalTime inicio, LocalTime fim) {
        return count("""
                aluno.keycloakId = ?1
                and matrizCurricular.horario.diaSemana = ?2
                and matrizCurricular.horario.horaInicio < ?4
                and matrizCurricular.horario.horaFim > ?3
                """, idKeycloakAluno, diaSemana, inicio, fim) > 0;
    }

    public List<Matricula> buscarDoAluno(String idKeycloakAluno) {
        return getEntityManager()
                .createQuery("""
                                    SELECT 
                                        matricula 
                                    FROM Matricula matricula
                                    JOIN FETCH matricula.matrizCurricular matriz
                                    JOIN FETCH matriz.disciplina
                                    JOIN FETCH matriz.professor
                                    JOIN FETCH matriz.horario
                                    WHERE 
                                        matricula.aluno.keycloakId = :keycloakId
                                    ORDER BY 
                                        matricula.dataMatricula DESC
                                """, Matricula.class)
                .setParameter("keycloakId", idKeycloakAluno)
                .getResultList();
    }

    public List<Matricula> buscarPorIdMatriz(Long idMatriz) {
        return getEntityManager()
                .createQuery("""
                                    SELECT 
                                        matricula 
                                    FROM Matricula matricula
                                    JOIN FETCH matricula.aluno aluno
                                    JOIN FETCH aluno.curso
                                    WHERE 
                                        matricula.matrizCurricular.id = :matrizId
                                """, Matricula.class)
                .setParameter("matrizId", idMatriz)
                .getResultList();
    }
}

package br.com.diego.soares.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import br.com.diego.soares.entity.MatrizCurricular;
import br.com.diego.soares.enums.PeriodoEnum;
import jakarta.persistence.LockModeType;
import jakarta.persistence.TypedQuery;
import java.time.LocalTime;
import java.util.Optional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class MatrizCurricularRepository implements PanacheRepository<MatrizCurricular> {

    public List<MatrizCurricular> buscarAtivasDoCoordenador(String idKeycloak, LocalTime horaInicio, LocalTime horaFim, PeriodoEnum periodo, Long cursoId, Integer quantidadeMaxima) {
        StringBuilder jpql = new StringBuilder("""
                                                    SELECT DISTINCT matriz FROM MatrizCurricular matriz
                                                    JOIN FETCH matriz.disciplina
                                                    JOIN FETCH matriz.professor
                                                    JOIN FETCH matriz.horario horario
                                                    LEFT JOIN FETCH matriz.cursosAutorizados
                                                    WHERE matriz.coordenador.keycloakId = :keycloakId
                                                      AND matriz.ativo = true
                                               """);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("keycloakId", idKeycloak);

        if (horaInicio != null) {
            jpql.append(" AND horario.horaInicio >= :horaInicio");
            parametros.put("horaInicio", horaInicio);
        }

        if (horaFim != null) {
            jpql.append(" AND horario.horaFim <= :horaFim");
            parametros.put("horaFim", horaFim);
        }

        if (periodo != null) {
            LocalTime[] faixa = periodo.faixaHorario();
            jpql.append(" AND horario.horaInicio >= :periodoInicio AND horario.horaInicio < :periodoFim");
            parametros.put("periodoInicio", faixa[0]);
            parametros.put("periodoFim", faixa[1]);
        }

        if (cursoId != null) {
            jpql.append(" AND :cursoId IN (SELECT curso.id FROM matriz.cursosAutorizados curso)");
            parametros.put("cursoId", cursoId);
        }

        if (quantidadeMaxima != null) {
            jpql.append(" AND matriz.quantidadeMaximaAlunos <= :quantidadeMaxima");
            parametros.put("quantidadeMaxima", quantidadeMaxima);
        }

        TypedQuery<MatrizCurricular> consulta = getEntityManager().createQuery(jpql.toString(), MatrizCurricular.class);
        parametros.forEach(consulta::setParameter);
        return consulta.getResultList();
    }

    public Optional<MatrizCurricular> buscarAtivaDoCoordenadorPorId(Long idMatriz, String idKeycloak) {
        return find("id = ?1 and ativo = true and coordenador.keycloakId = ?2", idMatriz, idKeycloak).firstResultOptional();
    }

    public MatrizCurricular buscarPorIdParaAtualizacao(Long idMatriz) {
        return findById(idMatriz, LockModeType.PESSIMISTIC_WRITE);
    }

    public boolean existeOfertaAtivaDaDisciplinaNoHorario(Long idDisciplina, Long idHorario, Long idMatrizIgnorada) {
        String consulta = "disciplina.id = ?1 and horario.id = ?2 and ativo = true";

        if (idMatrizIgnorada != null) {
            return count(consulta + " and id <> ?3", idDisciplina, idHorario, idMatrizIgnorada) > 0;
        }
        return count(consulta, idDisciplina, idHorario) > 0;
    }

    public List<MatrizCurricular> buscarAulasDisponiveisParaCurso(Long idCurso) {
        return getEntityManager()
                .createQuery("""
                                    SELECT DISTINCT matriz FROM MatrizCurricular matriz
                                    JOIN FETCH matriz.disciplina
                                    JOIN FETCH matriz.professor
                                    JOIN FETCH matriz.horario
                                    JOIN matriz.cursosAutorizados curso
                                    WHERE matriz.ativo = true AND curso.id = :cursoId
                                 """, MatrizCurricular.class)
                .setParameter("cursoId", idCurso)
                .getResultList();
    }

}

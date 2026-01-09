package pe.interseguro.siv.common.persistence.db.mysql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudDpsPregunta;

public interface SolicitudDpsPreguntaRepository extends JpaRepository<SolicitudDpsPregunta, Long> {
	@Query(
			value =
				"select    p " +
				"from 	   SolicitudDpsPregunta p " +
				"where 	   p.solicitudDps.solicitud.idSolicitud = :idSolicitud "
		)
		List<SolicitudDpsPregunta> findByIdSolicitud(@Param("idSolicitud") Long idSolicitud);
}

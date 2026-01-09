package pe.interseguro.siv.common.persistence.db.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudDps;

public interface SolicitudDpsRepositorioRepository extends JpaRepository<SolicitudDps, Long> {

	@Query(
			value =
				"select    s " + 
				"from 	   SolicitudDps s " + 
				"where 	   s.solicitud.idSolicitud = :idSolicitud " + 
				"  "
		)
	public SolicitudDps findByIdSolicitud(@Param("idSolicitud") Long idSolicitud);
	
}

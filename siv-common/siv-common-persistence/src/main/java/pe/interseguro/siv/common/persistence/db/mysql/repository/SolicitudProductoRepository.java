package pe.interseguro.siv.common.persistence.db.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudProducto;

public interface SolicitudProductoRepository extends JpaRepository<SolicitudProducto, Long> {
	@Query(
			value =
				"select    s " + 
				"from 	   SolicitudProducto s " + 
				"where 	   s.solicitud.idSolicitud = :idSolicitud " + 
				"  "
		)
	public SolicitudProducto findByIdSolicitud(@Param("idSolicitud") Long idSolicitud);
}

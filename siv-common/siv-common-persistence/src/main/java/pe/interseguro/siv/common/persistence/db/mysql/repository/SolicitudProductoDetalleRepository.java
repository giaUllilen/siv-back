package pe.interseguro.siv.common.persistence.db.mysql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudProductoDetalle;

public interface SolicitudProductoDetalleRepository extends JpaRepository<SolicitudProductoDetalle, Long> {
	@Query(
			value =
				"select    p " + 
				"from 	   SolicitudProductoDetalle p " + 
				"where 	   p.solicitudProducto.solicitud.idSolicitud = :idSolicitud "
		)
		public List<SolicitudProductoDetalle> findByIdSolicitud(@Param("idSolicitud") Long idSolicitud);

	
}

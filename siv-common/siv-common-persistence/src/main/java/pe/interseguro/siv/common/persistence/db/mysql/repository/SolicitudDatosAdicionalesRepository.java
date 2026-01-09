package pe.interseguro.siv.common.persistence.db.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudDatosAdicionales;

public interface SolicitudDatosAdicionalesRepository extends JpaRepository<SolicitudDatosAdicionales, Long> {
    @Query(nativeQuery = true, value = "SELECT s.* FROM solicitud_datos_adicionales s WHERE s.id_solicitud = :idSolicitud ORDER BY s.id_solicitud_datos_adicionales desc limit 1")
	public SolicitudDatosAdicionales findByIdSolicitud(@Param("idSolicitud") Long idSolicitud);	
}

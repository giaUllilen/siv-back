package pe.interseguro.siv.common.persistence.db.mysql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudExigenciasMedicas;

public interface SolicitudExigenciasMedicassRepository extends JpaRepository<SolicitudExigenciasMedicas, Long> {
	
    @Query(nativeQuery = true, value = "SELECT s.* FROM solicitud_exigencias_medicas s WHERE s.id_solicitud = :idSolicitud ORDER BY s.id_solicitud_exigencias_medicas desc limit 1")
	public List<SolicitudExigenciasMedicas> findByIdSolicitud(@Param("idSolicitud") Long idSolicitud);	
}

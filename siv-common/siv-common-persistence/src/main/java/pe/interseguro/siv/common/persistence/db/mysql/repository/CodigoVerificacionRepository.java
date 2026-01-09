package pe.interseguro.siv.common.persistence.db.mysql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.interseguro.siv.common.persistence.db.mysql.domain.CodigoVerificacion;

public interface CodigoVerificacionRepository extends JpaRepository<CodigoVerificacion, Long> {

	@Query(
			value =
				"select    cv " + 
				"from 	   CodigoVerificacion cv " + 
				"where 	   cv.idSolicitud = :idSolicitud " +
				"AND 	   cv.idPersona = :idPersona " +
				"AND 	   cv.activo  = 1 "+
				"AND 	   cv.fechaExpiracion >= CURRENT_TIMESTAMP " +
				"ORDER BY cv.idCodigoVerificacion"
		)
	public List<CodigoVerificacion> findCodigoActivo(@Param("idSolicitud") Long idSolicitud, @Param("idPersona") Long idPersona);
	
	@Modifying(clearAutomatically = true)
	@Query(value = "update CodigoVerificacion cv set cv.activo = 0, cv.usuarioModif = CURRENT_TIMESTAMP where cv.activo = 1 AND cv.fechaExpiracion < CURRENT_TIMESTAMP")
	int updateExpired();
	/*
	@Query(
			value =
				"select    cv " + 
				"from 	   CodigoVerificacion cv " + 
				"where 	   cv.idSolicitud = :idSolicitud " +
				"AND 	   cv.idPersona = :idPersona " +
				"AND 	   cv.enviado  = 1 "+
				"AND 	   cv.usado  = 1 "+
				"AND 	   cv.activo  = 0 " + 
				"ORDER BY cv.fechaUsado DESC "
		)
	*/
	@Query(nativeQuery = true, value = "SELECT * FROM codigo_verificacion where id_solicitud = :idSolicitud and id_persona = :idPersona and enviado = 1 and usado = 1 and activo = 0 order by fecha_usado desc limit 1")
	public CodigoVerificacion findCodigoFirmado(@Param("idSolicitud") Long idSolicitud, @Param("idPersona") Long idPersona);
}

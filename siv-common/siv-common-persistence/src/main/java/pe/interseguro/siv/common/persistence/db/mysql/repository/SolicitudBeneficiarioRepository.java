package pe.interseguro.siv.common.persistence.db.mysql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudBeneficiario;

public interface SolicitudBeneficiarioRepository extends JpaRepository<SolicitudBeneficiario, Long>{
	@Query(
			value =
				"select    b " + 
				"from 	   SolicitudBeneficiario b " + 
				"where 	   b.solicitud.idSolicitud = :idSolicitud "
		)
		public List<SolicitudBeneficiario> findByIdSolicitud(@Param("idSolicitud") Long idSolicitud);	
}

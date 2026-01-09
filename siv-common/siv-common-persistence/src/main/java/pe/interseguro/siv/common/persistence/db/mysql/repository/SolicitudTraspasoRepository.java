package pe.interseguro.siv.common.persistence.db.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudTraspaso;

public interface SolicitudTraspasoRepository extends JpaRepository<SolicitudTraspaso, Long>  {

	@Query(
			value =
				"select    s " + 
				"from 	   SolicitudTraspaso s " + 
				"where 	   s.numPropuestaOriginal = :numPropuestaOriginal " + 
				"  "
		)
	public SolicitudTraspaso findByPropuesta(@Param("numPropuestaOriginal") String numPropuestaOriginal);
}

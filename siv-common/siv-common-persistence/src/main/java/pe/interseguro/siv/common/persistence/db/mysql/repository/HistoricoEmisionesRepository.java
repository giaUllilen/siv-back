package pe.interseguro.siv.common.persistence.db.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.interseguro.siv.common.persistence.db.mysql.domain.HistoricoEmisiones;

public interface HistoricoEmisionesRepository extends JpaRepository<HistoricoEmisiones, Long> {
	@Query(
			value =
				"select    h " + 
				"from 	   HistoricoEmisiones h " + 
				"where 	   h.numeroPropuesta = :numeroPropuesta " + 
				"  "
		)
	public HistoricoEmisiones findByNumeroPropuesta(@Param("numeroPropuesta") String numeroPropuesta);
	

}
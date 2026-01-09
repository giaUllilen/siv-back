package pe.interseguro.siv.common.persistence.db.mysql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.interseguro.siv.common.persistence.db.mysql.domain.Poliza;

public interface PolizaRepository extends JpaRepository<Poliza, Integer>{

	@Query(
			value =
				"select    p " + 
				"from 	   Poliza p " + 
				"where 	   p.tipoDocumento = :tipoDoc and p.numeroDocumento = :numeroDoc "
			)
	public List<Poliza> findByPoliza(@Param("tipoDoc") String tipoDoc ,@Param("numeroDoc") String numeroDoc);
	
	@Query(
			value =
				"select    p " + 
				"from 	   Poliza p " + 
				"where 	   p.numeroPoliza = :nroPoliza "
			)
	public Poliza findByNroPoliza(@Param("nroPoliza") String nroPoliza);
}

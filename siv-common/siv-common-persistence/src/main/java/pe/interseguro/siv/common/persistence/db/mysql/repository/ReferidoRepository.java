package pe.interseguro.siv.common.persistence.db.mysql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.interseguro.siv.common.persistence.db.mysql.domain.Referido;

public interface ReferidoRepository extends JpaRepository<Referido, Long> {

	@Query(
		value =
			"select    r " + 
			"from 	   Referido r " + 
			"where 	   r.persona.idPersona = :idPersona "
		)
		public List<Referido> findByIdPersona(@Param("idPersona") Long idPersona);
}
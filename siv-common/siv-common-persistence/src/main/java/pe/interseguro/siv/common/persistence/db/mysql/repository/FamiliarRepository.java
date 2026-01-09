package pe.interseguro.siv.common.persistence.db.mysql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.interseguro.siv.common.persistence.db.mysql.domain.Familiar;

public interface FamiliarRepository extends JpaRepository<Familiar, Long> {
	
	@Query(
			value =
				"select    f " + 
				"from 	   Familiar f " + 
				"where 	   f.persona.idPersona = :idPersona "
		)
		public List<Familiar> findByIdPersona(@Param("idPersona") Long idPersona);	
}

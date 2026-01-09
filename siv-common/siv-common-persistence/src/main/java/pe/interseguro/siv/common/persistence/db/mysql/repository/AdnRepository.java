package pe.interseguro.siv.common.persistence.db.mysql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.interseguro.siv.common.persistence.db.mysql.domain.Adn;

public interface AdnRepository extends JpaRepository<Adn, Long> {
	/*
	@Query(
			value =
				"select    a " + 
				"from 	   Adn a " + 
				"where 	   a.persona.idPersona = :idPersona "
		)*/
	@Query(nativeQuery = true, value = "SELECT a.* FROM adn a WHERE a.id_persona = :idPersona ORDER BY a.id_adn DESC LIMIT 1")
		public Adn findByIdPersona(@Param("idPersona") Long idPersona);	
	
	@Query(
			value =
				"select    a " + 
				"from 	   Adn a " + 
				"where 	   a.persona.tipoDocumento = :tipoDocumento " +
				"  and	   a.persona.numeroDocumento = :numeroDocumento " + 
				"  "
		)
	
		public Adn findByTipoNumeroDocumento(@Param("tipoDocumento") String tipoDocumento, @Param("numeroDocumento") String numeroDocumento);	
	
	@Query(
			value =
				"select    a " + 
				"from 	   Adn a " +
				"inner join fetch a.persona p " +
				"where 	   a.persona.tipoDocumento = :tipoDocumento " +
				"  and	   a.persona.numeroDocumento = :numeroDocumento " + 
				"  "
		)
	
		public Adn LdpdpfindByTipoNumeroDocumento(@Param("tipoDocumento") String tipoDocumento, @Param("numeroDocumento") String numeroDocumento);	
	
}

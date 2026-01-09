package pe.interseguro.siv.common.persistence.db.mysql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.interseguro.siv.common.persistence.db.mysql.domain.Familiar;
import pe.interseguro.siv.common.persistence.db.mysql.domain.PlanFuturo;

public interface PlanFuturoRepository extends JpaRepository<PlanFuturo, Long> {
	/*
	@Query(
			value =
				"select    plan " + 
				"from 	   PlanFuturo plan " + 
				"inner join plan.persona per  " + 
				"where 	   per.idPersona = :idPersona " + 
				"  "
		)
	*/
	@Query(nativeQuery = true, value = "SELECT p.* FROM plan_futuro p WHERE p.id_persona = :idPersona ORDER BY p.id_plan_futuro DESC LIMIT 1")
	public PlanFuturo findByIdPersona(@Param("idPersona") Long idPersona);

	
}

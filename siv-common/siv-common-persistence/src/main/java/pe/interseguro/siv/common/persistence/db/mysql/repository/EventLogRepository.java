package pe.interseguro.siv.common.persistence.db.mysql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.interseguro.siv.common.persistence.db.mysql.domain.EventLog;

public interface EventLogRepository extends JpaRepository<EventLog, Long> {
	
	/*@Query(
			value =
				"select    e.* " + 
				"from 	   event_log e " + 
				"where 	   " +
				"DATE_FORMAT(e.event_date,'%d-%M-%Y') = DATE_FORMAT(NOW(), '%d-%M-%Y')" + 
				"  ", nativeQuery = true
		)
	public List<EventLog> findByAgente();
	
	@Query(
			value =
				"select    e.* " + 
				"from 	   event_log e " + 
				"where 	   e.agent_register = :agente " +
				"and DATE_FORMAT(e.event_date,'%d-%M-%Y') = DATE_FORMAT(NOW(), '%d-%M-%Y')" + 
				"  ", nativeQuery = true
		)
	public List<EventLog> findByAgente(@Param("agente") String agente);*/
}

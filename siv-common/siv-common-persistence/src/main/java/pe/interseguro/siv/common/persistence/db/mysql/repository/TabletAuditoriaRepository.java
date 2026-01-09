package pe.interseguro.siv.common.persistence.db.mysql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.interseguro.siv.common.persistence.db.mysql.domain.TabletAuditoria;

public interface TabletAuditoriaRepository extends JpaRepository<TabletAuditoria, Long> {

	@Query(
			value =
				"select    p " + 
				"from 	   TabletAuditoria p " + 
				"where 	   p.ip = :ip " + 
				"order by p.time desc"
		)
	public List<TabletAuditoria> findByIPTablet(@Param("ip") String ip);
}

package pe.interseguro.siv.common.persistence.db.mysql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.interseguro.siv.common.persistence.db.mysql.domain.Multitabla;

public interface MultitablaRepository extends JpaRepository<Multitabla, Long> {
	
	
	@Query(
		value =
			"select    m " + 
			"from 	   Multitabla m " + 
			"where 	   m.codigoTabla = :codigoTabla " + 
			"  and	   m.estadoTabla = '1' " + 
			"  and	   m.estado = '1' " + 
			"  and	   m.codigo is not null " +
			"order by  m.orden  "
	)
	public List<Multitabla> findByCodigoTabla(@Param("codigoTabla") String codigoTabla);
	
	@Query(
			value =
				"select    m " + 
				"from 	   Multitabla m " + 
				"where 	   m.codigoTabla = :codigoTabla " + 
				"  and	   m.estadoTabla = '1' " + 
				"  and	   m.codigo is not null " +
				"order by  m.orden  "
		)
	public List<Multitabla> findAllByCodigoTabla(@Param("codigoTabla") String codigoTabla);
	
	@Query(
			value =
				"select    m " + 
				"from 	   Multitabla m " + 
				"where 	   m.codigoTabla = :codigoTabla " + 
				"  and	   m.estadoTabla = '1' " + 
				"  and	   m.codigo is not null "
		)
	public List<Multitabla> findByCodigoTablaSinEstado(@Param("codigoTabla") String codigoTabla);

	Multitabla findByCodigoAndCodigoTabla(String codigo,String codigoTabla);
}

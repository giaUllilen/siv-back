package pe.interseguro.siv.common.persistence.db.mysql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudViaCobro;

public interface SolicitudViaCobroRepository extends JpaRepository<SolicitudViaCobro, Long> {
	@Query(
		value =
			"select    v " + 
			"from 	   SolicitudViaCobro v " + 
			"where 	   v.tipoViaCobro = :idTipoVia "
		)
		public List<SolicitudViaCobro> findByTipoVia(@Param("idTipoVia") Integer idTipoVia);
	
	@Query(
			value =
				"select    v " + 
				"from 	   SolicitudViaCobro v " + 
				"where 	   v.codigoViaCobro = :codigoViaCobro "
			)
			public List<SolicitudViaCobro> findByViaCobro(@Param("codigoViaCobro") Integer codigoViaCobro);
}

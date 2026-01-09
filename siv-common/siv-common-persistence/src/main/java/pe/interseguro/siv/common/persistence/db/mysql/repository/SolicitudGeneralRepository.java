package pe.interseguro.siv.common.persistence.db.mysql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.interseguro.siv.common.persistence.db.mysql.domain.CotizacionGeneralResponse;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Solicitud;

@Repository
public interface SolicitudGeneralRepository extends JpaRepository<Solicitud, Long> {
	
	@Query(
			value = 
					"select new pe.interseguro.siv.common.persistence.db.mysql.domain.CotizacionGeneralResponse(s.numeroCotizacion,DATE_FORMAT(s.fechaSolicitud, '%d/%m/%Y')," +
							"(select concat(nombres,' ',apellidoPaterno,' ',apellidoMaterno)from Persona WHERE numeroDocumento=:documento),sp.tipoProducto," +
							"s.idPlan,s.nombrePlan,s.subplan,(select valor from Multitabla where nombreTabla='Frecuencia' and codigo=sp.frecuencia)," +
							"(select valorAux from Multitabla where nombreTabla='Moneda' and codigo=s.moneda)," +
							"(select valorCrm from Multitabla where nombreTabla='Moneda' and codigo=s.moneda)," +
							"(select numeroDocumento from Persona WHERE numeroDocumento=:documento)," +
							"(select valor from Multitabla where nombreTabla='Sub Plan Cotizador' and codigo=s.subplan),concat(sp.primaComercial,'')," +
							"concat(sp.primaComercialAnual,''),s.moneda,s.numeroCotizacionOrigen,s.estado, s.estadoGeneral, s.fechaRegistroSamp,'','' )" +
							"from SolicitudProducto sp " +
							"inner join sp.solicitud s " +
							"WHERE s.idAsegurado in (SELECT p.idPersona FROM Persona p where p.numeroDocumento=:documento) and sp.tipoProducto in (1,2) " +
		                    "and STR_TO_DATE(sp.fechaCotizacion,'%d/%m/%Y') >= STR_TO_DATE(:fecha,'%d/%m/%Y' )"
	)
	public List<CotizacionGeneralResponse> findCotizacionGeneralbyDocumento(@Param("documento") String documento, @Param("fecha") String fecha);

}

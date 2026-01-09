package pe.interseguro.siv.common.persistence.db.mysql.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pe.interseguro.siv.common.persistence.db.mysql.domain.Solicitud;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudFiltrado;

@Repository
public interface SolicitudFiltradoRepository extends JpaRepository<Solicitud, Long> {

	@Query(
		value = 
			"select new pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudFiltrado(p.tipoDocumento, " +
			"p.numeroDocumento, s.estado, p.nombres, p.apellidoPaterno, p.apellidoMaterno,p.correo, p.razonSocial) " +
			"from Solicitud s " + 
			"inner join Persona p on s.idAsegurado = p.idPersona " + 
			"where s.numeroCotizacion = :numeroCotizacion "
	)
	public SolicitudFiltrado findByNumeroCotizacion(@Param("numeroCotizacion") String numeroCotizacion);

}

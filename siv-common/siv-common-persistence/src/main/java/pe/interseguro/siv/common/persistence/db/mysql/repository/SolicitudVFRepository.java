package pe.interseguro.siv.common.persistence.db.mysql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.interseguro.siv.common.persistence.db.mysql.domain.Solicitud;
import pe.interseguro.siv.common.persistence.rest.vidafree.response.CotizacionResponse;

@Repository
public interface SolicitudVFRepository  extends JpaRepository<Solicitud, Long> {


    @Query(
            value =
                    "select  new pe.interseguro.siv.common.persistence.rest.vidafree.response.CotizacionResponse(s.numeroCotizacion,DATE_FORMAT(s.fechaSolicitud, '%d/%m/%Y')," +
                            "'Vida Free',s.nombrePlan," +
                            "(select valor from Multitabla where nombreTabla='Frecuencia' and codigo=sp.frecuencia)," +
                            "concat(sp.primaComercial,''),concat(sp.primaComercialAnual,''),s.moneda, s.numeroCotizacionOrigen," +
                            "s.estado, s.fechaRegistroSamp, s.estadoGeneral)  " +
                            "from SolicitudProducto sp " +
                            "inner join sp.solicitud s " +
                            "WHERE s.idAsegurado in (SELECT p.idPersona FROM Persona p where p.numeroDocumento=:documento) and sp.tipoProducto=2" +
                            "and STR_TO_DATE(DATE_FORMAT(s.fechaSolicitud, '%d/%m/%Y'),'%d/%m/%Y') >= STR_TO_DATE(:fecha,'%d/%m/%Y' )"
    )
    public List<CotizacionResponse> findCotizacionVFbyDocumento(@Param("documento") String documento, @Param("fecha") String fecha);

}

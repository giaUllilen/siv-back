package pe.interseguro.siv.common.persistence.db.mysql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.interseguro.siv.common.persistence.db.mysql.domain.CotizacionPG;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Solicitud;

@Repository
public interface SolicitudPGRepository  extends JpaRepository<Solicitud, Long> {


    @Query(
            value =
                    "select  new pe.interseguro.siv.common.persistence.db.mysql.domain.CotizacionPG(s.numeroCotizacion,DATE_FORMAT(s.fechaSolicitud, '%d/%m/%Y'),'64899'," +
                            "(select concat(nombres,' ',apellidoPaterno,' ',apellidoMaterno)from Persona WHERE numeroDocumento=:documento)," +
                            "'Plan Garantizado'," +
                            "s.idPlan,s.nombrePlan,(select valor from Multitabla where nombreTabla='Frecuencia' and codigo=sp.frecuencia)," +
                            "(select valorAux from Multitabla where nombreTabla='Moneda' and codigo=s.moneda)," +
                            "(select valorCrm from Multitabla where nombreTabla='Moneda' and codigo=s.moneda)," +
                            "concat(sp.primaComercialAnual,'')," +
                            "concat(sp.primaComercial,''),(select numeroDocumento from Persona WHERE numeroDocumento=:documento),'',s.subplan," +
                            "(select valor from Multitabla where nombreTabla='Sub Plan Cotizador' and codigo=s.subplan),'','', s.numeroCotizacionOrigen," +
                            "s.estado, s.estadoGeneral, s.fechaRegistroSamp)" +
                            "from SolicitudProducto sp " +
                            "inner join sp.solicitud s " +
                            "WHERE s.idAsegurado in (SELECT p.idPersona FROM Persona p where p.numeroDocumento=:documento) and sp.tipoProducto=1 " +
                            "and STR_TO_DATE(sp.fechaCotizacion,'%d/%m/%Y') >= STR_TO_DATE(:fecha,'%d/%m/%Y' )  "
    )
    public List<CotizacionPG> findCotizacionesPGbyDocumento(@Param("documento") String documento, @Param("fecha") String fecha);

}

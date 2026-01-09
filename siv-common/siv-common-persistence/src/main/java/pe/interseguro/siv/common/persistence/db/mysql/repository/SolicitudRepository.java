package pe.interseguro.siv.common.persistence.db.mysql.repository;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.interseguro.siv.common.dto.response.LinkPagoCotResponseDTO;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Solicitud;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudAgente;
import pe.interseguro.siv.common.persistence.db.postgres.bean.Cotizacion;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

	@Query(
			value =
				"select    s " + 
				"from 	   Solicitud s " + 
				"where 	   s.idCircuitoFirma = :idCircuitoFirma " + 
				"  "
		)
	public Solicitud findByIdCircuitoFirma(@Param("idCircuitoFirma") String idCircuitoFirma);
	

	Solicitud findByNumeroCotizacion( String numeroCotizacion);
	
	@Query(
			value =
				"select    s " + 
				"from 	   SolicitudAgente s " + 
				"  "
		)
	public List<SolicitudAgente> listAgentesValidados();

	/*@Query(
			value= "SELECT s.numeroCotizacion, " +
			"concat('https://www.interseguro.pe/siv-web/zonasegura/pago?cliente=', s.tokenOnline) as linkPago, " +
			"concat('https://www.interseguro.pe/siv-web/zonasegura/pagoRecargo?cliente=', s.tokenAdnRecargo) as linkRecargo " +
			"FROM Solicitud s " +
			"WHERE s.numeroCotizacion = :cotizacion " +
	" "
	)
	@Results(value = {
			@Result(column = "nro_cotizacion", property = "numeroCotizacion"),
			@Result(column = "link_pago", property = "numeroPropuesta"),
			@Result(column = "link_recargo", property = "numeroPoliza")
	})
	public Solicitud listaLinkPago(@Param("cotizacion") String cotizacion);*/
	@Query(value = "SELECT numero_cotizacion, " +
			"token_online as link_pago, " +
			"token_adn_recargo as link_recargo " +
			"FROM solicitud s " +
			"WHERE numero_cotizacion = :nroCotizacion", nativeQuery = true)
	List<Object[]> listaLinkPago(@Param("nroCotizacion") String nroCotizacion);


	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update solicitud set documento_cotizacion = :documentoCotizacion , documento_solicitud  = :documentoSolcitud where id_solicitud = :idSolicitud")
	public void updateSolcitud(@Param("documentoCotizacion") String documentoCotizacion, @Param("documentoSolcitud") String documentoSolcitud, @Param("idSolicitud") Long idSolicitud);
	
	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update solicitud  set estado_propuesta = :estadoPropuesta , estado = :estado , alerta_plaft  = :alertaPlaft where id_solicitud = :idSolicitud")
	public void updateSolcitudEvaluacion(@Param("estadoPropuesta") Integer estadoPropuesta, @Param("estado") String estado, @Param("alertaPlaft") String alertaPlaft, @Param("idSolicitud") Long idSolicitud);
	
	
	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update solicitud  set estado_pubsubsend = :estadoPubSubSend  where id_solicitud = :idSolicitud")
	public void updateSolcitudPubSubSend(@Param("estadoPubSubSend") String estadoPubSubSend,  @Param("idSolicitud") Long idSolicitud);
	
	
	@Modifying
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Query(nativeQuery = true, value = "update solicitud  set numero_poliza = :numeroPoliza , estado_propuesta  = :estadoPropuesta, estado_general = :estadoGeneral , fecha_modif = :fechaModific , fecha_emision = :fechaEmision ,flg_proceso_emision = :flagProcesoEmision , estado_pubsubsend = :estadoPubSubSend where id_solicitud = :idSolicitud")
	public void updateSolcitudPolicyIssued(@Param("numeroPoliza") String numeroPoliza, @Param("estadoPropuesta") Integer estadoPropuesta,@Param("estadoGeneral") String estadoGeneral, @Param("fechaModific") String  fechaModific,@Param("fechaEmision") String fechaEmision,@Param("flagProcesoEmision") String flagProcesoEmision,@Param("estadoPubSubSend") String estadoPubSubSend, @Param("idSolicitud") Long idSolicitud);
	
	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update solicitud  set flg_proceso_emision = :flagProcesoEmision where id_solicitud = :idSolicitud")
	public void updateSolcitudReprocesoEision(@Param("flagProcesoEmision") String flagProcesoEmision, @Param("idSolicitud") Long idSolicitud);
	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update solicitud  set nombre_archivo_solicitud = :nombreArchivoSolicitud where id_solicitud = :idSolicitud")
	public void updateNombreArchivoSolicitud(@Param("nombreArchivoSolicitud") String nombreArchivoSolicitud, @Param("idSolicitud") Long idSolicitud);

	@Modifying
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Query(nativeQuery = true, value = "update solicitud  set documento_acp = :nombreDocumentoAcp where id_solicitud = :idSolicitud")
	public void updateNombreDocumentoAcp(@Param("nombreDocumentoAcp") String nombreDocumentoAcp, @Param("idSolicitud") Long idSolicitud);
}

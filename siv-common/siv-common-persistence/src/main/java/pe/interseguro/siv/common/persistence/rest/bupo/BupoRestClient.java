package pe.interseguro.siv.common.persistence.rest.bupo;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import pe.interseguro.siv.common.exception.SivSOAException;
import pe.interseguro.siv.common.persistence.rest.base.BaseRestClientImpl;
import pe.interseguro.siv.common.persistence.rest.bupo.request.AsignarProspectoRequest;
import pe.interseguro.siv.common.persistence.rest.bupo.request.CrearProspectoRequest;
import pe.interseguro.siv.common.persistence.rest.bupo.request.ObtenerDatosAgenteByNombreRequest;
import pe.interseguro.siv.common.persistence.rest.bupo.request.ObtenerDatosAgenteRequest;
import pe.interseguro.siv.common.persistence.rest.bupo.request.ValidarProspectoAsignacionRequest;
import pe.interseguro.siv.common.persistence.rest.bupo.response.AsignarProspectoResponse;
import pe.interseguro.siv.common.persistence.rest.bupo.response.CrearProspectoResponse;
import pe.interseguro.siv.common.persistence.rest.bupo.response.ObtenerDatosAgenteResponse;
import pe.interseguro.siv.common.persistence.rest.bupo.response.ValidarProspectoAsignacionResponse;
import pe.interseguro.siv.common.persistence.rest.crm.request.CrearBeneficiariosRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.CrmActualizarSitcRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.CrmCotizacionRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.CrmOportunidadCotizacionRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.DatoContactoRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.GetEstadoCotizacionRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.GetIdOportunidadRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.TokenCotizadorRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.UpdateContactoRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.UpdateCotizacionRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.UpdateFormatoDPSRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.UpdateRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.UploadArchivoRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.UrlCotizadorNuevoRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.UrlRecotizarVidaRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.ValidarAccesoRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.ValidarCreacionAsignacionDetRequest;
import pe.interseguro.siv.common.persistence.rest.crm.response.CrmCotizacionResponse;
import pe.interseguro.siv.common.persistence.rest.crm.response.CrmIntermedioResponse;
import pe.interseguro.siv.common.persistence.rest.crm.response.DatoContactoResponse;
import pe.interseguro.siv.common.persistence.rest.crm.response.GenericoResponse;
import pe.interseguro.siv.common.persistence.rest.crm.response.GetEstadoCotizacionResponse;
import pe.interseguro.siv.common.persistence.rest.crm.response.GetIdOportunidadResponse;
import pe.interseguro.siv.common.persistence.rest.crm.response.TokenCotizadorResponse;
import pe.interseguro.siv.common.persistence.rest.crm.response.UpdateResponse;
import pe.interseguro.siv.common.persistence.rest.crm.response.UploadArchivoResponse;
import pe.interseguro.siv.common.persistence.rest.crm.response.ValidarAccesoResponse;
import pe.interseguro.siv.common.persistence.rest.crm.response.ValidarCreacionAsignacionResponse;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.Utilitarios;


/**
 * @author digital-is
 *
 */
@Component
public class BupoRestClient extends BaseRestClientImpl {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageSource messageSource;

    @Value("#{ environment['persistence.rest.client.bupo.prospecto.servidor'] }")
    private String servidorApiBupoProspecto;
    
    @Value("#{ environment['persistence.rest.client.bupo.prospecto.ValidarProspectoAsignacion'] }")
    private String bupoProspectoValidarProspectoAsignacion;
    
    @Value("#{ environment['persistence.rest.client.bupo.agente.servidor'] }")
    private String servidorApiBupoAgente;
    
    @Value("#{ environment['persistence.rest.client.bupo.agente.ObtenerDatosAgente'] }")
    private String bupoObtenerDatosAgente;
    
    @Value("#{ environment['persistence.rest.client.bupo.prospecto.AsginarProspecto'] }")
    private String bupoAsignarProspecto;
    
    @Value("#{ environment['persistence.rest.client.bupo.prospecto.CrearProspecto'] }")
    private String bupoCrearProspecto;
    
    
    private String getUrlBupoValidarProspectoAsignacion() {
		return servidorApiBupoProspecto+bupoProspectoValidarProspectoAsignacion;
	}
    
    private String getUrlObtenerDatosAgente() {
		return servidorApiBupoAgente+bupoObtenerDatosAgente;
	}
    
    private String getUrlBupoAsignarProspecto() {
		return servidorApiBupoProspecto+bupoAsignarProspecto;
	}
    
    private String getUrlBupoCrearProspecto() {
		return servidorApiBupoProspecto+bupoCrearProspecto;
	}
	
	/**
     * Validar la asignación
     * 
     * @param request
     * @return
     */
    public ValidarProspectoAsignacionResponse validarProspectoAsignacion(ValidarProspectoAsignacionRequest request) {
    	LOGGER.info("Entro BupoRestClient#validarProspectoAsignacion(request)");
    	
    	ValidarProspectoAsignacionResponse response = null;
    	
		try {
			String url = getUrlBupoValidarProspectoAsignacion().concat("/").concat(request.getTipoDocumentoProspecto()).concat("-").concat(request.getNumeroDocumentoProspecto()).concat("/").concat(request.getTipoDocumentoAgente()).concat("-").concat(request.getNumeroDocumentoAgente());
			//obtenerDatosClienteResponse = obtenerGetPorObjeto(url, ObtenerDatosClienteResponse.class, headers);
			response = obtenerGetPorObjeto(url, ValidarProspectoAsignacionResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_BUPO_VALIDAR_PROSPECTO_ASIGNACION).concat( Objects.toString(e.getMessage(), "") ), 
				e.getErrors()
			);
		}
    	
		LOGGER.info("Salio BupoRestClient#validarProspectoAsignacion(request)");
    	return response;
    }
    
    /**
     * Obtiene Datos Agente
     * 
     * @param request
     * @return
     */
    public ObtenerDatosAgenteResponse obtenerDatosAgentePorEmail(ObtenerDatosAgenteRequest request) {
    	LOGGER.info("Entro BupoRestClient#obtenerDatosAgentePorEmail(request)");
    	
    	ObtenerDatosAgenteResponse response = null;
    	
		try {
			String url = getUrlObtenerDatosAgente().concat("/?email=").concat(request.getEmail());
			response = obtenerGetPorObjeto(url, ObtenerDatosAgenteResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_BUPO_OBTENER_AGENTE).concat( Objects.toString(e.getMessage(), "") ), 
				e.getErrors()
			);
		}
    	
		LOGGER.info("Salio BupoRestClient#obtenerDatosAgentePorEmail(request)");
    	return response;
    }
    
    
    /* START Reingeniería */
    
    /**
     * Obtiene Datos Agente
     * 
     * @param request
     * @return
     */
    public ObtenerDatosAgenteResponse obtenerDatosAgentePorNombreAndApellidoPaterno(ObtenerDatosAgenteByNombreRequest request) {
    	LOGGER.info("Entro BupoRestClient#obtenerDatosAgentePorNombreAndApellidoPaterno(request)");
    	
    	ObtenerDatosAgenteResponse response = null;
    	
		try {
			String url = getUrlObtenerDatosAgente().concat("/?nombre=").concat(request.getNombreAgente()).concat("&&apellidoPaterno").concat(request.getApellidoPaternoAgente());
			response = obtenerGetPorObjeto(url, ObtenerDatosAgenteResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_BUPO_OBTENER_AGENTE).concat( Objects.toString(e.getMessage(), "") ), 
				e.getErrors()
			);
		}
    	
		LOGGER.info("Salio BupoRestClient#obtenerDatosAgentePorNombreAndApellidoPaterno(request)");
    	return response;
    }
    
    /* END Reingeniería */
    
    
    /**
     * Asignar Prospecto
     * 
     * @param request
     * @return
     */
    public AsignarProspectoResponse asignarProspecto(AsignarProspectoRequest request) {
    	LOGGER.info("Entro BupoRestClient#asignarProspecto(request)");
    	AsignarProspectoResponse response = null;
    	
		try {
			String url = getUrlBupoAsignarProspecto().concat("/").concat(request.getTipoDocumentoProspecto()).concat("-").concat(request.getNumeroDocumentoProspecto()).concat("/").concat(request.getTipoDocumentoAgente()).concat("-").concat(request.getNumeroDocumentoAgente());
			response = obtenerPostPorObjeto(url, request, AsignarProspectoResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_BUPO_ASIGNAR_PROSPECTO).concat( Objects.toString(e.getMessage(), "") ), 
				e.getErrors()
			);
		}
    	
		LOGGER.info("Salio BupoRestClient#asignarProspecto(request)");
    	return response;
    }
    
    /**
     * Crear Prospecto en BUPO
     * 
     * @param request
     * @return
     */
    public CrearProspectoResponse crearProspecto(CrearProspectoRequest request) {
    	LOGGER.info("Entro BupoRestClient#crearProspecto(request)");
    	CrearProspectoResponse response = null;
    	
		try {
			String url = getUrlBupoCrearProspecto();
			response = obtenerPostPorObjeto(url, request, CrearProspectoResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_BUPO_CREAR_PROSPECTO).concat( Objects.toString(e.getMessage(), "") ), 
				e.getErrors()
			);
		}
    	
		LOGGER.info("Salio BupoRestClient#crearProspecto(request)");
    	return response;
    }
    
    
}

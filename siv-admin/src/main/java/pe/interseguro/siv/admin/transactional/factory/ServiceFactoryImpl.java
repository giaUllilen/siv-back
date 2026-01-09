package pe.interseguro.siv.admin.transactional.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pe.interseguro.siv.admin.transactional.service.AcpPDFService;
import pe.interseguro.siv.admin.transactional.service.AdnService;
import pe.interseguro.siv.admin.transactional.service.CotizaService;
import pe.interseguro.siv.admin.transactional.service.DescargaService;
import pe.interseguro.siv.admin.transactional.service.LogService;
import pe.interseguro.siv.admin.transactional.service.SolicitudPDFService;
import pe.interseguro.siv.admin.transactional.service.SolicitudService;
import pe.interseguro.siv.admin.transactional.service.UsuarioService;
import pe.interseguro.siv.admin.transactional.service.UtilitarioService;

/**
 * Factory intermedio entre la transaccional y la vista para solicitar a medida los servicios transaccionales
 * 
 * @author ti-is
 */
@Component("serviceFactory")
@Scope("singleton")
public class ServiceFactoryImpl implements ServiceFactory {

	@Autowired
	private AdnService adnService;
	
	@Autowired
	private AcpPDFService acpPDFService;
	
	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private CotizaService cotizaService;
	
	@Autowired
	private SolicitudService solicitudService;
	
	@Autowired
	private DescargaService descargaService;

	@Autowired
	private SolicitudPDFService solicitudPDFService;
	
	@Autowired
	private UtilitarioService utilitarioService;
	
	@Autowired
	private LogService logService;
	
	@Override
	public AdnService getAdnService() {
		return adnService;
	}

	@Override
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	@Override
	public CotizaService getCotizaService() {
		return cotizaService;
	}

	@Override
	public SolicitudService getSolicitudService() {
		return solicitudService;
	}

	@Override
	public DescargaService getDescargaService() {
		return descargaService;
	}

	@Override
	public SolicitudPDFService getSolicitudPDFService() {
		return solicitudPDFService;
	}

	@Override
	public UtilitarioService getUtilitarioService() {
		return utilitarioService;
	}

	@Override
	public AcpPDFService getAcpPDFService() {
		return acpPDFService;
	}

	@Override
	public LogService getLogService() {
		return logService;
	}
	
}

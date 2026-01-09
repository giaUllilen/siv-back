package pe.interseguro.siv.admin.transactional.factory;

import pe.interseguro.siv.admin.transactional.service.AcpPDFService;
import pe.interseguro.siv.admin.transactional.service.AdnService;
import pe.interseguro.siv.admin.transactional.service.CotizaService;
import pe.interseguro.siv.admin.transactional.service.DescargaService;
import pe.interseguro.siv.admin.transactional.service.LogService;
import pe.interseguro.siv.admin.transactional.service.SolicitudPDFService;
import pe.interseguro.siv.admin.transactional.service.SolicitudService;
import pe.interseguro.siv.admin.transactional.service.UsuarioService;
import pe.interseguro.siv.admin.transactional.service.UtilitarioService;

public interface ServiceFactory {

	public AdnService getAdnService();	
	
	public AcpPDFService getAcpPDFService();	
	
	public UsuarioService getUsuarioService();
	
	public CotizaService getCotizaService();
	
	public SolicitudService getSolicitudService();
	
	public DescargaService getDescargaService();
	
	public SolicitudPDFService getSolicitudPDFService();
	
	public UtilitarioService getUtilitarioService();
	
	public LogService getLogService();
}

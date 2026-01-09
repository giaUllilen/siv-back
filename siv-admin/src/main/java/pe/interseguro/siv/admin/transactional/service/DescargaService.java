package pe.interseguro.siv.admin.transactional.service;

import javax.servlet.http.HttpServletResponse;

public interface DescargaService {

	/**
	 * Descargar 
	 * 
	 * @param String nombreArchivo nombre del archivo temporal
	 * @return
	 */
	public void descargar(String nombreArchivo, HttpServletResponse response);

}

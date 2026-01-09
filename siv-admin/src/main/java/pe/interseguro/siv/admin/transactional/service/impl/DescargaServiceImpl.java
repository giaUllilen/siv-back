package pe.interseguro.siv.admin.transactional.service.impl;

import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import pe.interseguro.siv.admin.transactional.service.DescargaService;
import pe.interseguro.siv.admin.transactional.service.SolicitudService;
import pe.interseguro.siv.common.dto.request.PaginationRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudFiltroRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudRequestDTO;
import pe.interseguro.siv.common.dto.response.CotizaListaItemResponseDTO;
import pe.interseguro.siv.common.dto.response.PaginationResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudItemResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudResponseDTO;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Solicitud;
import pe.interseguro.siv.common.persistence.db.mysql.domain.ViewSolicitud;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.ViewSolicitudRepository;

@Service("descargaServiceImpl")
public class DescargaServiceImpl implements DescargaService {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Override
	public void descargar(String nombreArchivo, HttpServletResponse response) {
		response.setContentType("application/octet-stream");
//      response.setHeader("Content-Disposition", "attachment;filename="+nombreArchivo+Constantes.ARCHIVO_EXT_DOCX);
		response.setHeader("Content-Disposition", "attachment;filename=" + nombreArchivo);

		File file = new File(
				System.getProperty("java.io.tmpdir")
						.concat(File.separator)
						.concat(nombreArchivo)
//				.concat(Constantes.ARCHIVO_EXT_DOCX)
		);
		
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			BufferedInputStream archivoExportarBuffer =
					new BufferedInputStream(fileInputStream);
			BufferedOutputStream salidaStream =
					new BufferedOutputStream(response.getOutputStream());

			int read = 0;
			byte[] bytes = new byte[4096];
			while ((read = archivoExportarBuffer.read(bytes)) != -1) {
				salidaStream.write(bytes, 0, read);
			}

			salidaStream.flush();
			salidaStream.close();
			archivoExportarBuffer.close();
			fileInputStream.close();
		} catch (Exception e) {
			// TODO: enviar correo al admin con e.printtrace + e.getMessage
			// TODO: mandar codigo transacción. Usar temporalmente "0000"
		}
	}

}

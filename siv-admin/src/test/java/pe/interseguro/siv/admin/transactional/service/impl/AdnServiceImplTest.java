package pe.interseguro.siv.admin.transactional.service.impl;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import pe.interseguro.siv.admin.config.BaseTest;
import pe.interseguro.siv.admin.transactional.service.AdnService;
import pe.interseguro.siv.common.dto.request.ADNFirmaRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNReferidoRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNRegistroPlanFuturoBaseRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNRegistroPlanFuturoCapitalRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNRegistroPlanFuturoRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNRegistroPlanFuturoRespaldoRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNRegistroReferidoRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNRegistroRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNRegistroTitularRequestDTO;
import pe.interseguro.siv.common.dto.response.ADNFirmaResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNInicializacionResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNRegistroReferidoResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNRegistroResponseDTO;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Adn;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Persona;
import pe.interseguro.siv.common.persistence.db.mysql.repository.AdnRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.MultitablaRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.PersonaRepository;
import pe.interseguro.siv.common.persistence.rest.indenova.IndenovaRestClient;
import pe.interseguro.siv.common.persistence.rest.indenova.request.CrearCircuitoDocRequest;
import pe.interseguro.siv.common.persistence.rest.indenova.request.CrearCircuitoOptionRequest;
import pe.interseguro.siv.common.persistence.rest.indenova.request.CrearCircuitoRequest;
import pe.interseguro.siv.common.persistence.rest.indenova.request.CrearCircuitoSignerRequest;
import pe.interseguro.siv.common.persistence.rest.vtigger.VTigerRestClient;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.DateUtil;
import pe.interseguro.siv.common.util.IXDocReportUtil;

public class AdnServiceImplTest extends BaseTest {
	
	private Gson gson = new Gson();

	@Autowired
	private AdnService adnService;

	@Autowired
	private IndenovaRestClient indenovaRestClient;

	@Autowired
	private VTigerRestClient vtigerRestClient;

	@Autowired
	private AdnRepository adnRepository;

	@Autowired
	private PersonaRepository personaRepository;

	@Autowired
	private MultitablaRepository multitablaRepository;

	@Test
	public void obtenerInicializacionTest() {

		ADNInicializacionResponseDTO response = adnService.obtenerInicializacion("dev", "", "");
		System.out.println("response = " + gson.toJson(response));

		assertNotNull(response);
	}

	@Test
	public void registrarAdnTest() {

		ADNRegistroRequestDTO request = new ADNRegistroRequestDTO();

		request.setTipo(2);
		request.setFormulario(2);
		request.setIdUsuario("jaybaram");
		request.setNombreVendedorCRM("Nombre de agente de prueba");

		// -- 1
		ADNRegistroTitularRequestDTO titular = new ADNRegistroTitularRequestDTO();
		titular.setTipoDocumento(1);
		titular.setNumeroDocumento("47784213");
		titular.setFechaNacimiento("21/12/1968");
		titular.setNombres("JOHANA");
		titular.setApellidoPaterno("YOVERA");
		titular.setApellidoMaterno("PRADO");
		titular.setGenero(2);
		titular.setCelular(927128429);
		titular.setCorreo("yoverita.2000@gmail.com");
		titular.setProfesion(3);
		titular.setActividadEconomica(4);
		titular.setFumador(2);
		titular.setEdadActuarial(51);
		request.setTitular(titular);

		// -- 2
//		ADNRegistroFamiliarRequestDTO familiar1 = new ADNRegistroFamiliarRequestDTO();
//		familiar1.setTipoRelacion(1);
//		familiar1.setNombres("Jesus Perez Galvez");
//		familiar1.setEdad(17);
//		ADNRegistroFamiliarRequestDTO familiar2 = new ADNRegistroFamiliarRequestDTO();
//		familiar2.setTipoRelacion(3);
//		familiar2.setNombres("Ana Perez Galvez");
//		familiar2.setEdad(25);
//
//		java.util.List<ADNRegistroFamiliarRequestDTO> familiares = new ArrayList<>();
//		familiares.add(familiar1);
//		familiares.add(familiar2);
//
//		request.setFamiliar(familiares);

		// -- 3
		ADNRegistroPlanFuturoRequestDTO planFuturo = new ADNRegistroPlanFuturoRequestDTO();
		ADNRegistroPlanFuturoBaseRequestDTO base = new ADNRegistroPlanFuturoBaseRequestDTO();

		base.setPlanEducacion(2);
		base.setPlanJubilacion(2);
		base.setPlanProyecto(2);
//		base.setIngresoTitular(0.00);  
		base.setPorcentajeIngreso(5.0);
//		base.setTotalIngreso(0.00);  
//		base.setAnioProteccion(0);  

		ADNRegistroPlanFuturoRespaldoRequestDTO respaldo = new ADNRegistroPlanFuturoRespaldoRequestDTO();
//		respaldo.setAhorro(0.0);
		respaldo.setAhorroCheck(1);
//		respaldo.setPropiedad(200000.0);
		respaldo.setPropiedadCheck(1);
//		respaldo.setVehiculo(25000.0);
		respaldo.setVehiculoCheck(1);
//		respaldo.setSeguroVida(0.0);
		respaldo.setSeguroVidaCheck(1);
		respaldo.setSeguroVidaLey(0.00); // --
		respaldo.setSeguroVidaLeyCheck(1);

		ADNRegistroPlanFuturoCapitalRequestDTO capitalProteger = new ADNRegistroPlanFuturoCapitalRequestDTO();
//		capitalProteger.setCapitalProteccion(720000.0);
//		capitalProteger.setRespaldoEconomico(264000.0);
//		capitalProteger.setAfp(352800.0);
		capitalProteger.setAfpCheck(1);
		capitalProteger.setTotalCapitalProteger(0.0); // --
//		capitalProteger.setAdicional(100000.0);
		capitalProteger.setNuevoCapitalProteger(0.0); // --

		planFuturo.setBase(base);
		planFuturo.setRespaldo(respaldo);
		planFuturo.setCapitalProteger(capitalProteger);

		request.setPlanFuturo(planFuturo);

		// -- 4
		request.setIdAutoguardado("");
		request.setIdCircuitoFirma("");
		request.setIdAdn(1L);
		request.setIndicadorCambio(1);

		ADNRegistroResponseDTO response = adnService.registrarADNS(request);
		System.out.println("response = " + gson.toJson(response));

		assertNotNull(response);
	}

	@Test
	public void enviarArchivoAlCircuitoTest() {

		// -- URL: https://www.mkyong.com/java/java-read-a-file-from-resources-folder/
		File plantilla = new File(getClass().getClassLoader()
				.getResource(Constantes.RUTA_PLANTILLA + "/" + Constantes.PLANTILLA_TITULAR).getFile());

		System.out.println("existe = " + plantilla.exists());

		try {

			/*
			 * //-- Crear Word final y en PDF FileInputStream fileInputStream = new
			 * FileInputStream(file); BufferedInputStream bufferedInputStream = new
			 * BufferedInputStream(fileInputStream);
			 * 
			 * IXDocReport xdocReport =
			 * XDocReportRegistry.getRegistry().loadReport(bufferedInputStream,
			 * TemplateEngineKind.Freemarker);
			 * 
			 * IContext context = xdocReport.createContext(); context.put("nombres",
			 * "Carlos Diaz Valverde"); context.put("documento", "44114627");
			 * context.put("fecha", DateUtil.dateToString(new Date(),
			 * DateUtil.FORMATO_DIA_DDMMYYYY) );
			 * 
			 * String nombreArchivo = "adn_titular_final"; File tmp =
			 * File.createTempFile(nombreArchivo, ".pdf"); // File tmp =
			 * File.createTempFile(nombreArchivo, ".docx"); nombreArchivo = tmp.getName();
			 * 
			 * FileOutputStream outputStream = new FileOutputStream( tmp ); //
			 * xdocReport.process(context, outputStream); Options options =
			 * Options.getTo(ConverterTypeTo.PDF); xdocReport.convert(context, options,
			 * outputStream);
			 * 
			 * 
			 * fileInputStream.close(); bufferedInputStream.close(); outputStream.close();
			 */

			// -- Generar Documento
			String nombreCliente = "Carlos Diaz Valverde";
			String documentoIdentidad = "44114627";
			String nombreArchivo = "adn_titular_generado";
			Date hoy = new Date();

			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("nombres", nombreCliente);
			parametros.put("documento", documentoIdentidad);
			parametros.put("fecha", DateUtil.dateToString(hoy, DateUtil.FORMATO_DIA_DDMMYYYY));

			nombreArchivo = IXDocReportUtil.generarDocumento(plantilla, nombreArchivo, parametros, true);

			// -- Enviar a Indenova
			File archivoGenerado = new File(
					System.getProperty("java.io.tmpdir").concat(File.separator).concat(nombreArchivo));

			if (!archivoGenerado.exists()) {
				// -- Sino generar throw Exception
			}

			CrearCircuitoRequest request = new CrearCircuitoRequest();
			String circuitoAsunto = Constantes.CREAR_CIRCUITO_SUBJECT.concat(documentoIdentidad)
					.concat(DateUtil.dateToString(hoy, DateUtil.FORMATO_DIA_DDMMYYYYHHMMSS));

			request.setInclude(Constantes.CREAR_CIRCUITO_INCLUDE);
			request.setSubject(circuitoAsunto);
			request.setDescription(circuitoAsunto);

			request.setSigners(new ArrayList<CrearCircuitoSignerRequest>());
			request.getSigners().add(new CrearCircuitoSignerRequest(nombreCliente, documentoIdentidad, "", ""));

			request.setDocs(new ArrayList<CrearCircuitoDocRequest>());
			request.getDocs()
					.add(new CrearCircuitoDocRequest("",
							"adnDigital_".concat(documentoIdentidad).concat(Constantes.ARCHIVO_EXT_PDF),
							Constantes.CREAR_CIRCUITO_SIZE, Constantes.CREAR_CIRCUITO_DOCTYPE,
							Files.readAllBytes(archivoGenerado.toPath())));

			request.setOptions(new ArrayList<CrearCircuitoOptionRequest>());
			request.getOptions().add(new CrearCircuitoOptionRequest(Constantes.CREAR_CIRCUITO_OPTION1_NAME,
					Constantes.CREAR_CIRCUITO_OPTION1_VALUE));
			request.getOptions().add(new CrearCircuitoOptionRequest(Constantes.CREAR_CIRCUITO_OPTION2_NAME,
					Constantes.CREAR_CIRCUITO_OPTION2_VALUE));

			System.out.println("request = " + gson.toJson(request));

//			CrearCircuitoResponse response = indenovaRestClient.crearCircuito(request);
//			System.out.println("response = " + gson.toJson(response) );

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void registrarFirmaTest() {

		ADNFirmaRequestDTO aDNFirmaRequestDTO = new ADNFirmaRequestDTO();
		aDNFirmaRequestDTO.setTipoDocumento(1);
		aDNFirmaRequestDTO.setNumeroDocumento("85630147");
		aDNFirmaRequestDTO.setFechaNacimiento("12/09/1968");
		aDNFirmaRequestDTO.setNombres("Juan");
		aDNFirmaRequestDTO.setApellidoPaterno("Perez");
		aDNFirmaRequestDTO.setApellidoMaterno("Del Solar");
		aDNFirmaRequestDTO.setGenero(1);
		aDNFirmaRequestDTO.setCelular(952304752);
		aDNFirmaRequestDTO.setCorreo("demo@gmail.com");
		aDNFirmaRequestDTO.setProfesion(1);
		aDNFirmaRequestDTO.setActividadEconomica(1);
		aDNFirmaRequestDTO.setFumador(1);

		ADNFirmaResponseDTO aDNFirmaResponseDTO = adnService.registrarFirma(aDNFirmaRequestDTO);
		System.out.println("response = " + gson.toJson(aDNFirmaResponseDTO));

		assertNotNull(aDNFirmaResponseDTO);
	}

	@Test
	public void obtenerAdnTest() {

		Adn adn = adnRepository.findById(36L).get();
		System.out.println("=======================");

		System.out.println("ADN===>" + adn);
		System.out.println("ADN.getIdAdn===>" + adn.getIdAdn());
		System.out.println("ADN.getPersona===>" + adn.getPersona());

		System.out.println("=======================");
		assertNotNull(adn);
		/*
		 * Adn adn = adnRepository.findById(9L).get(); System.out.println("Apellido:" +
		 * adn.getPersona().getApellidoPaterno()); System.out.println("response = " +
		 * gson.toJson(adn));
		 * 
		 * assertNotNull(adn);
		 */
	}

	@Test
	public void obtenerPersonaTest() {

		Persona persona = personaRepository.findById(58L).get();
		System.out.println("=======================");

		System.out.println("PERSONA===>" + persona);
		System.out.println("PERSONA.getIdPersona===>" + persona.getIdPersona());

		System.out.println("=======================");
		System.out.println(String.format("%,.0f", 1500.15));
		assertNotNull(persona);

	}

	@Test
	public void registrarAdnReferidoTest() {

		ADNRegistroReferidoRequestDTO request = new ADNRegistroReferidoRequestDTO();
		request.setIdUsuario("ext.acerna");
		request.setIdAdn(1L);
		List<ADNReferidoRequestDTO> referidos = new ArrayList<ADNReferidoRequestDTO>();
		ADNReferidoRequestDTO referido1 = new ADNReferidoRequestDTO();
		referido1.setNombres("Paul Bautista");
		referido1.setTelefono("949820012");
		referidos.add(referido1);
		request.setReferidos(referidos);

		ADNRegistroReferidoResponseDTO response = adnService.registrarReferidoADNS(request);
		System.out.println("response = " + gson.toJson(response));

		assertNotNull(response);
	}
 
}

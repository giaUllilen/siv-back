/**
 * 
 */
package pe.interseguro.siv.common.util;

/**
 * Constantes generales
 * 
 * @author ti-is
 */
public interface Constantes {

	// -- Codigo Respuesta REST
	String CODIGO_RESPUESTA_GENERAL_EXITO = "01";
	String CODIGO_RESPUESTA_GENERAL_ERROR = "99";
	String CODIGO_RESPUESTA_GENERAL_ERROR_FUNCIONAL = "98";
	String CODIGO_RESPUESTA_GENERAL_CLIENTE_VTIGER_ERROR = "96";
	String CODIGO_RESPUESTA_GENERAL_NO_EXISTE = "97";
	String CODIGO_RESPUESTA_GENERAL_CULQI_ERROR_400 = "95";
	String CODIGO_RESPUESTA_GENERAL_CULQI_ERROR_500 = "94";
	String CODIGO_RESPUESTA_GENERAL_CLIENTE_CRM_ERROR = "02";
	String CODIGO_RESPUESTA_GENERAL_ADVERTENCIA = "05";
	String CODIGO_RESPUESTA_SOLICITUD_FIRMA_COMPLETA = "10";
	String CODIGO_CRM_RESPUESTA_TRUE = "true";
	String CODIGO_CRM_RESPUESTA_FALSE = "false";
	String MESSAGE_ERROR_NODATA = "No se encontraron datos.";
	String MESSAGE_ERROR_NODATA_AUTOCOMPLETE   = "Datos no encontrados, por favor complete los datos.";
	String RESPUESTA_NEGOCIO_OK = "02";

	// -- Descripcion Respuestas REST
	String MENSAJE_RESPUESTA_GENERAL_EXITO = "mensajeRespuesta.general.exito";
	String MENSAJE_RESPUESTA_GENERAL_ERR0R = "mensajeRespuesta.general.error";
	String MENSAJE_RESPUESTA_GENERAL_NO_EXISTE = "mensajeRespuesta.general.no.existe";
	String MENSAJE_RESPUESTA_REFERIDOS_EXITO = "mensajeRespuesta.referidos.exito";
	String MENSAJE_RESPUESTA_CRM_UPDATE_ERROR = "mensajeRespuesta.crm.update.error";
	String MENSAJE_RESPUESTA_NO_EMISION = "La propuesta no es una emision automatica";
	String MENSAJE_RESPUESTA_EMISION_ERROR = "Error al afiliar - estado solicitud pendiente Afiliación";
	String MENSAJE_RESPUESTA_REQUEST_EVALUATOR = "mensaje.error.requestEvaluator";
	String MENSAJE_RESPUESTA_REGISTRAR_ADNS = "mensaje.error.registraraAdns";
	String MENSAJE_RESPUESTA_OBTENER_AGENTE = "mensaje.error.obtenerAgente";

	// -- Indicador de Cambio
	Integer CODIGO_INDICADOR_CAMBIO_SI = 1;
	Integer CODIGO_INDICADOR_CAMBIO_NO = 2;

	// -- Formularios
	Integer CODIGO_FORMULARIO_1 = 1;
	Integer CODIGO_FORMULARIO_2 = 2;

	// -- Operaciones
	Integer CODIGO_OPERACION_REGISTRAR = 1;
	Integer CODIGO_OPERACION_ACTUALIZAR = 2;

	// -- Multitabla - Parametro ADN (INDEX)
	Integer PARAMETRO_ADN_PORCENTAJE_ING_INI = 0;
	Integer PARAMETRO_ADN_PORCENTAJE_ING_FIN = 1;
	Integer PARAMETRO_ADN_SEGURO_VIDA_LEY_REGLA = 2;

	Integer PARAMETRO_ADN_PORCENTAJE_ING_DEFAULT = 4;
	Integer PARAMETRO_ADN_URL_PDF_EVIDENCIAS = 5;
	Integer PARAMETRO_ADN_URL_PDF_EDUCACION = 6;
	Integer PARAMETRO_ADN_URL_PDF_PROYECTOS = 7;
	Integer PARAMETRO_ADN_URL_PDF_JUBILACION = 8;

	Integer PARAMETRO_SOLICITUD_PARENTEZCO = 3 - 1;
	Integer PARAMETRO_SOLICITUD_VINCULO_ASEGURADO = 4 - 1;
	Integer PARAMETRO_SOLICITUD_VINCULO_ASEGURADO_RUC = 5 - 1;

	String MULTITABLA_CODIGO_TABLA_TIPO_DOCUMENTO = "001";
	String MULTITABLA_CODIGO_TABLA_GENERO = "002";
	String MULTITABLA_CODIGO_TABLA_NACIONALIDAD = "010";
	String MULTITABLA_CODIGO_TABLA_PROFESION = "003";
	String MULTITABLA_CODIGO_TABLA_ACTIVIDAD_ECONOMICA = "004";
	String MULTITABLA_CODIGO_TABLA_TIPO_RELACION = "006";
	String MULTITABLA_CODIGO_TABLA_ESTADO_CIVIL = "012";
	String MULTITABLA_CODIGO_TABLA_DIRECCION_TIPO = "013";
	String MULTITABLA_CODIGO_TABLA_MONEDA = "014";
	String MULTITABLA_CODIGO_TABLA_FUMADOR = "018";
	String MULTITABLA_CODIGO_DEPARTAMENTO = "027";
	String MULTITABLA_CODIGO_PROVINCIA = "028";
	String MULTITABLA_CODIGO_DISTRITO = "029";
	String MULTITABLA_CODIGO_SUJETO_OBLIGADO = "030";
	String MULTITABLA_CODIGO_PEP = "031";
	String MULTITABLA_CODIGO_SUBPLAN_COTIZADOR = "021";
	String MULTITABLA_CODIGO_SUBPLAN_COTIZADOR_VIDA_FREE = "032";
	String MULTITABLA_CODIGO_FRECUENCIA = "016";
	String MULTITABLA_CODIGO_TABLA_COBERTURAS = "020";
	String MULTITABLA_CODIGO_TABLA_COBERTURAS_VIDA_FREE = "033";
	String MULTITABLA_CODIGO_TIPO_EMISION = "068";
	String MULTITABLA_CODIGO_TIPO_PRODUCTO = "015";

	// -- Plantillas documentos
	String RUTA_PLANTILLA = "templates";
	//String RUTA_PLANTILLA = "TMP_PDF";
	String RUTA_PLANTILLA_TEST = "templates";
	String PLANTILLA_TITULAR = "adn_titular.docx";
	String LDPDP = "LDPDP.docx";
	String PLANTILLA_COMPLETA = "adn_completo.docx";
	//String PLANTILLA_COTIZADOR = "Plantilla_Reporte_Cotizacion.docx";
	//String PLANTILLA_COTIZADOR_V2 = "Plantilla_Reporte_Cotizacion_v2.docx";
	//String PLANTILLA_COTIZADOR_V3 = "Plantilla_Reporte_Cotizacion_v3.docx";
	String PLANTILLA_COTIZADOR_VF_HTML = "Plantilla_Reporte_Cotizacion_VF";

	//String PLANTILLA_COTIZADOR_PG = "Plantilla_Reporte_Cotizacion_PG.docx";
	//String PLANTILLA_COTIZADOR_PG_V2 = "Plantilla_Reporte_Cotizacion_PG_v2.docx";
	String PLANTILLA_COTIZADOR_PG_HTML = "Plantilla_Reporte_Cotizacion_PG";
	String PLANTILLA_SOLICITUD_PG = "Plantilla_Solicitud_v1.docx";
	String PLANTILLA_SOLICITUD_VF = "Plantilla_Solicitud_v2.docx";
	String PLANTILLA_ACP_NATURAL = "Plantilla_ACP_Natural.docx";
	String PLANTILLA_ACP_JURIDICO = "Plantilla_ACP_Juridico.docx";
	String PLANTILLA_ACP_NATURAL_HTML = "Plantilla_ACP_Natural";
	String PLANTILLA_ACP_JURIDICO_HTML = "Plantilla_ACP_Juridico";
	String PLANTILLA_SOLICITUD_IMAGE_COMPLETE = "mini_circle_complete.png";
	String PLANTILLA_SOLICITUD_IMAGE_INCOMPLETE = "mini_circle_incomplete.png";
	String PLANTILLA_SOLICITUD_CORREO = "solicitud_correo.html";
	String PLANTILLA_LINK_PAGO = "mail_link_paso.html";
	String PLANTILLA_ACP_CORREO_CONFIRM_PG = "acp_correo_pg_{0}.html";
	String PLANTILLA_ACP_CORREO_CONFIRM_VF = "acp_correo_vf.html";
	String PLANTILLA_ACP_CORREO_SUPERVISOR = "acp_correo_supervisor.html";
	String PLANTILLA_AVISO_COBRO = "mail_aviso_cobro.html";
	String PLANTILLA_ADN_CORREO = "adn_correo.html";
	String PLANTILLA_COTIZADOR_CORREO = "cotizador_correo.html";
	String PLANTILLA_FORMATO_PEP = "Formato_PEP.pdf"; 
	String PLANTILLA_FORMATO_PJ = "Formato_PJ.pdf";//JTRAC 4 ADN
	String PLANTILLA_FORMATO_W9EN = "FormatoW9Eng.pdf";
	String PLANTILLA_FORMATO_W9ES = "FormatoW9Esp.pdf";
	String PLANTILLA_FORMATO_WAIVER = "FormatoWaiver.pdf";
	String PLANTILLA_ADN_CONSENTIMIENTO = "Plantilla_consentimiento_1.html";
	String PLANTILLA_ADN_CONSENTIMIENTO_2 = "Plantilla_consentimiento_2.html";
	String PLANTILLA_REPROCESO_EMISION = "reproceso_emision.html";
	String PLANTILLA_REPROCESO_SUSCRIPCION = "reproceso_suscripcion.html";
	
	/* Reingenieria ADN / SIV */
	String ANIO_EJEM_PDF_COTIZACION_VIDA_FREE = "6";
	
	//-- Plantillas indenova
	String CREAR_CIRCUITO_INCLUDE = "SIGNERS_LINKS";
	String CREAR_CIRCUITO_SUBJECT = "Interseguro - Consentimiento de Datos Personales de ";
	Integer CREAR_CIRCUITO_SIZE = 1000;
	String CREAR_CIRCUITO_DOCTYPE = "23";
	String CREAR_CIRCUITO_OPTION1_NAME = "DISABLE_SIGNATURE_ORDER";
	String CREAR_CIRCUITO_OPTION1_VALUE = "true";
	String CREAR_CIRCUITO_OPTION2_NAME = "GUESTS_ALLOWED_SIGNATURE_TYPES";
	String CREAR_CIRCUITO_OPTION2_VALUE = "{\"action\":\"allow\",\"signatureTypes\": [\"handwritten\"]}";
	String CREAR_CIRCUITO_OPTION3_NAME = "ALLOW_EMAIL_NOTIFICATIONS";
	String CREAR_CIRCUITO_OPTION3_VALUE = "false";
	String CREAR_CIRCUITO_OPTION4_NAME = "USE_SIGNERS_AS_EMAIL_ADDRESSEES";
	String CREAR_CIRCUITO_OPTION4_VALUE = "true";

	// -- SEGURIDAD
	String LOGIN_URL = "/api/v1/usuarios/ingresos";
	String LOGIN_BYPASS_USER = "admin";
	String LOGIN_BYPASS_PASSWORD = "1nt3rs3g4r0";
	String LOGIN_BYPASS_ROLE = "ADMIN";
	String JWT_ISSUER = "https://www.interseguro.com.pe/";
	String JWT_AUDIENCE = "web";
	Long JWT_EXPIRATION_TIME = 3600000L; // -- 30 minutos (1 min - 60 seg - 60000 ms)
	String JWT_SECRET_KEY = "$3gur0$1R";
	String JWT_RESPONSE_HEADER_KEY = "AuthorizationIS";
	String JWT_RESPONSE_HEADER_PREFIX = "Bearer";
	String JWT_URL_REFRESH_TOKEN = "/refreshToken";

	// -- Seguridad con API KEY para servicios compartidos
	String COMPARTIDO_URL = "/api/v1/cotizaciones/crm/cotiza";

	// -- Mensajes
	String MENSAJE_GENERICO_SOAEXCEPTION = "generico.soaException";
	String MENSAJE_GENERICO_SOAEXCEPTION_EMAIL = "generico.soaException.email";
	String MENSAJE_RESTCLIENT_INTERSEGURO_CORREO = "interseguro.enviarCorreo.soaException";
	String MENSAJE_RESTCLIENT_INTERSEGURO_SMS = "interseguro.enviarSms.soaException";
	String MENSAJE_RESTCLIENT_CRM_VALIDAR_ASIGNACION = "crm.validarCreacionAsignacion.soaException";
	String MENSAJE_RESTCLIENT_CRM_DATO_CONTACTO = "crm.getDatosContacto.soaException";
	String MENSAJE_RESTCLIENT_CRM_VALIDAR_ACCESO = "crm.validarAccesoCrm.soaException";
	String MENSAJE_RESTCLIENT_CRM_UPDATE = "crm.updateCrm.soaException";
	String MENSAJE_RESTCLIENT_CRM_COTIZADOR_TOKEN = "crm.cotizador.token.soaException";
	String MENSAJE_RESTCLIENT_CRM_UPDATE_SITC = "crm.updateSitc.soaException";
	String MENSAJE_RESTCLIENT_INDENOVA_CREAR_TOKEN = "indenova.crearToken.soaException";
	String MENSAJE_RESTCLIENT_INDENOVA_CREAR_CIRCUITO = "indenova.crearCircuito.soaException";
	String MENSAJE_RESTCLIENT_INDENOVA_DOCUMENTO_CIRCUITO = "indenova.obtenerDocumentoCircuito.soaException";
	String MENSAJE_RESTCLIENT_INTERSEGURO_AZMAN_USUARIO = "interseguro.azman.soaException";
	String MENSAJE_LOGIN_TX_AZMAN = "interseguro.azman.login.soaException";
	String MENSAJE_RESTCLIENT_INTERSEGURO_AZMAN_TOKEN = "interseguro.azman.token.soaException";
	String MENSAJE_ADN_FIRMA_TX_PLANTILLA = "adn.registrarFirma.plantilla.txException";
	String MENSAJE_ADN_FIRMA_TX_GENERAR_DOCUMENTO = "adn.registrarFirma.generarDocumento.txException";
	String MENSAJE_ADN_FIRMA_TX_DOCUMENTO_GENERADO = "adn.registrarFirma.documentoGenerado.txException";
	String MENSAJE_ADN_FIRMA_TX_ENVIO_INDENOVA = "adn.registrarFirma.envioIndenova.txException";
	String MENSAJE_ADN_FIRMA_TX_GENERAR_CIRCUITO = "adn.registrarFirma.indenova.error.generar.circuito";
	String MENSAJE_RESTCLIENT_CRM_UPDATE_COTIZACION = "crm.updateCotizacion.soaException";
	String MENSAJE_RESTCLIENT_CRM_UPDATE_CONTACTO = "crm.updateContacto.soaException";
	String MENSAJE_RESTCLIENT_CRM_UPDATE_DPS = "crm.updateDPS.soaException";
	String MENSAJE_RESTCLIENT_CRM_CREAR_BENEFICIARIOS = "crm.crearBeneficiarios.soaException";
	String MENSAJE_RESTCLIENT_CRM_URL_COTIZACION_NUEVO = "crm.urlNuevoCotizacion.soaException";
	String MENSAJE_RESTCLIENT_CRM_URL_COTIZACION_RECOTIZAR = "crm.urlRecotizarVida.soaException";
	String MENSAJE_RESTCLIENT_INTERSEGURO_VIDAFREE_CONSULTA = "interseguro.vidafree.soaException";
	String MENSAJE_RESTCLIENT_COTIZADOR_ASEGURABLE = "cotizador.asegurable.soaException";
	String MENSAJE_USUARIO_INGRESO_AZMAN = "usuario.usuarioIngreso.validarBaseDatos.txException";

	String MENSAJE_RESTCLIENT_INDENOVA_CIRCUITO = "indenova.obtenerCircuito.soaException";
	String MENSAJE_RESTCLIENT_INTERSEGURO_DIGITAL_TOKEN = "interseguro.digital.token.soaException";
	String MENSAJE_ADN_COTIZAR_GUARDAR_ERROR = "adn.cotizar.crm.guardar.error";
	String MENSAJE_ADN_COTIZAR_GUARDAR_SIN_OPORTUNIDAD = "adn.cotizar.crm.guardar.sin.oportunidad";
	
	// MENSAJES BUPO
	String MENSAJE_RESTCLIENT_BUPO_VALIDAR_PROSPECTO_ASIGNACION = "bupo.validarProspectoAsignacion.soaException";
	String CODIGO_RESPUESTA_GENERAL_CLIENTE_BUPO_ERROR = "102";
	String MENSAJE_RESTCLIENT_BUPO_OBTENER_AGENTE = "bupo.obtenerDatosAgentePorEmail.soaException";
	String MENSAJE_RESTCLIENT_BUPO_ASIGNAR_PROSPECTO = "bupo.asignarProspecto.soaException";
	String MENSAJE_RESTCLIENT_BUPO_CREAR_PROSPECTO = "bupo.crearProspecto.soaException";
	
	// MENSAJES GLOBAL
	String MENSAJE_RESTCLIENT_GLOBAL_OBTENER_POLIZAS = "global.obtenerPolizas.soaException";
	String MENSAJE_RESTCLIENT_GLOBAL_OBTENER_MONTO_RECARGO = "global.obtenerMontoRecargo.soaException";
		
	String MENSAJE_RESTCLIENT_CULQI_TOKENIZAR = "culqi.tokenizar.soaException";
	String MENSAJE_RESTCLIENT_CULQI_CARGO = "culqi.cargo.soaException";

	String MENSAJE_RESTCLIENT_SITC_TOKEN = "sitc.token.soaException";

	String MENSAJE_SOLICITUD_NO_FIRMADA = "solicitud.noFirmada";
	String MENSAJE_SOLICITUD_ERROR_DATOS_NO_ENVIADOS_CRM = "solicitud.error.enviar.datosCRM";
	String MENSAJE_SOLICITUD_DOCUMENTO_PDF_ERROR = "solicitud.documentoGenerado.txException";

	String MENSAJE_SOLICITUD_CLIENTE_SIN_ADN = "solicitud.cliente.sinAdn";
	String MENSAJE_SOLICITUD_CLIENTE_NO_EXISTE = "solicitud.cliente.no.existe";
	String MENSAJE_SOLICITUD_COTIZACION_NO_ENCONTRADA = "solicitud.cotizacion.no.encontrada";
	String MENSAJE_SOLICITUD_NO_REGISTRADA = "solicitud.no.registrada";
	String MENSAJE_SOLICITUD_ERROR_OBTENER_DATOS_FORMULARIO = "solicitud.datosFormulario.soaException";
	String MENSAJE_SOLICITUD_SMS = "solicitud.sms";
	String MENSAJE_SOLICITUD_ERROR_VALIDAR_CODIGO = "solicitud.error.validar.codigo";
	String MENSAJE_SOLICITUD_ERROR_GUARDAR = "solicitud.guardar.soaException";
	String MENSAJE_SOLICITUD_COTIZACION_NO_EXISTE_CRM = "solicitud.cotizacion.crm.no.existe";
	
	String MENSAJE_SOLICITUD_EMPTY_PLAFT = "Adicionalmente debe firmar los documentos adjuntos que se encuentra en el presente correo y enviarlos al agente.";
	String MENSAJE_PAGO_SAMP_DIGITALIZACION_ERROR = "Pago no pudo ser registrado en SAMP";
	String MENSAJE_PAGO_SITC_AFILIACION_ERROR = "Afiliacion no pudo ser registrada en SITC";
	String MENSAJE_PAGO_SITC_AFILIACION_WARNING = "Afiliación se ha registrado sin la prima recurrente";

	String MENSAJE_VALIDACION_TRASPASO_ERROR = "El asegurado y contratante de la propuesta origen(%s) deben ser iguales en la propuesta destino (%s)";

	// -- Genericos
	String USUARIO_ADMIN = "admin";
	String TIMEZONE_DEFAULT = "GMT-05:00";
	String PROFILE_DEV = "develop";
	String PROFILE_TST = "uat";
	String PROFILE_PRD = "master";
	String FLAG_SMS_EXITO = "1";
	String FLAG_SMS_FALLA = "2";
	String ARCHIVO_EXT_PDF = ".pdf";
	String ARCHIVO_EXT_DOCX = ".docx";
	String MENSAJE_ADMINISTRADOR = "Comunicarse con el Administrador";

	// -- Excepciones de rutas
	String URL_EXCEPTION_DESCARGAS = "/api/v1/descargas";
	String URL_EXCEPTION_INDENOVA_WEBHOOK_NOTIFICACION = "/solicitudes/indenova/webhooks/confirmacionFirma";
	String URL_EXCEPTION_TOKEN_VIDA_FREE = "/api/v1/cotizaciones/decrypt";
	String URL_EXCEPTION_CORRELATIVO_VIDA_FREE = "/api/v1/cotizaciones/correlativo";
	String URL_EXCEPTION_CUMULO = "/api/v1/cotizaciones/cumulo";
	String URL_EXCEPTION_TIPO_CAMBIO = "/api/v1/cotizaciones/intermedio/tipoCambio";
	String URL_EXCEPTION_CONVERSION_MONEDA = "/api/v1/cotizaciones/intermedio/conversionMoneda";
	String URL_EXCEPTION_PRINT_COTIZACION_VIDA_FREE = "/api/v1/cotizaciones/vidafree/pdf";
	String URL_EXCEPTION_PRINT_ADN = "/api/v1/adns/pdf"; // Anedd
	String URL_EXCEPTION_PRINT_LISTAR_ARCHIVOS = "/api/v1/solicitudes/obtenerArchivo"; // Anedd documentos -- Sirve para listar y obtener el archivo
	String URL_EXCEPTION_PRINT_COTIZACION_VIDA = "/api/v1/cotizaciones/pdf";
	String URL_EXCEPTION_CLONAR_COTIZACION_VIDA_FREE = "/api/v1/cotizaciones/vidafree/clonar";
	String URL_EXCEPTION_FORZAR_COTIZACION_VIDA_FREE = "/api/v1/cotizaciones/vidafree/reprocesar";
	String URL_EXCEPTION_VALIDAR_COTIZACION_VIDA_FREE = "/api/v1/cotizaciones/vidafree/validar";
	String URL_EXCEPTION_PRINT_SOLICITUD_VIDA_FREE = "/api/v1/solicitudes/pdf";
	String URL_EXCEPTION_PRINT_CREAR_SOLICITUD_VIDA_FREE = "/api/v1/solicitudes/pdf/crear";
	String URL_EXCEPTION_PRINT_CREAR_SOLICITUD_ACP = "/api/v1/solicitudes/pdf/acp/crear";
	String URL_EXCEPTION_SOLICITUD_CRM_MANUAL = "/api/v1/solicitudes/finalizarProcesoSolicitudManual";
	String URL_EXCEPTION_TEMP = "/api/v1/solicitudes/enviar-correo/avisoCobro";
	String URL_EXCEPTION_REGISTRO_AUDITORIA = "/api/v1/solicitudes/registro-tarjeta";
	String URL_EXCEPTION_PAGO_WEB = "/api/v1/solicitudes/link-pago/operaciones";
	String URL_EXCEPTION_CRM_REPORTE_RRVV = "/api/v1/utilidades/reporte-rrvv";
	String URL_EXCEPTION_AZMAN_SERVICE = "/api/v1/usuarios/valida-ad";
	String URL_EMISION_AUTOMATICA = "/api/v1/solicitudes/pubSubSend";
	String URL_POLICY_ISSUED = "/api/v1/solicitudes/policyIssued";
	String URL_EXCEPTION_LOGGER = "/api/v1/logger";
	String URL_EXCEPTION_TRANSMISION_COTIZACION = "/api/v1/cotizaciones/transmision";
	String URL_EXCEPTION_OBTENER_COTIZACION = "/api/v1/solicitud/obtenerCotizacion";
	String URL_EXCEPTION_AFILIACION_TRASPASO = "/api/v1/solicitudes/afiliacionTraspaso";
	String URL_EXCEPTION_LOG_BOTON = "/api/v1/solicitudes/printLog";
	// -- Codigo Evento Firma Indenova
	String CODIGO_INDENOVA_EVENTO_FIRMA_FINALIZADO = "finalized";

	// -- Codigo Estado Solicitud
	String CODIGO_SOLICITUD_ZERO = "0";
	String CODIGO_SOLICITUD_PENDIENTE = "1";
	String CODIGO_SOLICITUD_PENDIENTE_FIRMA = "2";
	String CODIGO_SOLICITUD_FIRMA_FINALIZADA = "3";
	String CODIGO_SOLICITUD_POR_TRANSMITIR = "4";
	String CODIGO_SOLICITUD_PAGO_CULQI = "5";
	String CODIGO_SOLICITUD_PAGO_SAMP = "6";
	String CODIGO_SOLICITUD_TRANSMITIDA = "4";
	String CODIGO_SOLICITUD_RECARGO = "";
	String CODIGO_SOLICITUD_PAGO_CULQI_RECARGO = "1";
	
	//-- Codigo Estado General
	String CODIGO_ESTADO_GENERAL = "2";
	String ENTREGADO_A_GDC = "3";
	String TRANSMITIDA = "4";

	
	//-- Codigo Estado CRM
	String CODIGO_CRM_ENTREGADO_GDC = "538560005";
	String CODIGO_CRM_TRANSMITIDO = "538560000";
	String CODIGO_CRM_ADN_ENTREGADO_GDC = "3";
	String CODIGO_CRM_ADN_TRANSMITIDO = "4";

	// -- Codigo Tipo Documento
	String CODIGO_TIPO_DOCUMENTO_RUC_NATURAL = "10";
	String CODIGO_TIPO_DOCUMENTO_RUC_JURIDICO = "11";

	String USUARIO_INDENOVA = "Webhook.Indenova";

	Integer SOLICITUD_PESO_VARIACION_DEFAULT = 5;

	Integer SOLICITUD_DPS_PREGUNTA_BLOQUE_1 = 1;
	Integer SOLICITUD_DPS_PREGUNTA_BLOQUE_2 = 2;
	String SOLICITUD_ASEGURADO_IGUAL_CONTRATANTE_TRUE = "1";

	String ESPACIO_BLANCO = " ";
	String VALOR_VACIO = "";

	String MULTITABLA_COLUMNA_VALOR = "valor";
	String MULTITABLA_COLUMNA_VALOR_AUXILIAR = "valorAux";
	String MULTITABLA_COLUMNA_VALOR_CRM = "valorCRM";
	String MULTITABLA_COLUMNA_VALOR_FLEX = "valorFlex";

	String ADN_PRODUCTO_PLAN_GARANTIZADO = "1";
	String ADN_PRODUCTO_VIDA_FREE = "2";
	String ADN_PRODUCTO_PLAN_GARANTIZADO_EDUCACION = "1";
	String ADN_PRODUCTO_PLAN_GARANTIZADO_JUBILACION = "2";
	String ADN_PRODUCTO_PLAN_GARANTIZADO_SUENO = "3";
	String COTIZADOR_VIDA_PRODUCTO_PLAN_GARANTIZADO = "PlanGarantizado";
	String COTIZADOR_VIDA_PRODUCTO_VIDA_FREE = "VidaFree";

	String COTIZADOR_VIDA_VALOR_SI = "S";
	String COTIZADOR_VIDA_VALOR_NO = "N";

	String MONEDA_ACSELE_SOLES = "2163";
	String MONEDA_ACSELE_DOLARES = "2123";
	String MONEDA_COTIZADOR_SOLES = "PEN";
	String MONEDA_COTIZADOR_DOLARES = "USD";
	String MONEDA_ADN_SOLES = "1";
	String FRECUENCIA = "1";
	String MONEDA_ADN_DOLARES = "2";
	String MONEDA_SIMBOLO_SOLES = "S/.";
	String MONEDA_SIMBOLO_DOLARES = "$";
	String FRECUENCIA_MENSUAL = "MENSUAL";
	String FRECUENCIA_TRIMESTRAL = "TRIMESTRAL";
	String FRECUENCIA_SEMESTRAL = "SEMESTRAL";
	String FRECUENCIA_ANUAL = "ANUAL";

	String PASARELA_CULQI = "Culqi";
	String PASARELA_NIUBIZ = "Niubiz";
	String PASARELA_IZIPAY = "Izipay";
	Integer SAMP_TIPO_VIA_COBRO_TARJETA = 2;
	Integer SAMP_TIPO_VIA_COBRO_CUENTA = 1;
	Integer SAMP_TIPO_VIA_COBRO_CAJA_VENTANILLA = 3;
	String SAMP_USUARIO_ADN = "ADN";
	String SAMP_RESPUESTA_OK = "1";

	String KEY_ENCRYPT = "HASHENCRYPTKEY19";

	String SERVER_PORT_PRODUCCION = "8003";

	String PAGO_PRIMA_VAL = "1";
	Integer CVC_REPUESTA_OK = 1;
		
	// consentimiento
	Integer IDTRAMITEVIDA = 4;
	Integer IDCONFIGURACIONUNIVERSAL = 3;
	Integer IDTRATAMIERNTOPRODUCTOVIDA = 5;
	Integer IDTRATAMIENTOUNIVERSALINTERCORP = 10;
	Integer IDTRATAMIENTOUNIVERSALINTERCORP_ESTUDIOS_MERCADO = 9;
	String CONSENTIMIENTO_TIPODDOCUMENTO_DNI = "D";
	String CONSENTIMIENTO_TIPODDOCUMENTO_CE = "E";
	String CONSENTIMIENTO_TIPODDOCUMENTO_RUC = "R";
	String CONSENTIMIENTO_DEPARTAMENTO_DEFAULT = "CEA98F63-E634-E211-9DA0-005056A6000F";
	String CONSENTIMIENTO_PROVINCIA_DEFAULT = "D861C7BD-EB34-E211-9DA0-005056A6000F";
	String CONSENTIMIENTO_DISTRITO_DEFAULT = "D23E280B-EE34-E211-9DA0-005056A6000F";
	String CONSENTIMIENTO_ESPACIO_DEFAULT = "800";
	Integer CONSENTIMIENTO_CODVIA_DEFAULT = 2;
	String CONSENTIMIENTO_DIRECCION_DEFAULT = "Javier prado";
	String CONSENTIMIENTO_AGENTE_DEFAULT = "int_test_agente@yopmail.com";
	String CONSENTIMIENTO_ACEPTADO_URL = "/api/v1/adns/getDataConsentimientoAccepted";
	String CONSENTIMIENTO_RECOTIZAR_VALIDAR = "/api/v1/solicitudes/recotizacion/validar";
	String CONSENTIMIENTO_DEFAULT_FECHA_NACIMIENTO = "1999-01-01";

	String MENSAJE_RESTCLIENT_GLOBAL_CONSULTAR_TASA_CAMBIO = "global.consultarTasaDeCambio.soaException";
	String MENSAJE_RESTCLIENT_GLOBAL_CONSULTAR_POLIZAS_CANCELADAS = "global.consultarPolizasCanceladas.soaException";
	
	String TIPO_DOCUMENTO_DNI = "DNI";
	String TIPO_DOCUMENTO_CE = "CE";
	String TIPO_DOCUMENTO_RUC = "RUC";
	
	String ACCION_CUENTA = "CUENTA";
	String WINDOWS = "Windows";

    String APPLICATION_JSON = "application/json";
	String CONTENT_TYPE = "Content-Type";

	String CONSENTIMIENTO_EMPRESA = "interseguro";
	String CONSENTIMIENTO_PRODUCTO = "VIDA";
	String CONSENTIMIENTO_SUB_TIPO_CANAL = "web";
	String CONSENTIMIENTO_CANAL = "self-service";
	String CONSENTIMIENTO_ACCION_APROBACION = "opt-in";
	String CONSENTIMIENTO_ACCION_NEGACION = "opt-out";
	String CONSENTIMIENTO_ACCION_INACCION = "not-given";
	String CONSENTIMIENTO_POLITICA_PRIVACIDAD = "privacidad";
	String CONSENTIMIENTO_POLITICA_COMUNICACION = "comunicacion";
	String CONSENTIMIENTO_SUB_POLITICA_COMUNICACION_ESTUDIO_MERCADO = "estudio-mercado";
	String CONSENTIMIENTO_SUB_POLITICA_COMUNICACION_PROMOCION = "promociones";
	String CONSENTIMIENTO_TIPO_DOCUMENTO_DNI = "DNI";
	String CONSENTIMIENTO_TIPO_DOCUMENTO_CE = "CE";
	String CONSENTIMIENTO_TIPO_DOCUMENTO_RUC = "RUC";
	Integer CONSENTIMIENTO_TIPO_DOCUMENTO_DNI_NUMERO = 1;
	Integer CONSENTIMIENTO_TIPO_DOCUMENTO_CE_NUMERO = 2;

	Integer CONSENTIMIENTO_ESTADO_LPDP_INACCION = 1;
	Integer CONSENTIMIENTO_ESTADO_LPDP_ACEPTADO = 2;
	Integer CONSENTIMIENTO_ESTADO_LPDP_RECHAZADO = 3;
	Integer CONSENTIMIENTO_ESTADO_LPDP_PERMISO_CONSENTIMIENTO = 4;

	String CONSENTIMIENTO_GENERO_MASCULINO = "M";
	String CONSENTIMIENTO_GENERO_FEMENINO = "F";

}

package pe.interseguro.siv.admin.transactional.listener;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.interseguro.siv.admin.transactional.entidad.PaymentMessage;
import pe.interseguro.siv.admin.transactional.entidad.PubsubMessage;
import pe.interseguro.siv.admin.transactional.factory.ServiceFactory;
import pe.interseguro.siv.common.dto.request.PaymentOperation;
import pe.interseguro.siv.common.dto.request.PolicyIssuedRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudRegistroPagoRequestDTO;
import pe.interseguro.siv.common.dto.response.BaseResponseDTO;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Solicitud;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudProducto;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudProductoRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudRepository;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.Utilitarios;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageListener {

	@Autowired
	private SolicitudRepository solicitudRepository;

	@Autowired
	private SolicitudProductoRepository solicitudProductoRepository;

	@Autowired
	private ServiceFactory serviceFactory;

	private final Gson gson;

	//@RabbitListener(queues = "${payment.rabbitmq.queue}")
	public void consumeMessageFromQueue(@NonNull PaymentMessage message) {

		log.info("Entro a consumeMessageFromQueue");
		log.info("Mensaje Rabbit: {}", gson.toJson(message));

		try {
			Date fechaPago = message.getTransaction().getTransactionDate2();

			String propuesta = message.getPoliza();
			String idPago = StringUtils.isBlank(message.getTransaction().getProviderId())
					? message.getTransaction().getSubscriptionId()
					: message.getTransaction().getProviderId();

			Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(propuesta);
			if (StringUtils.isNotBlank(solicitud.getTokenStatusRecargo())) {
				paymentRecharge(message, solicitud, fechaPago, idPago);
			}

			if (StringUtils.isBlank(solicitud.getTokenStatusRecargo())) {
				SolicitudProducto solicitudProducto = solicitudProductoRepository
						.findByIdSolicitud(solicitud.getIdSolicitud());

				String frecuencia = serviceFactory.getSolicitudService().valorMultiTabla(
						Constantes.MULTITABLA_CODIGO_FRECUENCIA, solicitudProducto.getFrecuencia(),
						Constantes.MULTITABLA_COLUMNA_VALOR);

				int idViaCobro = message.getTransaction().getViaCobro();

				if (message.getTransaction().getPassarella().equals(Constantes.ACCION_CUENTA)) {
					affiliateWhitAccount(message, propuesta, solicitud, frecuencia, idViaCobro);

				} else {

					switch (PaymentOperation.fromValue(message.getTransaction().getAction())) {
						case CVC:
							metodoPagaryAfiliar(message, fechaPago, idViaCobro, idPago, solicitud, propuesta);
							break;
						case TOKENIZE:
							metodoPagar(message, fechaPago, idViaCobro, idPago, solicitud, propuesta);
							break;
						case ONLYCVC:
							metodoAfiliar(message, fechaPago, solicitud, propuesta);
							break;
						default:
							break;
					}
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}

	}

	@Async
	public void affiliateWhitAccount(PaymentMessage message, String propuesta, Solicitud solicitud, String frecuencia,
									 int idViaCobro) throws UnknownHostException {
		String traza = Utilitarios.trazaLog() + "-" + propuesta;
		log.info("{} affiliateWhitAccount message:{},frecuencia:{},idViaCobro:{}", traza, gson.toJson(message),
				frecuencia, idViaCobro);

		InetAddress localHost = InetAddress.getLocalHost();
		String ipAddress = localHost.getHostAddress();

		SolicitudRegistroPagoRequestDTO solicitudRegistroPagoRequestDTO = new SolicitudRegistroPagoRequestDTO();
		solicitudRegistroPagoRequestDTO.setNumeroPropuesta(Long.parseLong(propuesta));
		solicitudRegistroPagoRequestDTO.setTipoViaCobro(2);
		solicitudRegistroPagoRequestDTO.setViaCobro(Integer.toString(idViaCobro));
		solicitudRegistroPagoRequestDTO.setTipoCuenta(Integer.parseInt(message.getTransaction().getTipoCuenta()));
		solicitudRegistroPagoRequestDTO.setMoneda(message.getTransaction().getMoneda());
		solicitudRegistroPagoRequestDTO.setEstado(1);
		solicitudRegistroPagoRequestDTO.setCodPeriodoPago(frecuencia);
		solicitudRegistroPagoRequestDTO.setCodRamo("1");
		solicitudRegistroPagoRequestDTO.setCodSubRamo("01");
		solicitudRegistroPagoRequestDTO.setNumDiaCobro(2);
		solicitudRegistroPagoRequestDTO.setUsuario("ADN");
		solicitudRegistroPagoRequestDTO.setNumeroTarjetaPp("");
		solicitudRegistroPagoRequestDTO.setFechaVencimientoPp("");
		solicitudRegistroPagoRequestDTO.setTipoViaCobroPp(2);
		solicitudRegistroPagoRequestDTO.setViaCobroPp(Integer.parseInt(solicitud.getIdViaCobroPP()));
		solicitudRegistroPagoRequestDTO.setIp(ipAddress);
		solicitudRegistroPagoRequestDTO.setPasarela(message.getTransaction().getPassarella());
		solicitudRegistroPagoRequestDTO.setTarjetaCobroRecurrente(message.getTransaction().getCuentaTarjeta());
		if (solicitud.getFechaPagoNiubiz() != null) {
			solicitudRegistroPagoRequestDTO.setPasarelaPp(Constantes.PASARELA_NIUBIZ);
		} else {
			solicitudRegistroPagoRequestDTO.setPasarelaPp(Constantes.PASARELA_CULQI);
		}
		log.info("{} envio a registrarAfiliacionPropuesta {}", traza, gson.toJson(solicitudRegistroPagoRequestDTO));
		serviceFactory.getSolicitudService().registrarAfiliacionPropuesta(solicitudRegistroPagoRequestDTO);

		executeGenerarPdf(propuesta, traza, 1);

		executePolicyIssued(propuesta, traza);
	}

	@Async("threadPoolTaskExecutorGenerate")
	public void executePolicyIssued(String propuesta, String traza) {
		log.info("[" + traza + "] Llamamos al metodo policyIssued");

		PolicyIssuedRequestDTO policyIssued = new PolicyIssuedRequestDTO();
		policyIssued.setNumeroPropuesta(Long.parseLong(propuesta));

		BaseResponseDTO respuestaPolicy = serviceFactory.getSolicitudService().policyIssued(policyIssued);

		log.info("{} CodigoRespuesta de getSolicitudService.policyIssued: {}", traza,
				respuestaPolicy.getCodigoRespuesta());
	}

	@Async
	public void metodoPagar(PaymentMessage message, Date fechaPago, int idViaCobro, String idPago, Solicitud solicitud,
							String propuesta) {

		String traza = Utilitarios.trazaLog() + "-" + propuesta;
		log.info(
				"[{}] Entro MessageListener#metodoPagar(message:{},fechaPago:{},idViaCobro:{},idPago:{},solicitud:{},propuesta:{})",
				traza, gson.toJson(message), fechaPago, idViaCobro, idPago, solicitud.getNumeroCotizacion(), propuesta);

		// VALIDAR LA POLIZA QUE ES DIFERENTE CON LA PROPUESTA
		String estado = Constantes.CODIGO_SOLICITUD_PAGO_CULQI;

		String pagoPrima = "1";
		String idViaCobroPp = Integer.toString(idViaCobro);
		String purchaseNumber = message.getTransaction().getPurchaseNumber();
		String estadoPubSubSend = "1";

		solicitud.setEstado(estado);
		if (message.getTransaction().getPassarella().equalsIgnoreCase(Constantes.PASARELA_NIUBIZ)) {
			solicitud.setIdPagoNiubiz(idPago);
			solicitud.setFechaPagoNiubiz(fechaPago);
		} else {
			solicitud.setIdPagoCulqi(idPago);
			solicitud.setFechaPagoCulqi(fechaPago);
		}
		solicitud.setIdViaCobro(idViaCobro);
		solicitud.setIdTarjetaSitc(message.getTransaction().getCreationToken());
		solicitud.setPagoPrima(pagoPrima);
		solicitud.setIdViaCobroPP(idViaCobroPp);
		solicitud.setNumeroTarjetaPP(message.getTransaction().getCreationToken());
		solicitud.setPurchaseNumber(purchaseNumber);
		solicitud.setEstadoPubSubSend(estadoPubSubSend);

		log.info("[{}] Guarda datos en la tabla solicitud", traza);
		solicitudRepository.save(solicitud);

	}

	@Async
	public void metodoPagaryAfiliar(PaymentMessage message, Date fechaPago, int idViaCobro, String idPago,
									Solicitud solicitud, String propuesta) {

		String traza = Utilitarios.trazaLog() + "-" + propuesta;
		log.info("[" + traza
				+ "] Entro MessageListener#metodoPagaryAfiliar(message,fechaPago,idViaCobro,idPago,solicitud,propuesta)");

		String estado = Constantes.CODIGO_SOLICITUD_PAGO_SAMP;
		String tarjetaNumero = message.getTransaction().getPaycard();
		String pagoPrima = "1";
		String idAfiliacion = message.getTransaction().getSubscriptionId();

		String idViaCobroPp = Integer.toString(idViaCobro);
		String pagoNumero = message.getTransaction().getPurchaseNumber();

		solicitud.setEstado(estado);
		if (message.getTransaction().getPassarella().equalsIgnoreCase(Constantes.PASARELA_NIUBIZ)) {
			solicitud.setIdPagoNiubiz(idPago);
			solicitud.setFechaPagoNiubiz(fechaPago);
		} else {
			solicitud.setIdPagoCulqi(idPago);
			solicitud.setFechaPagoCulqi(fechaPago);
		}
		solicitud.setIdViaCobro(idViaCobro);
		solicitud.setIdAfiliacionPropuesta(idAfiliacion);
		solicitud.setTarjetaAfiliacion(tarjetaNumero);
		solicitud.setIdTarjetaSitc(message.getTransaction().getCreationToken());
		solicitud.setFechaAfiliacionPropuesta(fechaPago);
		solicitud.setFechaRegistroSamp(fechaPago);
		solicitud.setPagoPrima(pagoPrima);
		solicitud.setIdViaCobroPP(idViaCobroPp);
		solicitud.setNumeroTarjetaPP(message.getTransaction().getCreationToken());
		solicitud.setPurchaseNumber(pagoNumero);
		solicitud.setTokenAfiliacion(message.getTransaction().getCreationToken());

		log.info("[" + traza + "] Guarda datos en la tabla solicitud");
		solicitudRepository.save(solicitud);

		executeGenerarPdf(propuesta, traza, 1);
		executePolicyIssued(propuesta, traza);

	}

	@Async("threadPoolTaskExecutorGenerate")
	public void executeGenerarPdf(String propuesta, String traza, int tag) {
		log.info("{} Execute method with configured executor - {}", traza, Thread.currentThread().getName());
		log.info("[{}] Genera el archivo ACPPDF", traza);
		BaseResponseDTO respuesta = serviceFactory.getAcpPDFService().generarPDF(propuesta, tag);
		log.info("[{}]Response getAcpPDFService.generarPDF: {}", traza, gson.toJson(respuesta));
	}

	@Async
	public void metodoAfiliar(PaymentMessage message, Date fechaAfiliacion, Solicitud solicitud, String propuesta) {

		String traza = Utilitarios.trazaLog() + "-" + propuesta;
		log.info("[" + traza + "] Entro MessageListener#metodoAfiliar(message,fechaAfiliacion,solicitud,propuesta)");

		String estado = Constantes.CODIGO_SOLICITUD_PAGO_SAMP;

		String idAfiliacion = message.getTransaction().getSubscriptionId();
		String tarjetaAfiliacion = message.getTransaction().getPaycard();
		String tokenAfiliacion = message.getTransaction().getCreationToken();
		String flagProcesoEmision = "1";

		solicitud.setEstado(estado);

		solicitud.setIdAfiliacionPropuesta(idAfiliacion);
		solicitud.setTarjetaAfiliacion(tarjetaAfiliacion);
		solicitud.setFechaAfiliacionPropuesta(fechaAfiliacion);
		solicitud.setFechaRegistroSamp(fechaAfiliacion);
		solicitud.setFechaModif(fechaAfiliacion);
		//solicitud.setFechaEmision(fechaAfiliacion);
		solicitud.setTokenAfiliacion(tokenAfiliacion);
		solicitud.setFlgProcesoEmision(flagProcesoEmision);

		solicitudRepository.save(solicitud);

		executeGenerarPdf(propuesta, traza, 2);

		executePolicyIssued(propuesta, traza);
	}


	/**
	 * Actualiza el recargo pagado
	 * @param message mensaje
	 * @param solicitud solicitud buscada
	 * @param fechaPago fecha de pago
	 * @param idPago id de transaccion
	 */
	@Async
	public void paymentRecharge(PaymentMessage message, Solicitud solicitud, Date fechaPago, String idPago) {
		String traza = Utilitarios.trazaLog() + "-" + solicitud.getNumeroPropuesta();
		log.info("{} Entra paymentRecharge", traza);
		if (message.getTransaction().getPassarella().equalsIgnoreCase(Constantes.PASARELA_NIUBIZ)) {
			solicitud.setIdPagoNiubizRecargo(idPago);
			solicitud.setFechaPagoNiubizRecargo(fechaPago);
		} else {
			solicitud.setIdPagoCulqiRecargo(idPago);
			solicitud.setFechaPagoCulqiRecargo(fechaPago);
		}

		// solicitud.setIdTarjetaSitc(token)
		// solicitud.setPurchaseNumber(message.getTransaction().getPurchaseNumber())
		solicitud.setEstadoRecargo(Constantes.CODIGO_SOLICITUD_PAGO_CULQI_RECARGO);
		// solicitud.setTarjetaAfiliacion(cargoRequestDTO.getTarjetaNumero())
		solicitud.setPagoPrimaRecargo(Constantes.PAGO_PRIMA_VAL);
		solicitudRepository.save(solicitud);
		log.info("{} Fin paymentRecharge", traza);

	}
	
	//@RabbitListener(queues = "${pubsub.rabbitmq.queue}")
	//public void readPubsubMessage(@NonNull PubsubMessage message) {
	//	log.info("Entro a pub sub consumeMessageFromQueue");
	//	log.info("Mensaje Rabbit pubsub: {}", gson.toJson(message));
	//	Solicitud solicitud =solicitudRepository.findByNumeroCotizacion(message.getNumeroPropuesta());
	//	solicitud.setEstadoPubSubSend(message.getEstadoPubSubSend());
	//	solicitud.setNumeroPoliza(message.getNumeroPoliza());
	//	solicitudRepository.save(solicitud);
	//	log.info("Finaliza pub-sub ");
	//}
	
}

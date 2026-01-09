package pe.interseguro.siv.admin.transactional.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.interseguro.siv.admin.transactional.service.LogService;
import pe.interseguro.siv.common.dto.response.LogResponseDTO;
import pe.interseguro.siv.common.persistence.db.mysql.domain.EventLog;
import pe.interseguro.siv.common.persistence.db.mysql.repository.EventLogRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.MultitablaRepository;
import pe.interseguro.siv.common.util.DateUtil;
import pe.interseguro.siv.common.util.Utilitarios;

@Service("LogService")
@Transactional
public class LogServiceImpl implements LogService {
//private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	/*@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private MultitablaRepository multitablaRepository;
	
	@Autowired
	private EventLogRepository logRepository;

	@Override
	public List<LogResponseDTO> listarLogDiario() {
		List<EventLog> eventos = logRepository.findByAgente();
		List<LogResponseDTO> response = new ArrayList<LogResponseDTO>();
		int i = 0;
		for (EventLog e : eventos) {
			i++;
			try {
				response.add(new LogResponseDTO(
						i, 
						e.getScreen(), 
						e.getPath(), 
						e.getCode(), 
						e.getMessage(), 
						e.getErrorDetail(), 
						e.getComment(), 
						e.getAgent(),
						e.getDevice(),
						e.getOs(),
					    DateUtil.dateToString(e.getEventDate(), DateUtil.FORMATO_DIA_DDMMYYYY_HHMMSS)));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return response;
	}
	
	@Override
	public List<LogResponseDTO> listarLogDiario(String agente) {
		List<EventLog> eventos = logRepository.findByAgente(agente);
		List<LogResponseDTO> response = new ArrayList<LogResponseDTO>();
		int i = 0;
		for (EventLog e : eventos) {
			i++;
			try {
				response.add(new LogResponseDTO(
						i,
						e.getScreen(), 
						e.getPath(), 
						e.getCode(), 
						e.getMessage(), 
						e.getErrorDetail(), 
						e.getComment(), 
						e.getAgent(),
						e.getDevice(),
						e.getOs(),
					    DateUtil.dateToString(e.getEventDate(), DateUtil.FORMATO_DIA_DDMMYYYY_HHMMSS)));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return response;
	}*/
	
}

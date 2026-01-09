package pe.interseguro.siv.common.persistence.db.mysql.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import pe.interseguro.siv.common.config.BaseTest;
import pe.interseguro.siv.common.persistence.db.mysql.domain.ViewSolicitud;

public class SolicitudRepositoryTest extends BaseTest {
	
	@Autowired
	private ViewSolicitudRepository solicitudRepository;

	@PersistenceContext
	private EntityManager em;
	
	@Value("#{ environment['persistence.db.driverClass'] }")
	private String nombrePersistance;
	
	@Test
	public void getAllSolicitudTest() {
		
		List<ViewSolicitud> solicitud = solicitudRepository.findByNumeroPropuesta("502219401");
		System.out.println("Solicitudes => " +  gson.toJson(solicitud) );
		assertNotNull(solicitud);
	}
	
	@Test
	public void getFilterByEstadoTest() {
		
		List<ViewSolicitud> solicitud = solicitudRepository.findByEstado(10);
		System.out.println("Solicitudes => " +  gson.toJson(solicitud) );
		
		assertNotNull(solicitud);
	}
	
	@Test
	@Ignore
	public void getFilterConditionalTest() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<ViewSolicitud> criteria = cb.createQuery(ViewSolicitud.class);
		Root<ViewSolicitud> root = criteria.from(ViewSolicitud.class);
		//criteria.where(cb.equal(root.get("numeroDocumento"), "43459267"));
		
		List<ViewSolicitud> solicitudes = ((EntityManager) em).createQuery(criteria).getResultList();
		System.out.println("Solicitudes getFilterConditionalTest => " +  gson.toJson(solicitudes) );
		assertNotNull(solicitudes);
		assertFalse(solicitudes.isEmpty());	
	} 
	
}

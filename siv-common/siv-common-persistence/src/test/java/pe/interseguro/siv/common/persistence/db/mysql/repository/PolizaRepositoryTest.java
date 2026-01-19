package pe.interseguro.siv.common.persistence.db.mysql.repository;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pe.interseguro.siv.common.config.BaseTest;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Poliza;

public class PolizaRepositoryTest extends BaseTest {

	@Autowired
	private PolizaRepository polizaRepository;
	
	@Test
	public void findAllTablasTest() {
		List<Poliza> lista = polizaRepository.findByPoliza("01","07934674");
		System.out.println("lista = " + gson.toJson(lista));
	}
}

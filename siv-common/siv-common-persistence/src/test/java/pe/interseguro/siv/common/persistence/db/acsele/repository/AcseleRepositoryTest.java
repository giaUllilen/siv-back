package pe.interseguro.siv.common.persistence.db.acsele.repository;

import org.springframework.beans.factory.annotation.Autowired;

import pe.interseguro.siv.common.config.BaseTest;


public class AcseleRepositoryTest extends BaseTest{
	@Autowired
	private AcseleRepository acseleRepository;
	
	/*@Test
	public void findAllTest() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("CURSOR_POLIZAS", null);
		map.put("P_ERR_MENSAJE", null);
		map.put("P_ERR_CODE", null);
		map.put("P_NUMERO_DOCUMENTO", "04045009");
		acseleRepository.obtenerPolizas(map);	
		
		System.out.println("lista => " + gson.toJson((List<PolizaAcsele>) map.get("CURSOR_POLIZAS")) );
		assertNotNull((List<PolizaAcsele>) map.get("CURSOR_POLIZAS"));
	}*/
}

package pe.interseguro.siv.common.persistence.db.mysql.repository;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import pe.interseguro.siv.common.config.BaseTest;
import pe.interseguro.siv.common.enums.TablaEnum;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Multitabla;

public class MultitablaRepositoryTest extends BaseTest {
	
	@Autowired
	private MultitablaRepository multitablaRepository;
	
	private Gson gson = new Gson();
	
	@Test
	public void getTablaTest() {
		
		List<Multitabla> lista = multitablaRepository.findAll();
		System.out.println("lista = " +  gson.toJson(lista) );
		
		List<Multitabla> listaTabla = multitablaRepository.findByCodigoTabla( TablaEnum.TABLA_TIPO_DOCUMENTO.getCodigoTabla() );
		System.out.println("listaTabla = " + gson.toJson(listaTabla) );
	}
	
	
}

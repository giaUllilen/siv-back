package pe.interseguro.siv.common.persistence.db.mysql.repository;

import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pe.interseguro.siv.common.config.BaseTest;
import pe.interseguro.siv.common.persistence.db.mysql.response.Perfil;

public class UsuarioPerfilRepositoryTest extends BaseTest {
	
	@Autowired
	private UsuarioPerfilRepository usuarioPerfilRepository;
	
	
	@Test
	public void getPerfilTest() {
		
		Perfil perfil = usuarioPerfilRepository.findByUsuario("jaybaram");
		System.out.println("Usuario Perfil = " +  gson.toJson(perfil) );
		
		assertNull(perfil);
	}
	
	
}

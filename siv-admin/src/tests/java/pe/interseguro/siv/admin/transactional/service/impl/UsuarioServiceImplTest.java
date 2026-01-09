package pe.interseguro.siv.admin.transactional.service.impl;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pe.interseguro.siv.admin.config.BaseTest;
import pe.interseguro.siv.admin.transactional.service.UsuarioService;
import pe.interseguro.siv.common.dto.request.UsuarioIngresoRequestDTO;
import pe.interseguro.siv.common.dto.response.UsuarioIngresoResponseDTO;
import pe.interseguro.siv.common.persistence.rest.interseguro.InterseguroRestClient;
import pe.interseguro.siv.common.util.Constantes;

import static org.junit.Assert.assertNotNull;

public class UsuarioServiceImplTest extends BaseTest {
	
	private Gson gson = new Gson();

	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private InterseguroRestClient interseguroRestClient;

	
	@Test
	public void validarUsuarioTest() {
		
		UsuarioIngresoRequestDTO request = new UsuarioIngresoRequestDTO();
		request.setUsuario("ptejeda");
		//request.setPassword("Gaston2302");

		UsuarioIngresoResponseDTO response = usuarioService.validarUsuario(request);
		System.out.println("response = " + gson.toJson(response));
		
		assertNotNull(response);
	}

	@Test
	public void getRefreshTokenTest() {
		
		String token = "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsInppcCI6IkRFRiJ9.eNo0yksOwiAUQNG9vHH5IwF2A5QqTWoJn1Bj3Ls4cHZzct-QagULj9ZytYSMMXB6tlhqvPdy4nAeOEcCC9Tu57e7l3fFHRNcXyeM6GfHK4NlN0U5Y1yrBZJrf6DiB3tL85bSGRolQ1pwhqRXK_JGGrRJLgILWiu9wecLAAD__w.t1w2gDrZFwnKPW4YclhD12dinXXWozhzhWJnR86ULxo";
		Jws<Claims> jws = Jwts.parser()
				.setSigningKey(Constantes.JWT_SECRET_KEY)
				.parseClaimsJws(token.replace(Constantes.JWT_RESPONSE_HEADER_PREFIX, "").trim());		
		
		UsuarioIngresoResponseDTO response = usuarioService.getRefreshToken(jws.getBody());
		System.out.println("response = " + gson.toJson(response));
		
	}
	

}

package pe.interseguro.siv.common.persistence.db.postgres.repository;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import pe.interseguro.siv.common.config.BaseTest;
import pe.interseguro.siv.common.enums.TablaEnum;
import pe.interseguro.siv.common.persistence.db.postgres.bean.Cotizacion;

public class CotizacionRepositoryTest extends BaseTest {

	@Autowired
	private CotizacionRepository cotizacionRepository;
	
	private Gson gson = new Gson();
	

}
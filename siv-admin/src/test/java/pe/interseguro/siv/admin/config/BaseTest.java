package pe.interseguro.siv.admin.config;

import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pe.interseguro.siv.common.config.JpaConfig;
import pe.interseguro.siv.common.config.MyBatisCrmConfig;
import pe.interseguro.siv.common.config.MyBatisPostgresConfig;
import pe.interseguro.siv.common.config.MyBatisSitcConfig;
import pe.interseguro.siv.common.config.MybatisConfig;
import pe.interseguro.siv.common.config.RestClientConfig;
import pe.interseguro.siv.common.config.SoapClientConfigCobranza;
import pe.interseguro.siv.common.config.SoapClientConfigSitc;

@ContextConfiguration(classes = {
	AdminTestConfig.class,
	JpaConfig.class,
	RestClientConfig.class,
	PropertyConfig.class,
	MybatisConfig.class,
	MyBatisPostgresConfig.class,
	MyBatisSitcConfig.class,
	MyBatisCrmConfig.class,
	SoapClientConfigSitc.class,
	SoapClientConfigCobranza.class,
	Gson.class
})
@ActiveProfiles("uat")
@RunWith(SpringJUnit4ClassRunner.class)
public class BaseTest {
	
	@Autowired
	private DataSource dataSource;

	protected static Gson gson;
	
	@Autowired
	private DataSource dataSourcePostgres;

	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create(); 
	}
	
	@Test
	 
	public void dataSourceTest() {
	    assertNotNull(dataSource);
	}
	/*
	@Test
	public void testMongoTemplate() {
	    assertNotNull(mongoTemplate);
	}
	*/
	@Test
	public void dataSourcePostgresTest() {
	    assertNotNull(dataSourcePostgres);
	}
}

package pe.interseguro.siv.common.config;

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

@ContextConfiguration(classes = {
	PersistenceTestConfig.class,
	MybatisConfig.class,
	JpaConfig.class,
	RestClientConfig.class,
	MyBatisPostgresConfig.class,
	MyBatisSitcConfig.class,
	SoapClientConfigSitc.class,
	SoapClientConfigCobranza.class
})
@ActiveProfiles("uat")
@RunWith(SpringJUnit4ClassRunner.class)
public class BaseTest {
	
	@Autowired
	private DataSource dataSource;
	
	protected static Gson gson;
	
	@Autowired
	private DataSource barDataSource;
	
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

	@Test
	public void barDataSourceTest() {
	    assertNotNull(barDataSource);
	}
	
	@Test
	public void dataSourcePostgresTest() {
	    assertNotNull(dataSourcePostgres);
	}

	
}

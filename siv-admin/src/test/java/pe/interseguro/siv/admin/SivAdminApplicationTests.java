package pe.interseguro.siv.admin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ActiveProfiles("uat")
@RunWith(SpringJUnit4ClassRunner.class)
public class SivAdminApplicationTests {

	@Test
	public void contextLoads() {
		System.out.println("Test SivAdminApplicationTests ");
	}

}

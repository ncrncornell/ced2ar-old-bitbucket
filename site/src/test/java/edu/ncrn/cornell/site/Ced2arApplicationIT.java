package edu.ncrn.cornell.site;

import edu.ncrn.cornell.Ced2arApplication;
import edu.ncrn.cornell.model.testing.BaseRepositoryIT;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Ced2arApplication.class)
@WebAppConfiguration
public class Ced2arApplicationIT extends BaseRepositoryIT {

	@Test
	public void contextLoads() {
		System.out.println("hello from Ced2arApplicationIT");
	}

}

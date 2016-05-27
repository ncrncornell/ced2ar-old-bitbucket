package edu.ncrn.cornell;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Ced2arApplication.class)
@WebAppConfiguration
public class Ced2arApplicationIT {

	@Test
	public void contextLoads() {
		System.out.println("hello from Ced2arApplicationIT");
	}

}

package net.javaguides.cms;

import javax.swing.Spring;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication


public class CmsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CmsBackendApplication.class, args);
	}

}

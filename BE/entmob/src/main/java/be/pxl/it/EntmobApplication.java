package be.pxl.it;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.jms.annotation.EnableJms;


@SpringBootApplication
@EnableJms
public class EntmobApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(EntmobApplication.class, args);
	}
}


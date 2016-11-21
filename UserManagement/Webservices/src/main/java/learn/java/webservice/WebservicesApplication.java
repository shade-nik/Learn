package learn.java.webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@ComponentScan("learn.java")
@Configuration
@Import(WebservicesConfiguration.class)
public class WebservicesApplication  {
	
	public static void main(String[] args) {
		SpringApplication.run(WebservicesApplication.class, args);
	}
	
	
}

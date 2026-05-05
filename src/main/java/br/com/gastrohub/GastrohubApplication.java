package br.com.gastrohub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class GastrohubApplication {

	public static void main(String[] args) {
		SpringApplication.run(GastrohubApplication.class, args);
	}

}

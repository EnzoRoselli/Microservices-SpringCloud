package com.udemy.springbootserviciouserscommons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class}) /*para no depender de una bbdd para usar JPA*/
public class SpringbootServicioUsersCommonsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioUsersCommonsApplication.class, args);
	}

}

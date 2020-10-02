package com.udemy.springbootserviciousers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EntityScan({"com.udemy.springbootserviciouserscommons.models.entities"}) /*para que busque por entidades en otro proyecto*/
@EnableEurekaClient /*no es obligacion esta anotacion*/ /*es para registrar a los microservicios en eureka*/
@SpringBootApplication
public class SpringbootServicioUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioUsersApplication.class, args);
	}

}

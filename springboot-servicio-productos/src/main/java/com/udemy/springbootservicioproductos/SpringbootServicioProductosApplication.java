package com.udemy.springbootservicioproductos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient /*no es obligacion esta anotacion*/ /*es para registrar a los microservicios en eureka*/
@SpringBootApplication
@EntityScan({"com.udemy.springbootserviciocommons.models.entities"}) /*para que busque por entidades en otro proyecto*/
public class SpringbootServicioProductosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioProductosApplication.class, args);
	}

}

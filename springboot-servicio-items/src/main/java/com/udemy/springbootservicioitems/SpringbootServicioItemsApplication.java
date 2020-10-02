package com.udemy.springbootservicioitems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/*para habilitar Hystrix*/ /*se encarga mediante un hilo separado la comunicacion de los microservicios*/
/*envuelve Ribbon y se encarga de manejar los distintos errores(latencia,timeout,etc.)*/
@EnableCircuitBreaker
/*eureka ya incluye Ribbon de manera implicita, se encarga del balanceo de carga y ademas sin usar
url y port sino el name del microservicio*/
@EnableEurekaClient /*no es obligatorio esta anotacion*/ /*es para registrar a los microservicios en eureka*/
/*@RibbonClient(name = "servicio-productos")*/ /*solo se usa si no esta configurado Eureka*/
/*el mismo name que el de IProductClientRest @FeignClient*/
/*de esta manera Feign se integra con Ribbon, por debajo consume Feign usando el balanceo de carga de Ribbon*/
@EnableFeignClients /*es importante para habilitar el cliente http feign que tenga nuestro proyecto*/
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class}) /*para no depender de una bbdd para usar JPA*/
@SpringBootApplication
public class SpringbootServicioItemsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioItemsApplication.class, args);
	}

}

package com.udemy.springbootserviciooauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableFeignClients /*para poder usar Feign*/
@EnableEurekaClient /*esta anotacion es opcional, es para registrar este servicio en eureka*/
@SpringBootApplication
public class SpringbootServicioOauthApplication /*implements CommandLineRunner*/ {

//	@Autowired
//	private BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioOauthApplication.class, args);
	}

	/*metodo de la interfaz CommandLineRunner para encriptar constraseñas*/
//	@Override
//	public void run(String... args) throws Exception {
//		String password = "12345";
//
//		/*BCrypt nos permite generar encriptaciones distintas(todas validas), de la misma contraseña*/
//		for (int i = 0; i < 4; i++) {
//			String passwordBCrypt = passwordEncoder.encode(password);
//			System.out.println(passwordBCrypt);
//		}
//	}
}

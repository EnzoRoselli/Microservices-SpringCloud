package com.udemy.springbootservicioitems.configuration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    @LoadBalanced /*con esta anotacion restTemplate usa Ribbon como balanceador de carga
    para buscar la mejor instancia*/
    public RestTemplate registerRestTemplate(){
        return new RestTemplate();
    }
}

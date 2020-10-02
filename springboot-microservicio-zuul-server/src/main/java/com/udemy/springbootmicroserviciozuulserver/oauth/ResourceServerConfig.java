package com.udemy.springbootmicroserviciozuulserver.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableResourceServer /*para habilitar esta clase como el servidor de recurso*/
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${config.security.oauth.jwt.key}")
    private String jwtKey;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        /*aca configuro el token en el Servidor de Recurso, tiene que ser igual al del
        * Servidor de Autorizacion*/
        resources.tokenStore(tokenStore());
    }

    @Override/*Aca protegemos nuestros endpoints*/
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        /*la ruta para obtener el token tiene que ser publica*/
        .antMatchers("/api/security/oauth/**").permitAll()
        /*estas rutas tambien son publicas*/
        .antMatchers(HttpMethod.GET, "/api/products", "/api/items",
                "/api/users/users").permitAll()
        /*estas rutas son privadas para cualquier usuario que sea ADMIN o USER*/
        .antMatchers(HttpMethod.GET, "/api/products/{id}", "/api/items/{id}",
                "/api/users/users/{id}").hasAnyRole("ADMIN", "USER")
        /*rutas privadas de POST/PUT/DELETE solo para el o los ADMIN*/
        .antMatchers("/api/products/**", "/api/items/**","/api/users/users/**").hasRole("ADMIN")
        /*para que cualquier ruta que no haya sido configurada necesite autentificacion*/
        .anyRequest().authenticated()
        /*aca configuramos que se pueda hacer CORS*/
        .and().cors().configurationSource(corsConfigurationSource());
    }

    @Bean /*se hace bean para luego inyectarse en corsFilter()*/
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        /*ponemos la direccion de los clientes front que puede hacer CORS(usar nuestro backend)
        * por ej. en el caso de Angular podria ser https://localhost:4200, pero en este caso lo
        * dejamos general*/
        corsConfig.addAllowedOrigin("*");
        /*los metodos que podra usar nuestro front*/
        corsConfig.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));

        /*para permitir que nuestro front nos envie sus credenciales(client_id y secret),
        * el token en la cabecera y las credenciales del usuario por el login*/
        corsConfig.setAllowCredentials(true);
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        /*para unir la configuracion de nuestro CORS con los endpoints, en este caso
        * hacemos que todos los path sigan las reglas de lo configurado en el CORS*/
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return source;
    }

    /*este bean es para configurar el CORS a nivel global, para toda nuestra aplicacion
    * y no solo para spring security*/
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter(){

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(
                new CorsFilter(corsConfigurationSource()));
        /*le damos una prioridad alta a este filtro de spring*/
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return bean;
    }

    /*el token storage se encarga de crear el token y almacenarlo, y accessTokenConverter
    se encarga de convertir al token en JWT cargandole sus datos, y darle su firma*/
    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    /*aca se crea el tokenConverter, que se encarga de tomar los datos del usuario, cargarlos,
     y convertirlos en el token JWT codificado en base64, y darle su firma*/
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();

        /*codigo secreto para firmar el token, luego se usa en el servidor de recurso
         * para validar la firma y asi dar acceso a los recursos*/
        tokenConverter.setSigningKey(jwtKey);

        return tokenConverter;
    }
}

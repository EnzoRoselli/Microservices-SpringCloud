package com.udemy.springbootserviciooauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*esta clase es para toda la configuracion de Spring Security, y siempre tiene que
extender de esta clase*/
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    /*aca inyecta la clase de services/UserService*/
    @Autowired
    private UserDetailsService userService;

    /*aca se inyecta de la clase de security/event/AuthenticationSuccessErrorHandler*/
    /*se usa para hacer que suceda un evento luego de que el usuario intenta autenticarse*/
    @Autowired
    private AuthenticationEventPublisher eventPublisher;

    @Override
    @Autowired /*para inyectar el AuthenticationManagerBuilder*/
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*se registra el userDetailsService*/
        auth.userDetailsService(userService)
         /*para que automaticamente se encripte la password cuando el usuario inicie
         sesion, luego lo compara con la BBDD que tiene q estar la pass encriptada tmb*/
        .passwordEncoder(passwordEncoder())
        /*aca se registra el eventPublisher, que es para hacer que suceda un evento, luego
        * de que el usuario intenta autenticarse*/
        .and().authenticationEventPublisher(eventPublisher);
    }

    @Bean /*se inyecta en el configure*/
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean /*se registra para luego usarlo en el servidor de autorizacion, y tiene
    los datos del usuario con sus roles*/
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}

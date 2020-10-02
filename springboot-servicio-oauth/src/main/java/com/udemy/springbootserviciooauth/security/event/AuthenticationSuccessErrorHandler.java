package com.udemy.springbootserviciooauth.security.event;

import brave.Tracer;
import com.udemy.springbootserviciooauth.services.IUserService;
import com.udemy.springbootserviciouserscommons.models.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import feign.FeignException;

/*Esta clase es para manejar eventos justo despues de que el usuario se autentique*/
@Component /*tiene que ser un componente ya que dsps tenemos que registrarlo en la configuracion
de spring security*/
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {

    private Logger log = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private Environment env;

    /*se usa para registrar un tag con informacion en Zipkin*/
    @Autowired
    private Tracer tracer;

    /*para manejar el exito, "authentication" tiene los datos del usuario autenticado*/
    @Override
    public void publishAuthenticationSuccess(Authentication authentication) {

        if(authentication.getName().equalsIgnoreCase(env.getProperty("config.security.oauth.client.id"))){
            return; // si es igual a frontendapp se salen del método!
        }

        /*UserDetails es un user de spring security con sus authorities(roles)*/
        UserDetails user = (UserDetails)authentication.getPrincipal();

        String msg = "Success Login: " + user.getUsername();
        System.out.println(msg);
        log.info(msg);

        User usuario = userService.findByUsername(authentication.getName());

        if (usuario.getAttempts() != null && usuario.getAttempts() > 0){
            usuario.setAttempts(0);
            userService.update(usuario, usuario.getId());
        }
    }

    /*para manejar el error, "authentication" tiene los datos del usuario que se quiere autenticar*/
    @Override
    public void publishAuthenticationFailure(AuthenticationException e, Authentication authentication) {
        String msg = "Error en el Login: " + e.getMessage();
        System.out.println(msg);
        log.error(msg);

        try{
            StringBuilder errors = new StringBuilder();
            errors.append(msg);

            User user = userService.findByUsername(authentication.getName());

            if (user.getAttempts() == null){
                user.setAttempts(0);
            }else{
                user.setAttempts(user.getAttempts()+1);
            }

            errors.append(" - Ahora usted tiene: " + user.getAttempts() + " intentos");

            if (user.getAttempts() >= 3){
                String maxAttemptsMsg = "El usuario " + user.getUsername() +
                        " fue deshabilitado por demasiados intentos";

                log.error(" - " + maxAttemptsMsg);
                errors.append(maxAttemptsMsg);

                user.setEnabled(false);
            }

            tracer.currentSpan().tag("error.mensaje", errors.toString());

            userService.update(user, user.getId());
        }catch (FeignException e1){
            log.error(String.format("El usuario %s no existe en el sistema", authentication.getName()));
        }
    }
}

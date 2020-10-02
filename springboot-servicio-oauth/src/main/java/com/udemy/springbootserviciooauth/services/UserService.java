package com.udemy.springbootserviciooauth.services;

import brave.Tracer;
import com.udemy.springbootserviciooauth.clients.IUserFeignClient;
import com.udemy.springbootserviciouserscommons.models.entities.User;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/*la anotacion es para poder usar la clase en security/SpringSecurityConfig*/
@Service
public class UserService implements UserDetailsService, IUserService {

    @Autowired /*uso Bean de clients/IUserFeignClient que se conecta a SERVICIO-USUARIOS*/
    private IUserFeignClient client;

    /*se usa para registrar un tag con informacion en Zipkin*/
    @Autowired
    private Tracer tracer;

    /*aca se hace la autenticacion*/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try{
            /*voy a buscar al usuario al microservicio SERVICIO-USUARIOS*/
            User user = client.findByUsername(username);

            /*Los roles de Spring Security son del tipo de la interfaz GrantedAuthority*/
            List<GrantedAuthority> authorities = user.getRoles().stream()
                    /*Para hacer el new tengo que usar la clase concreta de la interfaz GrantedAuthority*/
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());

                    /*devuelvo un User de spring security con sus authorities(roles)*/
            return new org.springframework.security.core.userdetails.User(user.getUsername(),
                    user.getPassword(), user.getEnabled(), true,
                    true, true, authorities);

        }catch (FeignException e){
            String error = "Error en el login, no existe el usuario " + username + " en la BBDD";

           /*Para agregarle un tag con informacion a Zipkin*/
            tracer.currentSpan().tag("error.mensaje", error + ": " + e.getMessage());

            throw new UsernameNotFoundException(error);
        }
    }

    @Override
    public User findByUsername(String username) {
        return client.findByUsername(username);
    }

    @Override
    public User update(User user, Long id) {
        return client.update(user, id);
    }
}

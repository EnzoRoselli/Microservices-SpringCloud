package com.udemy.springbootserviciousers.repository;

import com.udemy.springbootserviciouserscommons.models.entities.Role;
import com.udemy.springbootserviciouserscommons.models.entities.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

/*esta clase es solo para configurar que al traer los User o Role, se traigan tambien
las Id de los mismos*/
@Configuration
public class RepositoryConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(User.class, Role.class);
    }
}

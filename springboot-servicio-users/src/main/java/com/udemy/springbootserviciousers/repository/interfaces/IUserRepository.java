package com.udemy.springbootserviciousers.repository.interfaces;

import com.udemy.springbootserviciouserscommons.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "users") /*La anotacion y dependencia genera un endpoint con el CRUD de users*/
public interface IUserRepository extends JpaRepository<User, Long> {

    @RestResource(path = "user") /*ruta opcional para dsps del search/ para q el nombre de la ruta no diga findByUsername*/
    public User findByUsername(String username);
}

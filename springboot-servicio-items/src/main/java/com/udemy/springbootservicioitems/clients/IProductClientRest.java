package com.udemy.springbootservicioitems.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;import com.udemy.springbootserviciocommons.models.entities.Product;


@FeignClient(name = "servicio-productos") /*el name es el del application properties del servicio-productos*/
/*con esta anotacion se define que la clase es un clienteFeign y a cual microservicio nos queremos
conectar*/
/*con @FeignClient ya pasax a ser un Component de Spring, asi que se puede inyectar con @Autowired*/
public interface IProductClientRest {
    @GetMapping("/")
    public List<Product> getAll();

    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id);

    @PostMapping("/")
    public Product save(@RequestBody Product product);

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product product);

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id);
}

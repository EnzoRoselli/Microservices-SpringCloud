package com.udemy.springbootservicioproductos.controllers;

import com.udemy.springbootserviciocommons.models.entities.Product;
import com.udemy.springbootservicioproductos.services.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
//@RequestMapping("/products")
public class ProductController {
    @Value("${server.port}")
    private Integer port;

    @Autowired
    private IProductService productService;

    @GetMapping
    public List<Product> findAll(){
        return productService.findAll().stream().map(p -> {
            p.setPort(port);
            return p;
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable Long id) throws Exception {
        Product product = productService.findById(id);
        product.setPort(port);

        /*solo para probar timeout de Hystrix y Ribbn*/
//        try{
//            Thread.sleep(5000L);
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }

        return product;
    }

    @PostMapping
    public ResponseEntity<Product> save(@RequestBody Product product){
        return ResponseEntity.created(getLocation(productService.save(product))).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product){
        Product productDDBB = productService.findById(id);

        productDDBB.setName(product.getName());
        productDDBB.setPrice(product.getPrice());

        return ResponseEntity.ok(productService.save(productDDBB));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        Product productDDBB = productService.findById(id);

        productService.deleteById(id);
        
        return ResponseEntity.ok().build();
    }

    public URI getLocation(Product product) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();
    }
}

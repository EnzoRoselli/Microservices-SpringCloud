package com.udemy.springbootservicioitems.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.udemy.springbootservicioitems.models.Item;
import com.udemy.springbootservicioitems.services.interfaces.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;import com.udemy.springbootserviciocommons.models.entities.Product;


/*@RefreshScope es para recargar todos los @Component y properties de la aplicacion y se usa para
* cambiar de entorno/profile(default,dev,prod,etc) sin dar de baja el servidor de spring y volver a levantarlo*/
/*@RefreshScope*/
@RestController
//@RequestMapping("/items")
public class ItemController {

    @Autowired
    private Environment env;

    @Autowired
    @Qualifier("ItemServiceFeign")
    private IItemService itemService;

    @GetMapping
    public List<Item> findAll(){
        return itemService.findAll();
    }

    /*@HystrixCommand(fallbackMethod = "metodoAlternativo")*/ /*llama a este metodo si por algun motivo
    falla SERVICIO-PRODUCTO(puede ser que SERVICIO-PRODUCTO devuelva un error, o que el microservicio
    este caido, cualquiera que sea el caso llama a metodoAlternativo)*/
    @GetMapping("/{id}")
    public Item findById(@PathVariable Long id, @RequestParam Integer quantity){
        return itemService.findById(id, quantity);
    }

//    public Item metodoAlternativo(Long id, Integer quantity){
//        Product product = Product.builder().id(id).name("XXX").price(500.0).build();
//        Item item = Item.builder().product(product).quantity(quantity).build();
//
//        return item;
//    }

    @PostMapping
    public ResponseEntity<Product> save(@RequestBody Product product){
        return ResponseEntity.ok(itemService.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product){
        return ResponseEntity.ok(itemService.update(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/config") /*para saber en que ambiente/entorno estoy*/
    public ResponseEntity<?> getConfig(@Value("${configuracion.texto}") String texto,
                                       @Value("${server.port}") String puerto){
        Map<String, String> json = new HashMap<>();
        json.put("texto", texto);
        json.put("puerto", puerto);

        if (env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")){

            json.put("autor.nombre", env.getProperty("configuracion.autor.nombre"));
            json.put("autor.email", env.getProperty("configuracion.autor.email"));
        }

        return new ResponseEntity<Map<String, String>>(json, HttpStatus.OK);
    }
}

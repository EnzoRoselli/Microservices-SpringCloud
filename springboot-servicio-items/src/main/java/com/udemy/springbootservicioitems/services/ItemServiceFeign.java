package com.udemy.springbootservicioitems.services;

import com.udemy.springbootservicioitems.clients.IProductClientRest;
import com.udemy.springbootservicioitems.models.Item;
import com.udemy.springbootservicioitems.services.interfaces.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;import com.udemy.springbootserviciocommons.models.entities.Product;


/*Version mejorada de RestTemplate, se centraliza la url y se simplifica codigo
(mas facil enviar @PathVariable y mas facil obtener el response)*/
@Service("ItemServiceFeign")
public class ItemServiceFeign implements IItemService {
    @Autowired
    private IProductClientRest productClientRest;

    @Override
    public List<Item> findAll() {
        return productClientRest.getAll().stream().map(p -> new Item(p, 1))
                .collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer quantity) {
        return new Item(productClientRest.getById(id), quantity);
    }

    @Override
    public Product save(Product product) {
        return productClientRest.save(product);
    }

    @Override
    public Product update(Long id, Product product) {
        return productClientRest.update(id, product);
    }

    @Override
    public void delete(Long id) {
        productClientRest.delete(id);
    }
}

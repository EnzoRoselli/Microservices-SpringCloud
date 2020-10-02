package com.udemy.springbootservicioitems.services;

import com.udemy.springbootservicioitems.models.Item;
import com.udemy.springbootservicioitems.services.interfaces.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;import com.udemy.springbootserviciocommons.models.entities.Product;


@Service("ItemService")
public class ItemService implements IItemService {

    @Autowired
    private RestTemplate restClient; /*Autowired del Bean en configuration/RestTemplateConfig*/

    @Override
    public List<Item> findAll() {
        List<Product> products = Arrays.asList(restClient.getForObject("http://servicio-productos/", Product[].class));

        return products.stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer quantity) {
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("id", id.toString());
        Product product = restClient.getForObject("http://servicio-productos/{id}", Product.class, pathVariables);
        return new Item(product, quantity);
    }

    @Override
    public Product save(Product product) {
        HttpEntity<Product> body = new HttpEntity<>(product);

        ResponseEntity<Product> response = restClient.exchange("http://servicio-productos/", HttpMethod.POST, body, Product.class); /*Product.class es para setear como quiero que sea el response*/
        Product productResponse = response.getBody();

        return productResponse;
    }

    @Override
    public Product update(Long id, Product product) {
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("id", id.toString());

        HttpEntity<Product> body = new HttpEntity<>(product);

        ResponseEntity<Product> response = restClient.exchange("http://servicio-productos/{id}",
                HttpMethod.PUT, body, Product.class, pathVariables); /*Product.class es para setear como quiero que sea el response*/
        Product productResponse = response.getBody();

        return productResponse;
    }

    @Override
    public void delete(Long id) {
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("id", id.toString());

        restClient.delete("http://servicio-productos/{id}", pathVariables);
    }
}

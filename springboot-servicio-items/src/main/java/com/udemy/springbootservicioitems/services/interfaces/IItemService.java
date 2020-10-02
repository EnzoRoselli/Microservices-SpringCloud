package com.udemy.springbootservicioitems.services.interfaces;

import com.udemy.springbootservicioitems.models.Item;
import com.udemy.springbootserviciocommons.models.entities.Product;


import java.util.List;

public interface IItemService {

    public List<Item> findAll();
    public Item findById(Long id, Integer quantity);
    public Product save(Product product);
    public Product update(Long id, Product product);
    public void delete(Long id);
}

package com.udemy.springbootservicioproductos.services.interfaces;

import com.udemy.springbootserviciocommons.models.entities.Product;

import java.util.List;

public interface IProductService {
    public List<Product> findAll();
    public Product findById(Long id);
    public Product save(Product product);
    public void deleteById(Long id);

}

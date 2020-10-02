package com.udemy.springbootservicioproductos.repositories;

import com.udemy.springbootserviciocommons.models.entities.Product;

import org.springframework.data.repository.CrudRepository;

public interface IProductRepository extends CrudRepository<Product, Long> {
}

package com.udemy.springbootservicioitems.models;

import com.udemy.springbootserviciocommons.models.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private Product product;
    private Integer quantity;

    public Double getTotal(){
        return product.getPrice() * quantity.doubleValue();
    }
}

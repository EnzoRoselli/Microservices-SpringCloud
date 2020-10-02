package com.udemy.springbootserviciocommons.models.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "products")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    @Column(name = "created_at")
    @Temporal(TemporalType.DATE) /*YYYY-MM-dd*/
    private Date createdAt;
    @Transient /*significa que no esta mapeado a ningun campo en la DDBB*/
    private Integer port;

    @PrePersist
    public void prePersist(){
        createdAt = new Date();
    }
}

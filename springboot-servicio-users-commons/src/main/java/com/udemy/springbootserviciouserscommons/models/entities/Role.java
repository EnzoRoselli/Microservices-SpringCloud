package com.udemy.springbootserviciouserscommons.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(unique = true, length = 30)
    private String name;

//    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles") /*si quisieramos que sea bidireccional*/
//    private List<User> users;
}

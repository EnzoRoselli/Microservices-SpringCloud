package com.udemy.springbootserviciouserscommons.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 20)
    @NotEmpty
    @Column(unique = true, length = 20) /*solo se usa si JPA crea la DDBB, por ej con H2*/
    private String username;

    @Size(min = 3, max = 60)
    @NotEmpty
    @Column(length = 60)
    private String password;

    private Boolean enabled;

    @NotEmpty
    private String name;

    @NotEmpty
    private String surname;

    @Email
    @NotEmpty
    @Column(unique = true, length = 50)
    private String email;

    @ManyToMany(fetch = FetchType.LAZY) /*Un usuario tiene muchos roles, y un rol tiene muchos usuarios*/
    /*LAZY es para que solo cargue los roles cuando se haga el getRoles()*/
    @JoinTable(name = "users_to_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"), /*para setear parametros de la tabla intermedia*/
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role_id"})}) /*para hacer unique user_id y role_id juntos*/
    private List<Role> roles; /*El usuario tiene una lista de roles, en este caso no es necesario que sea
    bidireccional(que Role tenga una List<User>)*/

    private Integer attempts;
}

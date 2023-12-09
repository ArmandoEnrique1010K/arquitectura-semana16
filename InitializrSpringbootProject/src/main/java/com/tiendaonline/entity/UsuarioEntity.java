package com.tiendaonline.entity;

import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "usuario", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Entity
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "edad")
    private Integer edad;
    
    @Column(name = "dni")
    private Integer dni;
    
    @Column(name = "sexo")
    private String sexo;

    
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "usuario_roles", 
            joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "rol_id", referencedColumnName = "id_rol")
    )
    private Collection<RolEntity> roles;

    

}
    /*
    @OneToMany(mappedBy = "usuarioEntity", fetch = FetchType.LAZY)
    private List<CarritoEntity> carritoEntity;
    
    @OneToMany(mappedBy = "usuarioEntity", cascade = CascadeType.ALL, 
        orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CarritoEntity> carritoEntity;
    */
    /*
    public UsuarioEntity(String nombre, String apellido, String email, String password, Integer edad, Integer dni, String sexo, Collection<RolEntity> roles) {
        super();
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.edad = edad;
        this.dni = dni;
        this.sexo = sexo;
        this.roles = roles;
    }
*/


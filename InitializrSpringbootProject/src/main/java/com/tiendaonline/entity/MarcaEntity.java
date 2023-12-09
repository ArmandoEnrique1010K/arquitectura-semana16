package com.tiendaonline.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "marca")
public class MarcaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marca")
    private Long id_marca;
    // No se van a permitir valores repetitivos
    @Column(name = "nombre", unique = true, nullable = false)
    private String nombre;
    @Column(name = "estado", nullable = false)
    private Boolean estado;
    
    // Relacion hacia productos
    @OneToMany(mappedBy = "marcaEntity", cascade = CascadeType.ALL, 
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductoEntity> productoEntity;
    
}
package com.tiendaonline.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
@Table(name = "detallesproducto")
public class DetallesProductoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detallesproducto")
    private Long id_detallesproducto;
    @Column(name = "descripcion", length = 2000)
    private String descripcion;
    @Column(name = "fichatecnica", length = 5000)
    private String fichatecnica;
    
    // Relacion hacia productos
    @OneToOne(mappedBy = "detallesproductoEntity", cascade = CascadeType.ALL, 
            orphanRemoval = true, fetch = FetchType.LAZY)
    private ProductoEntity productoEntity;

}
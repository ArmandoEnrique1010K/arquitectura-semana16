package com.tiendaonline.repository;

import com.tiendaonline.entity.DetallesProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallesProductoRepository extends JpaRepository<DetallesProductoEntity, Long>{
    
}
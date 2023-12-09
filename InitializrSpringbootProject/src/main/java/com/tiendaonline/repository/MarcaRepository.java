package com.tiendaonline.repository;

import com.tiendaonline.entity.MarcaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MarcaRepository extends JpaRepository<MarcaEntity, Long>{
    
    // LISTAR LAS MARCAS QUE ESTAN HABILITADAS
    @Query("SELECT m FROM MarcaEntity m WHERE m.estado = true")
    List<MarcaEntity> findAllByEstadoTrue();
    
    // LISTAR LAS DISTINTAS MARCAS QUE SE ENCUENTRAN DENTRO DE UN GRUPO DE PRODUCTOS QUE PERTENECEN A LA MISMA CATEGORIA
    @Query("SELECT DISTINCT m FROM MarcaEntity m JOIN m.productoEntity p JOIN p.categoriaEntity c WHERE c.nombre = :nombreCategoria AND m.estado = true AND p.estado = true")
    List<MarcaEntity> findAllDistinctByCategoriaGroupAndEstadoTrue(@Param("nombreCategoria") String nombreCategoria);

    // LISTAR LAS DISTINTAS MARCAS QUE SE ENCUENTRAN DENTRO DE UN GRUPO DE PRODUCTOS QUE PERTENECEN A LA MISMA CATEGORIA
    @Query("SELECT DISTINCT m FROM MarcaEntity m JOIN m.productoEntity p JOIN p.categoriaEntity c WHERE c.nombre = :nombreCategoria AND p.estado = true")
    List<MarcaEntity> findAllDistinctByCategoriaGroup(@Param("nombreCategoria") String nombreCategoria);

}

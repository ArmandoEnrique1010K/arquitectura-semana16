package com.tiendaonline.repository;

import com.tiendaonline.entity.ProductoEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Long>{
    
    /* PARA EL USUARIO (PAGINAR Y FILTRAR LOS PRODUCTOS POR PARAMETROS)*/
    @Query(value = "SELECT p FROM ProductoEntity p JOIN p.categoriaEntity c JOIN p.marcaEntity m "
            + "WHERE (:categoriaId IS NULL OR c.id_categoria = :categoriaId) "
            + "AND (COALESCE(:marcaIds, NULL) IS NULL OR m.id_marca IN :marcaIds) "
            + "AND ((:minPrecio IS NULL AND :maxPrecio IS NULL) OR "
            + "     (p.oferta = true AND (:minPrecio IS NULL OR p.preciooferta >= :minPrecio) "
            + "             AND (:maxPrecio IS NULL OR p.preciooferta <= :maxPrecio)) OR "
            + "     (p.oferta = false AND (:minPrecio IS NULL OR p.precionormal >= :minPrecio) "
            + "             AND (:maxPrecio IS NULL OR p.precionormal <= :maxPrecio))) "
            + "AND (:enOferta IS NULL OR p.oferta = :enOferta) "
            + "AND (:palabraClave IS NULL OR CONCAT(' ', p.nombre) LIKE %:palabraClave%) "
            + "AND p.estado = true "
            + "AND c.estado = true "
            + "AND m.estado = true "
            + "ORDER BY p.fechaeditado DESC"
    )
    Page<ProductoEntity> pageAllByEstadoTrueAndParams(
            @Param("categoriaId") Long categoriaId, 
            @Param("marcaIds") List<Long> marcaIds,
            @Param("minPrecio") Double minPrecio,
            @Param("maxPrecio") Double maxPrecio,
            @Param("enOferta") Boolean enOferta,
            @Param("palabraClave") String palabraClave,
            Pageable pageable
    );

    /* CANTIDAD DE PRODUCTOS PARA EL USUARIO */
    @Query("SELECT COUNT(p) FROM ProductoEntity p JOIN p.categoriaEntity c JOIN p.marcaEntity m "
            + "WHERE (:categoriaId IS NULL OR c.id_categoria = :categoriaId) "
            + "AND (COALESCE(:marcaIds, NULL) IS NULL OR m.id_marca IN :marcaIds) "
            + "AND ((:minPrecio IS NULL AND :maxPrecio IS NULL) OR "
            + "     (p.oferta = true AND (:minPrecio IS NULL OR p.preciooferta >= :minPrecio) "
            + "             AND (:maxPrecio IS NULL OR p.preciooferta <= :maxPrecio)) OR "
            + "     (p.oferta = false AND (:minPrecio IS NULL OR p.precionormal >= :minPrecio) "
            + "             AND (:maxPrecio IS NULL OR p.precionormal <= :maxPrecio))) "
            + "AND (:enOferta IS NULL OR p.oferta = :enOferta) "
            + "AND (:palabraClave IS NULL OR CONCAT(' ', p.nombre) LIKE %:palabraClave%) "
            + "AND p.estado = true "
            + "AND c.estado = true "
            + "AND m.estado = true"
    )
    Long countAllByEstadoTrueAndParams(
            @Param("categoriaId") Long categoriaId, 
            @Param("marcaIds") List<Long> marcaIds,
            @Param("minPrecio") Double minPrecio,
            @Param("maxPrecio") Double maxPrecio,
            @Param("enOferta") Boolean enOferta,
            @Param("palabraClave") String palabraClave
    );


    
    // PAGINAR LOS PRODUCTOS QUE CUMPLAN CON LA CONDICIÓN POR PARAMETROS:
    // A DIFERENCIA DEL PRIMER @QUERY, AQUI SELECCIONA TODOS LOS PRODUCTOS TANTO HABILITADOS COMO INHABILITADOS
    @Query(value = "SELECT p FROM ProductoEntity p JOIN p.categoriaEntity c JOIN p.marcaEntity m "
            + "WHERE (:categoriaId IS NULL OR c.id_categoria = :categoriaId) "
            + "AND (COALESCE(:marcaIds, NULL) IS NULL OR m.id_marca IN :marcaIds) "
            + "AND ((:minPrecio IS NULL AND :maxPrecio IS NULL) OR "
            + "     (p.oferta = true AND (:minPrecio IS NULL OR p.preciooferta >= :minPrecio) "
            + "                     AND (:maxPrecio IS NULL OR p.preciooferta <= :maxPrecio)) OR "
            + "     (p.oferta = false AND (:minPrecio IS NULL OR p.precionormal >= :minPrecio) "
            + "                     AND (:maxPrecio IS NULL OR p.precionormal <= :maxPrecio))) "
            + "AND (:enOferta IS NULL OR p.oferta = :enOferta) "
            + "AND (:palabraClave IS NULL OR CONCAT(' ', p.nombre) LIKE %:palabraClave%) "
            + "ORDER BY p.fechaeditado DESC"
    )
    Page<ProductoEntity> pageAllByParams(
            @Param("categoriaId") Long categoriaId,
            @Param("marcaIds") List<Long> marcaIds,
            @Param("minPrecio") Double minPrecio,
            @Param("maxPrecio") Double maxPrecio,
            @Param("enOferta") Boolean enOferta,
            @Param("palabraClave") String palabraClave,
            Pageable pageable
    );

    
    
    /* CANTIDAD DE PRODUCTOS PARA EL ADMINISTRADOR */
    @Query(value = "SELECT COUNT(p) FROM ProductoEntity p JOIN p.categoriaEntity c JOIN p.marcaEntity m "
            + "WHERE (:categoriaId IS NULL OR c.id_categoria = :categoriaId) "
            + "AND (COALESCE(:marcaIds, NULL) IS NULL OR m.id_marca IN :marcaIds) "
            + "AND ((:minPrecio IS NULL AND :maxPrecio IS NULL) OR "
            + "     (p.oferta = true AND (:minPrecio IS NULL OR p.preciooferta >= :minPrecio) "
            + "             AND (:maxPrecio IS NULL OR p.preciooferta <= :maxPrecio)) OR "
            + "     (p.oferta = false AND (:minPrecio IS NULL OR p.precionormal >= :minPrecio) "
            + "             AND (:maxPrecio IS NULL OR p.precionormal <= :maxPrecio))) "
            + "AND (:enOferta IS NULL OR p.oferta = :enOferta) "
            + "AND (:palabraClave IS NULL OR CONCAT(' ', p.nombre) LIKE %:palabraClave%) "
            + "ORDER BY p.fechaeditado DESC"
    )
    Long countAllByParams(
            @Param("categoriaId") Long categoriaId, 
            @Param("marcaIds") List<Long> marcaIds,
            @Param("minPrecio") Double minPrecio,
            @Param("maxPrecio") Double maxPrecio,
            @Param("enOferta") Boolean enOferta,
            @Param("palabraClave") String palabraClave
    );

}

    

    /* PARA EL USUARIO (FILTRAR LOS PRODUCTOS POR PARAMETROS)*/
/*
    @Query(value = "SELECT p FROM ProductoEntity p JOIN p.categoriaEntity c JOIN p.marcaEntity m "
            + "WHERE (:categoriaId IS NULL OR c.id_categoria = :categoriaId) "
            + "AND (COALESCE(:marcaIds, NULL) IS NULL OR m.id_marca IN :marcaIds) "
            + "AND ((:minPrecio IS NULL AND :maxPrecio IS NULL) OR "
            + "     (p.oferta = true AND (:minPrecio IS NULL OR p.preciooferta >= :minPrecio) "
            + "             AND (:maxPrecio IS NULL OR p.preciooferta <= :maxPrecio)) OR "
            + "     (p.oferta = false AND (:minPrecio IS NULL OR p.precionormal >= :minPrecio) "
            + "             AND (:maxPrecio IS NULL OR p.precionormal <= :maxPrecio))) "
            + "AND (:enOferta IS NULL OR p.oferta = :enOferta) "
            + "AND (:palabraClave IS NULL OR CONCAT(' ', p.nombre, m.nombre, c.nombre) LIKE %:palabraClave%) "
            + "AND p.estado = true "
            + "AND c.estado = true "
            + "AND m.estado = true "
            + "ORDER BY p.id_producto DESC"
    )
    List<ProductoEntity> findAllByEstadoTrueAndParams(
            @Param("categoriaId") Long categoriaId, 
            @Param("marcaIds") List<Long> marcaIds,
            @Param("minPrecio") Double minPrecio,
            @Param("maxPrecio") Double maxPrecio,
            @Param("enOferta") Boolean enOferta,
            @Param("palabraClave") String palabraClave
    );

    */
    

    
    
    
    

    
    /* PARA EL ADMINISTRADOR */
/*
    @Query(value = "SELECT p FROM ProductoEntity p JOIN p.categoriaEntity c JOIN p.marcaEntity m "
            + "WHERE (:categoriaId IS NULL OR c.id_categoria = :categoriaId) "
            + "AND (COALESCE(:marcaIds, NULL) IS NULL OR m.id_marca IN :marcaIds) "
            + "AND ((:minPrecio IS NULL AND :maxPrecio IS NULL) OR "
            + "     (p.oferta = true AND (:minPrecio IS NULL OR p.preciooferta >= :minPrecio) "
            + "             AND (:maxPrecio IS NULL OR p.preciooferta <= :maxPrecio)) OR "
            + "     (p.oferta = false AND (:minPrecio IS NULL OR p.precionormal >= :minPrecio) "
            + "             AND (:maxPrecio IS NULL OR p.precionormal <= :maxPrecio))) "
            + "AND (:enOferta IS NULL OR p.oferta = :enOferta) "
            + "AND (:palabraClave IS NULL OR CONCAT(' ', p.id_producto, p.nombre, m.nombre, c.nombre) LIKE %:palabraClave%) "
            + "ORDER BY p.id_producto DESC"
    )
    List<ProductoEntity> findAllByParams(
            @Param("categoriaId") Long categoriaId, 
            @Param("marcaIds") List<Long> marcaIds,
            @Param("minPrecio") Double minPrecio,
            @Param("maxPrecio") Double maxPrecio,
            @Param("enOferta") Boolean enOferta,
            @Param("palabraClave") String palabraClave
    );
*/
    
    
    

    // LISTA LOS PRODUCTOS QUE CUMPLAN CON LA CONDICIÓN POR PARAMETROS:
    // A DIFERENCIA DEL PRIMER @QUERY, AQUI SELECCIONA TODOS LOS PRODUCTOS TANTO HABILITADOS COMO INHABILITADOS
    /*
    Page<ProductoEntity> pageAllByEstadoTrueAndParams(
            @Param("categoriaId") Long categoriaId,
            @Param("marcaIds") List<Long> marcaIds,
            @Param("minPrecio") Double minPrecio,
            @Param("maxPrecio") Double maxPrecio,
            @Param("enOferta") Boolean enOferta,
            @Param("palabraClave") String palabraClave,
            Pageable pageable
    );
*/










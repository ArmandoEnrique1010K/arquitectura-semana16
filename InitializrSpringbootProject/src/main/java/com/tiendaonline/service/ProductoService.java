package com.tiendaonline.service;

import com.tiendaonline.entity.ProductoEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


public interface ProductoService {

    public Page<ProductoEntity> PaginarProductosHabilitadosPorFiltrosDeBusqueda(
            Long categoriaId,
            List<Long> marcaIds,
            Double minPrecio,
            Double maxPrecio,
            Boolean enOferta,
            String palabraClave,
            Pageable pageable
    );
    public Long ContarProductosHabilitadosPorFiltrosDeBusqueda(
            Long categoriaId,
            List<Long> marcaIds,
            Double minPrecio,
            Double maxPrecio,
            Boolean enOferta,
            String palabraClave
    );

    public Page<ProductoEntity> PaginarProductosPorFiltrosDeBusqueda(
            Long categoriaId,
            List<Long> marcaIds,
            Double minPrecio,
            Double maxPrecio,
            Boolean enOferta,
            String palabraClave,
            Pageable pageable
    );

    public Long ContarProductosPorFiltrosDeBusqueda(
            Long categoriaId,
            List<Long> marcaIds,
            Double minPrecio,
            Double maxPrecio,
            Boolean enOferta,
            String palabraClave
    );

    public ProductoEntity ObtenerProductoPorId(Long id_producto);
    public ProductoEntity GuardarProducto(ProductoEntity productoEntity);
    public ProductoEntity ActualizarProducto(ProductoEntity productoEntity);
    public void CambiarEstadoProductoAFalse(Long id_producto);
    public void CambiarEstadoProductoATrue(Long id_producto);
    public void EliminarDefinitivamente(Long id_producto);

}

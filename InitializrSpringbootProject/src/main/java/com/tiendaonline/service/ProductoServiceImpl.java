package com.tiendaonline.service;

import com.tiendaonline.entity.DetallesProductoEntity;
import com.tiendaonline.entity.ImagenProductoEntity;
import com.tiendaonline.entity.ProductoEntity;
import com.tiendaonline.repository.DetallesProductoRepository;
import com.tiendaonline.repository.ImagenProductoRepository;
import com.tiendaonline.repository.ProductoRepository;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ImagenProductoRepository imagenProductoRepository;

    @Autowired
    private ImagenProductoServiceImpl imagenProductoServiceImpl;

    @Autowired
    private DetallesProductoRepository detallesProductoRepository;
    
    @Override
    public Page<ProductoEntity> PaginarProductosHabilitadosPorFiltrosDeBusqueda(
            Long categoriaId,
            List<Long> marcaIds,
            Double minPrecio,
            Double maxPrecio,
            Boolean enOferta,
            String palabraClave,
            Pageable pageable
    ) {

        return productoRepository.pageAllByEstadoTrueAndParams(
                categoriaId, marcaIds, minPrecio, maxPrecio, enOferta, palabraClave, pageable
        );

    }

    @Override
    public Long ContarProductosHabilitadosPorFiltrosDeBusqueda(
            Long categoriaId,
            List<Long> marcaIds,
            Double minPrecio,
            Double maxPrecio,
            Boolean enOferta,
            String palabraClave) {
        return productoRepository.countAllByEstadoTrueAndParams(categoriaId, marcaIds, minPrecio, maxPrecio, enOferta, palabraClave);
    }

    @Override
    public Page<ProductoEntity> PaginarProductosPorFiltrosDeBusqueda(
            Long categoriaId,
            List<Long> marcaIds,
            Double minPrecio,
            Double maxPrecio,
            Boolean enOferta,
            String palabraClave,
            Pageable pageable
    ) {
        return productoRepository.pageAllByParams(categoriaId, marcaIds, minPrecio, maxPrecio, enOferta, palabraClave, pageable);
    }

    @Override
    public Long ContarProductosPorFiltrosDeBusqueda(
            Long categoriaId,
            List<Long> marcaIds,
            Double minPrecio,
            Double maxPrecio,
            Boolean enOferta,
            String palabraClave) {
        return productoRepository.countAllByParams(categoriaId, marcaIds, minPrecio, maxPrecio, enOferta, palabraClave);
    }

    @Override
    public ProductoEntity ObtenerProductoPorId(Long id_producto) {
        return productoRepository.findById(id_producto).get();
    }
/*
    @Override
    public ProductoEntity GuardarProducto(ProductoEntity productoEntity){
        try {
            // Guardar la imagen asociada al producto
            ImagenProductoEntity imagenProducto = productoEntity.getImagenProductoEntity();
            imagenProductoRepository.save(imagenProducto);

            // Almacenar la imagen físicamente y actualizar la ruta en la entidad
            String rutaImagen = imagenProductoServiceImpl.almacenarUnaImagen(imagenProducto.getImagen());
            imagenProducto.setRutaimagen(rutaImagen);

            // Configurar la entidad del producto
            productoEntity.setEstado(true);

            // Guardar el producto con la imagen actualizada
            return productoRepository.save(productoEntity);
        } catch (Exception e) {
            // Manejar excepciones específicas o lanzar una excepción de la aplicación
            throw null;
        }
    }
    
    @Override
    public ProductoEntity GuardarProducto(ProductoEntity productoEntity){
        // Guardar la imagen asociada al producto
        ImagenProductoEntity imagenProducto = productoEntity.getImagenProductoEntity();
        imagenProductoRepository.save(imagenProducto);

        // Almacenar la imagen físicamente y actualizar la ruta en la entidad
        String rutaImagen = imagenProductoServiceImpl.almacenarUnaImagen(imagenProducto.getImagen());
        imagenProducto.setRutaimagen(rutaImagen);

        // Configurar la entidad del producto
        productoEntity.setEstado(true);

        // Guardar el producto con la imagen actualizada
        return productoRepository.save(productoEntity);
    }

    
    */
    @Override
    public ProductoEntity GuardarProducto(ProductoEntity productoEntity) {
        
        /*
        DetallesProductoEntity detallesProducto = new DetallesProductoEntity();
        // Configurar detallesProducto con la información necesaria
        detallesProductoRepository.save(detallesProducto);
*/
        LocalDateTime fechaActual = LocalDateTime.now();
        detallesProductoRepository.save(productoEntity.getDetallesproductoEntity());
        ImagenProductoEntity imagenProducto = imagenProductoRepository.save(productoEntity.getImagenProductoEntity());
        String rutaimagen = imagenProductoServiceImpl.almacenarUnaImagen(productoEntity.getImagenProductoEntity().getImagen());
        imagenProducto.setRutaimagen(rutaimagen);
        productoEntity.setFechaeditado(fechaActual);
        productoEntity.setEstado(Boolean.TRUE);
        productoEntity.setImagenProductoEntity(imagenProducto);
        return productoRepository.save(productoEntity);
    }

    @Override
    public ProductoEntity ActualizarProducto(ProductoEntity productoEntity) {
        // 1. Recuperar el producto existente de la base de datos
        ProductoEntity productoExistente = productoRepository.findById(productoEntity.getId_producto()).orElse(null);

        if (productoExistente == null) {
            return null;
        }

        // 2. Actualizar los campos del producto existente con los nuevos valores
        LocalDateTime fechaActual = LocalDateTime.now();
        detallesProductoRepository.save(productoEntity.getDetallesproductoEntity());

        productoExistente.setNombre(productoEntity.getNombre());
        productoExistente.setCodigo(productoEntity.getCodigo());
        productoExistente.setOferta(productoEntity.getOferta());
        productoExistente.setPrecionormal(productoEntity.getPrecionormal());
        productoExistente.setPreciooferta(productoEntity.getPreciooferta());
        // productoExistente.setEstado(Boolean.TRUE);
        productoExistente.setCategoriaEntity(productoEntity.getCategoriaEntity());
        productoExistente.setMarcaEntity(productoEntity.getMarcaEntity());
        productoExistente.setDetallesproductoEntity(productoEntity.getDetallesproductoEntity());
        productoExistente.setFechaeditado(fechaActual);
        // productoExistente.getDetallesproductoEntity().getCaracteristicas();

        // 3. Manejar la imagen si se proporciona una nueva imagen en la solicitud
        MultipartFile nuevaImagen = productoEntity.getImagenProductoEntity().getImagen();

        if (nuevaImagen != null && !nuevaImagen.isEmpty()) {

            String rutaImagen = imagenProductoServiceImpl.almacenarUnaImagen(nuevaImagen);
            imagenProductoServiceImpl.eliminarImagen(productoExistente.getImagenProductoEntity().getRutaimagen());
            productoExistente.getImagenProductoEntity().setRutaimagen(rutaImagen);
            // 4. Eliminar la imagen existente si es necesario
            

        } else {
            productoEntity.getImagenProductoEntity().setRutaimagen(productoExistente.getImagenProductoEntity().getRutaimagen());
        }

        return productoRepository.save(productoExistente);
    }

    @Override
    public void CambiarEstadoProductoAFalse(Long id_producto) {
        ProductoEntity productoEntity = productoRepository.findById(id_producto).orElse(null);
        if (productoEntity != null) {
            productoEntity.setEstado(Boolean.FALSE);
            productoRepository.save(productoEntity);
        }
    }

    @Override
    public void CambiarEstadoProductoATrue(Long id_producto) {
        ProductoEntity productoEntity = productoRepository.findById(id_producto).orElse(null);
        if (productoEntity != null) {
            productoEntity.setEstado(Boolean.TRUE);
            productoRepository.save(productoEntity);
        }
    }

    @Override
    public void EliminarDefinitivamente(Long id_producto) {
        ProductoEntity productoEntity = productoRepository.findById(id_producto).orElse(null);
        if (productoEntity != null) {
            // Eliminar el registro de la base de datos
            productoRepository.delete(productoEntity);
            // Obtener la ruta de la imagen asociada
            String rutaImagen = productoEntity.getImagenProductoEntity().getRutaimagen();
            // Eliminar físicamente el archivo de la imagen
            if (rutaImagen != null && !rutaImagen.isEmpty()) {
                File archivoImagen = new File(rutaImagen);
                if (archivoImagen.exists()) {
                    archivoImagen.delete();
                }
            }
        }

    }

}

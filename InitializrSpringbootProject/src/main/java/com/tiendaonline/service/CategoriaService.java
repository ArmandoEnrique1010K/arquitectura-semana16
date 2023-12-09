package com.tiendaonline.service;

import com.tiendaonline.entity.CategoriaEntity;
import java.util.List;

public interface CategoriaService {
    public List<CategoriaEntity> ListarCategorias();
    public List<CategoriaEntity> ListarCategoriasByEstadoTrue();
    public CategoriaEntity obtenerCategoriaPorId(Long id_categoria);
    public CategoriaEntity ObtenerCategoriaPorNombre(String nombreCategoria);
    public CategoriaEntity GuardarCategoria(CategoriaEntity categoriaentity);
    public CategoriaEntity CambiarNombreCategoria(Long id_categoria, CategoriaEntity categoriaentity);
    public void EliminarCategoria(Long id_categoria);
    public void RestaurarCategoria(Long id_categoria);
}

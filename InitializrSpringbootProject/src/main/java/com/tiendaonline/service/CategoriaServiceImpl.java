package com.tiendaonline.service;

import com.tiendaonline.entity.CategoriaEntity;
import com.tiendaonline.repository.CategoriaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<CategoriaEntity> ListarCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public List<CategoriaEntity> ListarCategoriasByEstadoTrue() {
        return categoriaRepository.findAllByEstadoTrue();
    }

    @Override
    public CategoriaEntity obtenerCategoriaPorId(Long id_categoria) {
        Optional<CategoriaEntity> categoriaOptional = categoriaRepository.findById(id_categoria);
        return categoriaOptional.orElse(null);
        
    }

    @Override
    public CategoriaEntity ObtenerCategoriaPorNombre(String nombreCategoria) {
        return categoriaRepository.findByNombreParam(nombreCategoria);
    }

    @Override
    public CategoriaEntity GuardarCategoria(CategoriaEntity categoriaEntity) {
        categoriaEntity.setEstado(true);
        return categoriaRepository.save(categoriaEntity);
    }
    

    @Override
    public CategoriaEntity CambiarNombreCategoria(Long id_categoria, CategoriaEntity categoriaEntity) {
        Optional<CategoriaEntity> categoriaOptional = categoriaRepository.findById(id_categoria);
        if (categoriaOptional.isPresent()) {
            CategoriaEntity categoriaExistente = categoriaOptional.get();
            categoriaExistente.setNombre(categoriaEntity.getNombre()); // Asignar el nuevo nombre
            // Puedes actualizar más atributos según tus necesidades.
            return categoriaRepository.save(categoriaExistente);
        }
        return null; // O manejar de otra forma si no se encuentra la categoría.
    }

    @Override
    public void EliminarCategoria(Long id_categoria) {
        Optional<CategoriaEntity> categoriaOptional = categoriaRepository.findById(id_categoria);
        categoriaOptional.ifPresent(categoria -> {
            // Puedes cambiar el estado o realizar la eliminación según tus necesidades.
            categoria.setEstado(false);
            categoriaRepository.save(categoria);
        });
    }

    @Override
    public void RestaurarCategoria(Long id_categoria) {
        Optional<CategoriaEntity> categoriaOptional = categoriaRepository.findById(id_categoria);
        categoriaOptional.ifPresent(categoria -> {
            categoria.setEstado(true);
            categoriaRepository.save(categoria);
        });
    }    
    
}

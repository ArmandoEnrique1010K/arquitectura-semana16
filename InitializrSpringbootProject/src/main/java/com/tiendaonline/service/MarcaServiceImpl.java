package com.tiendaonline.service;

import com.tiendaonline.entity.MarcaEntity;
import com.tiendaonline.repository.MarcaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarcaServiceImpl implements MarcaService {

    @Autowired
    private MarcaRepository marcaRepository;

    @Override
    public List<MarcaEntity> ListarMarcas() {
        return marcaRepository.findAll();
    }

    @Override
    public List<MarcaEntity> ListarMarcasByEstadoTrue() {
        return marcaRepository.findAllByEstadoTrue();
    }

    @Override
    public List<MarcaEntity> ListarDistintasMarcasByGrupoCategoriaAndEstadoTrue(String nombreCategoria) {
        return marcaRepository.findAllDistinctByCategoriaGroupAndEstadoTrue(nombreCategoria);
    }

    @Override
    public List<MarcaEntity> ListarDistintasMarcasByGrupoCategoria(String nombreCategoria) {
        return marcaRepository.findAllDistinctByCategoriaGroup(nombreCategoria);
    }

    @Override
    public MarcaEntity ObtenerMarcaPorId(Long id_marca) {
        Optional<MarcaEntity> marcaOptional = marcaRepository.findById(id_marca);
        return marcaOptional.orElse(null);
    }

    @Override
    public MarcaEntity GuardarMarca(MarcaEntity marcaEntity) {
        marcaEntity.setEstado(true);
        return marcaRepository.save(marcaEntity);
    }

    @Override
    public MarcaEntity CambiarNombreMarca(Long id_marca, MarcaEntity marcaEntity) {
        
        Optional<MarcaEntity> marcaOptional = marcaRepository.findById(id_marca);
        if (marcaOptional.isPresent()) {
            MarcaEntity marcaExistente = marcaOptional.get();
            marcaExistente.setNombre(marcaEntity.getNombre());
            return marcaRepository.save(marcaExistente);
        }
        return null;

        
        
    }

    /*
    public MarcaEntity CambiarNombreMarca(Long id_marca, MarcaEntity marcaEntity) {
        Optional<MarcaEntity> marcaOptional = marcaRepository.findById(id_marca);
        if (marcaOptional.isPresent()) {
            MarcaEntity marcaExistente = marcaOptional.get();
            if (!marcaExistente.getNombre().equals(marcaEntity.getNombre())) {
                marcaExistente.setNombre(marcaEntity.getNombre());
                // Puedes actualizar más atributos según tus necesidades.
                return marcaRepository.save(marcaExistente);
            }
        }
        return null;
    }
     */

    @Override
    public void EliminarMarca(Long id_marca) {
        Optional<MarcaEntity> marcaOptional = marcaRepository.findById(id_marca);
        marcaOptional.ifPresent(marca -> {
            marca.setEstado(false);
            marcaRepository.save(marca);
        });
    }

    @Override
    public void RestaurarMarca(Long id_marca) {
        Optional<MarcaEntity> marcaOptional = marcaRepository.findById(id_marca);
        marcaOptional.ifPresent(marca -> {
            marca.setEstado(true);
            marcaRepository.save(marca);
        });
    }

}

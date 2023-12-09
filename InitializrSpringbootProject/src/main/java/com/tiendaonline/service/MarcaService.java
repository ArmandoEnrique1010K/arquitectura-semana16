package com.tiendaonline.service;

import com.tiendaonline.entity.MarcaEntity;
import java.util.List;


public interface MarcaService {
    
    public List<MarcaEntity> ListarMarcas();
    public List<MarcaEntity> ListarMarcasByEstadoTrue();
    public List<MarcaEntity> ListarDistintasMarcasByGrupoCategoria(String nombreMarca);
    public List<MarcaEntity> ListarDistintasMarcasByGrupoCategoriaAndEstadoTrue(String nombreMarca);
    public MarcaEntity ObtenerMarcaPorId(Long id_marca); 
    public MarcaEntity GuardarMarca(MarcaEntity marcaEntity);
    public MarcaEntity CambiarNombreMarca(Long id_marca, MarcaEntity marcaEntity);
    public void EliminarMarca(Long id_marca);
    public void RestaurarMarca(Long id_marca);
    
}

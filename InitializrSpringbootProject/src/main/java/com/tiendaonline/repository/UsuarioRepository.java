package com.tiendaonline.repository;

import com.tiendaonline.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long>{
    
    // CONSULTA PARA BUSCAR UN USUARIO POR SU EMAIL
    @Query("SELECT u FROM UsuarioEntity u WHERE u.email = :email")
    public UsuarioEntity findUserByEmail(@Param("email") String email);
    
}

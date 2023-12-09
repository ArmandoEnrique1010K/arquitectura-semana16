package com.tiendaonline.service;

import com.tiendaonline.dto.UsuarioDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsuarioService extends UserDetailsService{
    
    public UsuarioDto GuardarUsuario(UsuarioDto usuarioDto);
    public UsuarioDto obtenerUsuarioPorEmail(String email);
    public UsuarioDto obtenerUsuarioPorId(Long id_usuario);
    public boolean ExisteUsuarioPorEmail(String email);

}

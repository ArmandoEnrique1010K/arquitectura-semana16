package com.tiendaonline.service;

import com.tiendaonline.dto.UsuarioDto;
import com.tiendaonline.entity.RolEntity;
import com.tiendaonline.entity.UsuarioEntity;
import com.tiendaonline.repository.RolRepository;
import com.tiendaonline.repository.UsuarioRepository;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    HttpSession session;

    
    @Override
    public UsuarioDto GuardarUsuario(UsuarioDto usuarioDto) {

        // Crear una entidad de rol "USER" para el nuevo usuario
        RolEntity rolEntity = RolEntity.builder()
                .nombre("USER")
                .build();
        
        // Almacenar la entidad del rol en la base de datos
        rolEntity = rolRepository.save(rolEntity);

        // Construir una entidad de usuario con los datos proporcionados
        UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .nombre(usuarioDto.getNombre())
                .apellido(usuarioDto.getApellido())
                .edad(usuarioDto.getEdad())
                .dni(usuarioDto.getDni())
                .sexo(usuarioDto.getSexo())
                .email(usuarioDto.getEmail())
                // Encriptar la contraseña para almacenarla de manera segura
                .password(passwordEncoder.encode(usuarioDto.getPassword()))
                // Asignar el rol "USER" al nuevo usuario
                .roles(Arrays.asList(rolEntity))
                .build();

        // Guardar la entidad del usuario en la base de datos
        usuarioEntity = usuarioRepository.save(usuarioEntity);
        // Actualizar el objeto datosUsuarioDto con el ID del usuario creado
        usuarioDto.setId_usuario(usuarioEntity.getId_usuario());
        // Devolver el objeto datosUsuarioDto actualizado
        return usuarioDto;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioEntity usuarioEntity = usuarioRepository.findUserByEmail(username);
        if (usuarioEntity == null) {
            throw new UsernameNotFoundException("Usuario o contraseña invalido(a)");
        }

        /* Convertir la lista de roles a una lista de cadenas (nombres de roles) */
        List<String> roles = usuarioEntity.getRoles().stream()
                .map(role -> role.getNombre())
                .collect(Collectors.toList());

        // Almacenar el ID del usuario en la sesión
        session.setAttribute("identificadordelusuario", usuarioEntity.getId_usuario().toString());

        // Configurar los roles correctamente
        return User.withUsername(usuarioEntity.getEmail())
                .password(usuarioEntity.getPassword())
                .roles(roles.toArray(String[]::new))
                .build();

    }
        

    @Override
    public UsuarioDto obtenerUsuarioPorEmail(String email) {
        // Buscar un usuario por su dirección de correo electrónico en el repositorio
        UsuarioEntity usuarioEntity = usuarioRepository.findUserByEmail(email);

        // Verificar si el usuario no fue encontrado en el repositorio
        if (usuarioEntity == null) {
            // Lanzar una excepción en caso de que el usuario no exista
            throw null;
        }

        // Construir un objeto DatosUsuarioDto utilizando los datos del usuario encontrado
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .id_usuario(usuarioEntity.getId_usuario())
                .nombre(usuarioEntity.getNombre())
                .apellido(usuarioEntity.getApellido())
                .email(usuarioEntity.getEmail())
                .edad(usuarioEntity.getEdad())
                .dni(usuarioEntity.getDni())
                .sexo(usuarioEntity.getSexo())
                .build();
        // Devolver el objeto DatosUsuarioDto con la información del usuario encontrado
        return usuarioDto;
    }

    @Override
    public UsuarioDto obtenerUsuarioPorId(Long id_usuario) {
        // Buscar un usuario por su ID en el repositorio
        UsuarioEntity usuarioEntity = usuarioRepository.findById(id_usuario).orElse(null);
        
        // Verificar si el usuario no fue encontrado en el repositorio
        if (usuarioEntity == null) {
            // Lanzar una excepción en caso de que el usuario no exista
            throw null;
        }
        
        // Construir un objeto DatosUsuarioDto utilizando los datos del usuario encontrado
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .id_usuario(usuarioEntity.getId_usuario())
                .nombre(usuarioEntity.getNombre())
                .apellido(usuarioEntity.getApellido())
                .email(usuarioEntity.getEmail())
                .edad(usuarioEntity.getEdad())
                .dni(usuarioEntity.getDni())
                .sexo(usuarioEntity.getSexo())
                .build();
        
        // Devolver el objeto DatosUsuarioDto con la información del usuario encontrado
        return usuarioDto;
    }
    
    @Override
    public boolean ExisteUsuarioPorEmail(String email) {
        
        // Utiliza el repositorio para buscar un usuario por su correo electrónico
        UsuarioEntity usuario = usuarioRepository.findUserByEmail(email);
        
        // Si se encuentra un usuario con el mismo correo, entonces ya existe
        return usuario != null;
    
    }

    
    
}



        /* VERDADERO CODIGO :O */
        /*
        String[] roles = usuarioEntity.getRoles().stream()
                .map(role -> role.getNombre())
                .toArray(String[]::new);
        return User.withUsername(usuarioEntity.getEmail())
                .password(usuarioEntity.getPassword())
                .roles(roles)
                
                .build();
        */
        /* VERDADERO CODIGO :O */











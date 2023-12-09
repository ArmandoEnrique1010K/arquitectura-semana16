package com.tiendaonline.service;

import java.nio.file.Path;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImagenProductoService {
    // public void iniciarContenedorImagenes();
    public String almacenarUnaImagen(MultipartFile imagen);
    public Path cargarImagen(String nombreImagen);
    public Resource cargarComoRecurso(String nombreImagen);
    public void eliminarImagen(String nombreImagen);
}

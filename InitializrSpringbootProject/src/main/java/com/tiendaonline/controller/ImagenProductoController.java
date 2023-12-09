package com.tiendaonline.controller;

import com.tiendaonline.service.ImagenProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/assets/productos")
public class ImagenProductoController {

    private final Logger logger = LoggerFactory.getLogger(ImagenProductoController.class);

    @Autowired
    private ImagenProductoService imagenProductoService;

    // RUTA A LA PAGINA PARA VISUALIZAR UNA IMAGEN: http://localhost:8080/images/productos/...
    @GetMapping("/{nombreImagen:.+}")
    public ResponseEntity<Resource> ObtenerRecurso(@PathVariable("nombreImagen") String nombreImagen) {
        
        // LLAMAR AL METODO PARA CARGAR UNA IMAGEN COMO RECURSO
        Resource recurso = imagenProductoService.cargarComoRecurso(nombreImagen);
        logger.info("MOSTRANDO LA IMAGEN: " + nombreImagen);
        
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .contentType(MediaType.IMAGE_PNG)
                .contentType(MediaType.IMAGE_GIF)
                .body(recurso);
        
    }

}

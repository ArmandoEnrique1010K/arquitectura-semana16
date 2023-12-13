package com.tiendaonline.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

@Service
public class ImagenProductoServiceImpl implements ImagenProductoService{
    
    // Función para generar un codigo aleatorio
    private String generarUUIDComoNombre(String extension) {
        UUID uuid = UUID.randomUUID();
        return uuid.toString() + extension;
    }

    // Función para obtener la extensión del nombre original de la imagen
    private String obtenerExtension(String nombreOriginal) {
        int extensionIndex = nombreOriginal.lastIndexOf('.');
        return (extensionIndex != -1) ? nombreOriginal.substring(extensionIndex) : "";
    }

    // Función para obtener el nombre original de la imagen (sin extensión)
    private String obtenerNombreOriginal(String nombreOriginal) {
        int extensionIndex = nombreOriginal.lastIndexOf('.');
        return (extensionIndex != -1) ? nombreOriginal.substring(0, extensionIndex) : nombreOriginal;
    }

    @Autowired
    private AmazonS3 amazonS3;
    
    @Value("${aws.s3.bucket}")
    private String storageLocationProductos;

    
    /*
    @PostConstruct
    @Override
    public void iniciarContenedorImagenes() {
        try {
            Files.createDirectories(Paths.get(storageLocationProductos));
        } catch (IOException exception) {
            throw null;
        }
    }
*/
    
    @Override
    public String almacenarUnaImagen(MultipartFile imagen) {
        if (imagen.isEmpty()) {
            throw new IllegalArgumentException("La imagen está vacía");
        }

        // Verificar si el tipo de archivo es válido (solo admitir JPG, PNG y GIF)
        String contentType = imagen.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new UnsupportedMediaTypeStatusException("Tipo de archivo no admitido");
        }

        try {
            // Obtener la extensión del archivo original
            String extension = StringUtils.getFilenameExtension(imagen.getOriginalFilename());

            // Generar un código aleatorio usando UUID
            String codigoAleatorio = generarUUIDComoNombre(extension);

            // Generar el nuevo nombre usando el código aleatorio y la extensión del archivo original
            String nuevoNombre = codigoAleatorio + "." + extension;

            // Configurar los metadatos del objeto
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(imagen.getSize());

            // Crear la solicitud de carga
            PutObjectRequest putObjectRequest = new PutObjectRequest(storageLocationProductos, nuevoNombre, imagen.getInputStream(), metadata);

            // Subir la imagen a S3
            amazonS3.putObject(putObjectRequest);

            // Retornar el nombre de la imagen almacenada
            return nuevoNombre;

        } catch (IOException exception) {
            throw new RuntimeException("Error al subir la imagen a S3", exception);
        }
    }
    
    @Override
    public Path cargarImagen(String nombreImagen) {
        Path imagenPath = Paths.get(storageLocationProductos).resolve(nombreImagen);
        if (!Files.exists(imagenPath)) {
            throw null;
        }
        return imagenPath;
    }

    @Override
    public Resource cargarComoRecurso(String nombreImagen) {
        try {
            // Descargar la imagen desde S3
            S3Object s3Object = amazonS3.getObject(storageLocationProductos, nombreImagen);
            InputStream inputStream = s3Object.getObjectContent();

            // Crear un recurso de InputStream para la imagen
            return new InputStreamResource(inputStream);

        } catch (Exception exception) {
            throw new RuntimeException("Error al cargar la imagen desde S3", exception);
        }
    }

    @Override
    public void eliminarImagen(String nombreImagen) {
        try {
            // Eliminar la imagen desde S3
            amazonS3.deleteObject(storageLocationProductos, nombreImagen);
        } catch (Exception exception) {
            throw new RuntimeException("Error al eliminar la imagen desde S3", exception);
        }
    }
    
}

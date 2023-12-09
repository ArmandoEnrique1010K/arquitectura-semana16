package com.tiendaonline.controller;

import com.tiendaonline.dto.UsuarioDto;
import com.tiendaonline.entity.ProductoEntity;
import com.tiendaonline.service.CategoriaService;
import com.tiendaonline.service.MarcaService;
import com.tiendaonline.service.ProductoService;
import com.tiendaonline.service.UsuarioService;
import com.tiendaonline.util.PageRender;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    private final Logger logger = LoggerFactory.getLogger(WebController.class);

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private MarcaService marcaService;
    
    /*
    @Autowired
    private CarritoService carritoService;
*/
    
    // RUTA A LA PAGINA DE INICIO: http://localhost:8080
    @GetMapping("/")
    public String inicio(Model model, Authentication authentication) {
        model.addAttribute("categorias", categoriaService.ListarCategoriasByEstadoTrue());
        model.addAttribute("marcas", marcaService.ListarMarcasByEstadoTrue());
        
        // 6 PRIMEROS PRODUCTOS EN OFERTA
        Pageable pageRequest = PageRequest.of(0, 6);
        // Llama al servicio para paginar y filtrar los productos
        Page<ProductoEntity> variosProductoDto = productoService.PaginarProductosHabilitadosPorFiltrosDeBusqueda(null, null, null, null, Boolean.TRUE, null, pageRequest);
                model.addAttribute("productos", variosProductoDto);
        logger.info("BIENVENIDO...");
        return "index";
    }

        /*
        if (authentication != null && authentication.isAuthenticated()) {
            // Cuando un usuario inicia sesión, obtén su ID de usuario
            String username = authentication.getName();
            // SI ESTA AGARRANDO EL CORREO DEL USUARIO
            logger.info("CORREO DEL USUARIO :" + username);
            UsuarioDto usuarioDto = usuarioService.obtenerUsuarioPorEmail(username);

            // Verifica si el usuario ya tiene un carrito asociado
            // CarritoDto carritoDto = carritoService.obtenerCarritoPorUsuario(datosUsuarioDto);
            // SI ESTA AGARRANDO EL ID DEL USUARIO
            logger.info("ID DEL USUARIO : " + usuarioDto.getId_usuario());

            /////
            if (carritoDto == null) {
                // No se encontró un carrito existente, crea uno nuevo
                carritoDto = new CarritoDto();
                carritoDto.setFecha_creacion(new Date());
                carritoDto.setId_usuario(datosUsuarioDto.getId_usuario());
                logger.info("ID DEL NUEVO USUARIO :" + datosUsuarioDto.getId_usuario());
                carritoDto.setEstado_carrito(true); // Asigna el estado a "true"

                // Guarda el carrito nuevo en la base de datos
                carritoService.guardarCarrito(carritoDto, datosUsuarioDto);
            }
            ////
        }
        */

        /**
         * *CARRITO***
         */
        // En el método de manejo de inicio de sesión
        /*
        DatosUsuarioDto datosUsuarioDto = usuarioService.ObtenerUsuarioPorEmail(email);
        CarritoDto carritoDto = carritoService.obtenerCarritoPorUsuario(datosUsuarioDto);
        if (carritoDto == null) {
            carritoDto = new CarritoDto();
            carritoDto.setFecha_creacion(new Date());
            carritoDto.setDatosUsuarioDto(datosUsuarioDto);
            carritoDto = carritoService.guardarCarrito(carritoDto, datosUsuarioDto.getId_usuario());
        }
         */
        // ATRIBUTO PARA LA LISTA DE CATEGORIAS HABILITADAS, EL CUAL SE VA A MOSTRAR EN LA BARRA DE MENÚ

    // RUTA AL LOGIN: http://localhost:8080/login
    @GetMapping("/login")
    public String formularioDeLogin(Model model) {
        model.addAttribute("categorias", categoriaService.ListarCategoriasByEstadoTrue());
        return "form_login";
    }

    // RUTA PARA CERRAR SESION http://localhost:8080/logout
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, Model modelo
    ) {
        // Obtener la autenticación actual del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verificar si existe una autenticación
        if (authentication != null) {
            // Invalidar la sesión actual si la autenticación existe
            request.getSession().invalidate();
        }

        modelo.addAttribute("categorias", categoriaService.ListarCategoriasByEstadoTrue());
        return "redirect:/login?logout"; // Redirigir a la página de inicio de sesión con un mensaje de cierre de sesión
    }

    // RUTA PARA REGISTRAR NUEVO USUARIO http://localhost:8080/registro
    @GetMapping("/registro")
    public String mostrarFormularioDeRegistro(Model modelo) {
        modelo.addAttribute("categorias", categoriaService.ListarCategoriasByEstadoTrue());
        return "form_registro_usuario";
    }

    // Crear y retornar un nuevo objeto DatosUsuarioDto como modelo
    @ModelAttribute("usuarioDto")
    public UsuarioDto retornarNuevoUsuario() {
        return new UsuarioDto();
    }

    @PostMapping("/registro")
    public String registrarCuentaDeUsuario(
            @RequestParam("email") String email,
            @Validated
            @ModelAttribute("datosUsuarioDto") UsuarioDto usuarioDto,
            BindingResult result,
            Model model
    ) {

        model.addAttribute("categorias", categoriaService.ListarCategoriasByEstadoTrue());

        // Crear y retornar un nuevo objeto DatosUsuarioDto como modelo
        if (result.hasErrors()) {
            logger.info("COMPLETE LOS CAMPOS FALTANTES");
            // En caso de errores de validación, se redirige de nuevo al formulario de registro.
            return "form_registro_usuario";
        }

        // Verificar si el correo electrónico ya está en uso
        if (usuarioService.ExisteUsuarioPorEmail(email)) {
            logger.info("El correo electrónico ya está en uso.");
            model.addAttribute("errorEmailRepetido", "Este correo electrónico ya está en uso.");
            // En caso de correo electrónico duplicado, se muestra un mensaje de error y se redirige al formulario de registro.
            return "form_registro_usuario";
        }

        // Guardar los datos del usuario si no hay errores
        usuarioService.GuardarUsuario(usuarioDto);

        // Redirigir a la página de registro con un mensaje de éxito
        return "redirect:/registro?exito";

    }

}


/*
    @PostMapping("/loginProcessingUrl")
    public String procesarInicioDeSesion(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            Model model) {

        model.addAttribute("categorias", categoriaService.ListarCategoriasByEstadoTrue());
            logger.info("PROCESANDO");

        // Ejemplo de validación simple
        if (usuarioService.ValidarCredenciales(username, password)) {
            // Credenciales válidas, redirige al usuario a la página de inicio
            return "redirect:/inicio";
        } else {
            // Credenciales no válidas, muestra un mensaje de error y devuelve el formulario de inicio de sesión
            model.addAttribute("error", "Credenciales no válidas. Inténtalo de nuevo.");
            model.addAttribute("error2", "Complete el campo");

            logger.info("INTENTE OTRA VEZ");
            return "form_login";
        }
    }
 */
 /*
        if (username.isEmpty()) {
            model.addAttribute("error2", "Complete el campo de Email");
            return "form_login";
        }

        if (password.isEmpty()) {
            model.addAttribute("error2", "Complete el campo de Contraseña");
            return "form_login";
        }
 */

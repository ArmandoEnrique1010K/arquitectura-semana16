package com.tiendaonline.controller;

import com.tiendaonline.entity.CategoriaEntity;
import com.tiendaonline.entity.MarcaEntity;
import com.tiendaonline.entity.ProductoEntity;
import com.tiendaonline.service.CategoriaService;
import com.tiendaonline.service.ImagenProductoService;
import com.tiendaonline.service.MarcaService;
import com.tiendaonline.service.ProductoService;
import com.tiendaonline.util.PageRender;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;


@Controller
@RequestMapping("/admin")

public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private MarcaService marcaService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ImagenProductoService imagenProductoService;
    
    // RUTA A LA PAGINA DE INICIO DE ADMINISTRADORES: http://localhost:8080/admin
    @GetMapping
    public String InicioAdministradores(Model model) {
        model.addAttribute("categorias", categoriaService.ListarCategoriasByEstadoTrue());
                model.addAttribute("marcas", marcaService.ListarMarcasByEstadoTrue());

        // 6 PRIMEROS PRODUCTOS EN OFERTA
        Pageable pageRequest = PageRequest.of(0, 6);
        // Llama al servicio para paginar y filtrar los productos
        Page<ProductoEntity> variosProductoDto = productoService.PaginarProductosHabilitadosPorFiltrosDeBusqueda(null, null, null, null, Boolean.TRUE, null, pageRequest);
                model.addAttribute("productos", variosProductoDto);

        logger.info("MOSTRANDO LA PAGINA DE INICIO DE ADMINISTRADORES");
        return "admin_index";
    }

    
    
    // --------------------- RUTAS PARA CATEGORIAS ---------------------

    // RUTA A LA PAGINA DE LISTA DE CATEGORIAS: http://localhost:8080/admin/categorias
    @GetMapping("categorias")
    public String ListarCategorias(Model model) {
        // ATRIBUTO PARA LA LISTA DE TODAS LAS CATEGORIAS
        model.addAttribute("categorias", categoriaService.ListarCategorias());
        logger.info("LISTANDO TODAS LAS CATEGORIAS");
        return "admin_categorias";
    }

    // RUTA A LA PAGINA DE NUEVA CATEGORIA: http://localhost:8080/admin/categorias/nuevo
    @GetMapping("categorias/nuevo")
    public String MostrarFormularioDeAgregarCategoria(Model model) {
        // ATRIBUTO PARA CREAR LOS PARAMETROS QUE SE VAN UTILIZAR
        CategoriaEntity categoriaEntity = new CategoriaEntity();
        model.addAttribute("categoriaEntity", categoriaEntity);
        logger.info("MOSTRANDO EL FORMULARIO PARA AGREGAR UNA NUEVA CATEGORIA");
        return "admin_form_nueva_categoria";
    }

    // AL HACER CLIC EN EL BOTON PARA GUARDAR UNA NUEVA CATEGORIA, VA IR A LA RUTA: http://localhost:8080/admin/categorias
    @PostMapping("/categorias/nuevo")
    public String GuardarNuevaCategoria(
            @Validated @ModelAttribute("categoriaEntity") CategoriaEntity categoriaEntity,
            BindingResult result) {

        // SI EL ADMINISTRADOR NO COMPLETA LOS CAMPOS QUE NO DEBEN ESTAR EN BLANCO
        if (result.hasErrors()) {
            logger.info("COMPLETE LOS CAMPOS FALTANTES");
            return "admin_form_nueva_categoria";
        }

        // LLAMA AL SERVICIO PARA GUARDAR UNA CATEGORIA
        categoriaService.GuardarCategoria(categoriaEntity);
        logger.info("SE HA SUBIDO LA CATEGORIA " + categoriaEntity);
        return "redirect:/admin/categorias";
    }

    // RUTA A LA PAGINA DE EDITAR UNA CATEGORIA QUE EXISTE: http://localhost:8080/admin/categorias/.../editar
    @GetMapping("/categorias/{id_categoria}/editar")
    public String MostrarFormularioDeEditarCategoria(@PathVariable Long id_categoria, Model model) {
        // ATRIBUTO PARA RECOGER LOS DATOS DE LA CATEGORIA SEGÚN SU ID
        CategoriaEntity categoriaEntity = categoriaService.obtenerCategoriaPorId(id_categoria);
        model.addAttribute("categoriaEntity", categoriaEntity);
        logger.info("MOSTRANDO EL FORMULARIO PARA EDITAR LA CATEGORIA CON EL ID " + id_categoria);
        return "admin_form_editar_categoria";
    }

    // AL HACER CLIC EN EL BOTON PARA GUARDAR LOS CAMBIOS REALIZADOS A LA CATEGORIA, VA IR A LA RUTA: http://localhost:8080/admin/categorias
    @PostMapping("/categorias/{id_categoria}/editar")
    public String GuardarCategoriaEditada(
            @PathVariable Long id_categoria,
            @Validated @ModelAttribute("categoriaEntity") CategoriaEntity categoriaEntity,
            BindingResult result
    ) {
        // SI EL ADMINISTRADOR NO COMPLETA LOS CAMPOS QUE NO DEBEN ESTAR EN BLANCO
        if (result.hasErrors()) {
            logger.info("COMPLETE LOS CAMPOS FALTANTES");
            return "admin_form_editar_categoria";
        }
        // LLAMA AL SERVICIO PARA EDITAR UNA CATEGORIA
        categoriaService.CambiarNombreCategoria(id_categoria, categoriaEntity);
        logger.info("LA CATEGORIA CON EL ID " + id_categoria + " HA SIDO MODIFICADO Y SE HA GUARDADO");
        return "redirect:/admin/categorias";
    }

    // AL HACER CLIC EN EL BOTON PARA INHABILITAR UNA CATEGORIA, VA IR A LA RUTA: http://localhost:8080/admin/categorias
    @GetMapping("/categorias/{id_categoria}/eliminar")
    public String EliminarCategoria(@PathVariable Long id_categoria) {
        categoriaService.EliminarCategoria(id_categoria);
        logger.info("EL ESTADO DE LA CATEGORIA CON EL ID " + id_categoria + " HA CAMBIADO A FALSE (HA SIDO ELIMINADO)");
        return "redirect:/admin/categorias";
    }

    // AL HACER CLIC EN EL BOTON PARA HABILITAR UNA CATEGORIA, VA IR A LA RUTA: http://localhost:8080/admin/categorias
    @GetMapping("/categorias/{id_categoria}/restaurar")
    public String RestaurarCategoria(@PathVariable Long id_categoria) {
        categoriaService.RestaurarCategoria(id_categoria);
        logger.info("EL ESTADO DE LA CATEGORIA CON EL ID " + id_categoria + " HA CAMBIADO A TRUE (HA SIDO RESTAURADO)");
        return "redirect:/admin/categorias";
    }

    
    // --------------------- RUTAS PARA MARCAS ---------------------

    // RUTA A LA PAGINA DE LISTA DE MARCAS: http://localhost:8080/admin/marcas
    @GetMapping("marcas")
    public String listarMarcas(Model model) {
        // ATRIBUTO PARA LA LISTA DE TODAS LAS MARCAS
        model.addAttribute("marcas", marcaService.ListarMarcas());
        logger.info("LISTANDO TODAS LAS MARCAS");
        return "admin_marcas";
    }

    // RUTA A LA PAGINA DE NUEVA MARCA: http://localhost:8080/admin/marcas/nuevo
    @GetMapping("marcas/nuevo")
    public String mostrarFormularioDeAgregarMarca(Model model) {
        // ATRIBUTO PARA CREAR LOS PARAMETROS QUE SE VAN UTILIZAR
        MarcaEntity marcaEntity = new MarcaEntity();
        model.addAttribute("marcaEntity", marcaEntity);
        logger.info("MOSTRANDO EL FORMULARIO PARA AGREGAR UNA NUEVA MARCA");
        return "admin_form_nueva_marca";
    }

    // AL HACER CLIC EN EL BOTON PARA GUARDAR UNA NUEVA MARCA, VA IR A LA RUTA: http://localhost:8080/admin/marcas
    @PostMapping("/marcas/nuevo")
    public String guardarNuevaMarca(
            @Validated @ModelAttribute("marcaEntity") MarcaEntity marcaEntity,
            BindingResult result) {

        // SI EL ADMINISTRADOR NO COMPLETA LOS CAMPOS QUE NO DEBEN ESTAR EN BLANCO
        if (result.hasErrors()) {
            logger.info("COMPLETE LOS CAMPOS FALTANTES");
            return "admin_form_nueva_marca";
        }

        // LLAMA AL SERVICIO PARA GUARDAR UNA MARCA
        marcaService.GuardarMarca(marcaEntity);
        logger.info("SE HA SUBIDO LA MARCA " + marcaEntity);
        return "redirect:/admin/marcas";
    }

    // RUTA A LA PAGINA DE EDITAR UNA MARCA QUE EXISTE: http://localhost:8080/admin/marcas/.../editar
    @GetMapping("/marcas/{id_marca}/editar")
    public String mostrarFormularioDeEditarMarca(@PathVariable Long id_marca, Model model) {
        // ATRIBUTO PARA RECOGER LOS DATOS DE LA MARCA SEGÚN SU ID
        model.addAttribute("marcaEntity", marcaService.ObtenerMarcaPorId(id_marca));
        logger.info("MOSTRANDO EL FORMULARIO PARA EDITAR LA MARCA CON EL ID: " + id_marca);
        return "admin_form_editar_marca";
    }

    // AL HACER CLIC EN EL BOTON PARA GUARDAR LOS CAMBIOS REALIZADOS A LA CATEGORIA, VA IR A LA RUTA: http://localhost:8080/admin/marcas
    @PostMapping("/marcas/{id_marca}/editar")
    public String guardarMarcaEditada(
            @PathVariable Long id_marca,
            @Validated @ModelAttribute("marcaEntity") MarcaEntity marcaEntity,
            BindingResult result
    ) {

        // SI EL ADMINISTRADOR NO COMPLETA LOS CAMPOS QUE NO DEBEN ESTAR EN BLANCO
        if (result.hasErrors()) {
            logger.info("COMPLETE LOS CAMPOS FALTANTES");
            return "admin_form_editar_marca";
        }

        // LLAMA AL SERVICIO PARA EDITAR UNA MARCA
        marcaService.CambiarNombreMarca(id_marca, marcaEntity);
        logger.info("LA MARCA CON EL ID " + id_marca + " HA SIDO MODIFICADO Y SE HA GUARDADO");
        return "redirect:/admin/marcas";
    }

    // AL HACER CLIC EN EL BOTON PARA INHABILITAR UNA MARCA, VA IR A LA RUTA: http://localhost:8080/admin/marcas
    @GetMapping("/marcas/{id_marca}/eliminar")
    public String eliminarMarca(@PathVariable Long id_marca) {
        marcaService.EliminarMarca(id_marca);
        logger.info("EL ESTADO DE LA MARCA CON EL ID " + id_marca + " HA CAMBIADO A FALSE (HA SIDO ELIMINADO)");
        return "redirect:/admin/marcas";
    }

    // AL HACER CLIC EN EL BOTON PARA HABILITAR UNA MARCA, VA IR A LA RUTA: http://localhost:8080/admin/marcas
    @GetMapping("/marcas/{id_marca}/restaurar")
    public String restaurarMarca(@PathVariable Long id_marca) {
        marcaService.RestaurarMarca(id_marca);
        logger.info("EL ESTADO DE LA MARCA CON EL ID " + id_marca + " HA CAMBIADO A TRUE (HA SIDO RESTAURADO)");
        return "redirect:/admin/marcas";
    }

    
    // --------------------- RUTAS PARA PRODUCTOS ---------------------
    // RUTA A LA PAGINA DE LISTA DE PRODUCTOS: http://localhost:8080/admin/productos
    /*
    @GetMapping("/productos")
    public String ListarTodosLosProductosDelSistemaPorFiltros(
            @RequestParam(value = "p-categoria", required = false) Long categoriaId,
            @RequestParam(value = "p-marcas", required = false) List<Long> marcaIds,
            @RequestParam(value = "p-min", required = false) Double minPrecio,
            @RequestParam(value = "p-max", required = false) Double maxPrecio,
            @RequestParam(value = "p-oferta", required = false) Boolean enOferta,
            @RequestParam(value = "p-palabraclave", required = false) String palabraClave,
            Model model
    ) {

        // ATRIBUTO PARA LA LISTA DE TODAS LAS CATEGORIAS EN EL SISTEMA
        model.addAttribute("categorias", categoriaService.ListarCategorias());
        // ATRIBUTO PARA LA LISTA DE TODOS LOS PRODUCTOS EN EL SISTEMA
        model.addAttribute("productos", productoService.ListarProductosPorFiltrosDeBusqueda(
                categoriaId, marcaIds, minPrecio, maxPrecio, enOferta, palabraClave));
        // ATRIBUTO PARA CONTAR TODOS LOS PRODUCTOS EN EL SISTEMA
        model.addAttribute("cantidad", productoService.ContarProductosPorFiltrosDeBusqueda(
                categoriaId, marcaIds, minPrecio, maxPrecio, enOferta, palabraClave));
        // ATRIBUTO PARA LA LISTA DE TODAS LAS MARCAS EN EL SISTEMA
        model.addAttribute("marcas", marcaService.ListarMarcas());
        // ATRIBUTO PARA RECOGER LA PALABRA CLAVE Y APLICARLO A LA RUTA
        model.addAttribute("palabraClave", palabraClave);

        logger.info("MOSTRANDO LOS TODOS LOS PRODUCTOS POR LA PALABRA CLAVE: "
                + palabraClave + ", ADEMÁS DE LOS PARAMETROS "
                + "CATEGORIA: " + categoriaId
                + ", MARCAS: " + marcaIds
                + ", RANGO DE PRECIOS: ["
                + minPrecio + "," + maxPrecio
                + "] Y OFERTA: " + enOferta
        );

        return "admin_productos";
    }
     */
 
    // RUTA A LA PAGINA DE LISTA DE PRODUCTOS: http://localhost:8080/admin/productos , INCLUYE LA PAGINACIÓN Y FILTRACIÓN A LA VEZ
    // Controlador que maneja la visualización de productos con opciones de filtrado y paginación.
    // Permite a los usuarios filtrar productos por categoría, marcas, precio, ofertas y palabras clave, 
    // y ver los resultados paginados.
    // categoriaId : (Opcional) El ID de la categoría por la cual se desean filtrar los productos.
    // marcasParams : (Opcional) Un array de ID de marcas para filtrar los productos. Permite la selección de múltiples marcas.
    // minPrecio : (Opcional) El precio mínimo de los productos a mostrar.
    // maxPrecio : (Opcional) El precio máximo de los productos a mostrar.
    // enOferta : (Opcional) Si se establece en true, solo se mostrarán productos en oferta.
    // palabraClave : (Opcional) Una palabra clave para buscar productos por nombre o descripción.
    // page : (Predeterminado: 0) El número de página que se debe mostrar en los resultados.
    // model : El modelo que se utilizará para pasar datos a la vista.
    // Retorna la vista que muestra los productos filtrados y paginados.
    @GetMapping("/productos")
    public String ListarTodosLosProductosDelSistemaPorFiltros(
            @RequestParam(name = "p-categoria", required = false) Long categoriaId,
            @RequestParam(name = "p-marcas", required = false) String[] marcasParams,
            @RequestParam(name = "p-min", required = false) Double minPrecio,
            @RequestParam(name = "p-max", required = false) Double maxPrecio,
            @RequestParam(name = "p-oferta", required = false) Boolean enOferta,
            @RequestParam(name = "p-palabraclave", required = false) String palabraClave,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model
    ) {

        // INICIALIZA UNA LISTA DE IDS DE MARCAS PARA EL FILTRADO
        List<Long> marcaIds = new ArrayList<>();

        // SI SE PROPORCIONARON PARAMETROS DE MARCA, SE CONVIERTEN EN IDS DE MARCAS Y SE AGREGAN A LA LISTA
        if (marcasParams != null) {
            for (String marcaParam : marcasParams) {
                try {
                    Long marcaId = Long.parseLong(marcaParam);
                    marcaIds.add(marcaId);
                } catch (NumberFormatException e) {
                    logger.info("NO SE ENCONTRO LA MARCA " + e);
                }
            }
        }

        // CONSTRUYE UNA URL DE CONSULTA PARA LA PAGINACIÓN Y FILTRACIÓN DE PRODUCTOS.
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/admin/productos");

        // AGREGA PARAMETROS A LA URL SI LOS VALORES NO SON NULOS O VACIOS.
        if (categoriaId != null) {
            builder.queryParam("p-categoria", categoriaId);
        }
        if (!marcaIds.isEmpty()) {
            String marcasParam = String.join(",", marcaIds.stream().map(String::valueOf).collect(Collectors.toList()));
            builder.queryParam("p-marcas", marcasParam);
        }
        if (marcaIds.isEmpty()) {
            marcaIds = Collections.emptyList();
        }
        if (minPrecio != null) {
            builder.queryParam("p-min", minPrecio);
        }
        if (maxPrecio != null) {
            builder.queryParam("p-max", maxPrecio);
        }
        if (enOferta != null) {
            builder.queryParam("p-oferta", enOferta);
        }
        if (palabraClave != null && !palabraClave.isEmpty()) {
            builder.queryParam("p-palabraclave", palabraClave);
        }

        // OBTIENE LA URL CON LOS PARAMETROS DE BUSQUEDA Y LO AGREGA AL MODELO PARA SU USO EN LA VISTA.
        String urlParams = builder.toUriString();
        model.addAttribute("urlParams", urlParams);

        // 10 REGISTROS COMO MAXIMO POR UNA PAGINA
        Pageable pageRequest = PageRequest.of(page, 10);
        
        // LLAMA AL SERVICIO PARA PAGINAR Y FILTRAR LOS PRODUCTOS
        Page<ProductoEntity> productoEntity = productoService.PaginarProductosPorFiltrosDeBusqueda(categoriaId, marcaIds, minPrecio, maxPrecio, enOferta, palabraClave, pageRequest);
        PageRender<ProductoEntity> pageRender = new PageRender<>("/admin/productos", productoEntity);

        // ATRIBUTO PARA LA LISTA DE TODOS LOS PRODUCTOS EN EL SISTEMA
        model.addAttribute("productos", productoEntity);
        
        // ATRIBUTO PARA LA PAGINACIÓN
        model.addAttribute("page", pageRender);
        
        // ATRIBUTO PARA LA LISTA DE TODAS LAS CATEGORIAS EN EL SISTEMA
        model.addAttribute("categorias", categoriaService.ListarCategorias());
        
        // ATRIBUTO PARA CONTAR TODOS LOS PRODUCTOS EN EL SISTEMA
        model.addAttribute("cantidad", productoService.ContarProductosPorFiltrosDeBusqueda(
                categoriaId, marcaIds, minPrecio, maxPrecio, enOferta, palabraClave));
        
        // ATRIBUTO PARA LA LISTA DE TODAS LAS MARCAS EN EL SISTEMA
        model.addAttribute("marcas", marcaService.ListarMarcas());
        
        // ATRIBUTO PARA RECOGER LA PALABRA CLAVE Y APLICARLO A LA RUTA
        model.addAttribute("palabraClave", palabraClave);

        logger.info("MOSTRANDO LOS TODOS LOS PRODUCTOS POR LA PALABRA CLAVE: "
                + palabraClave + ", ADEMÁS DE LOS PARAMETROS "
                + "CATEGORIA: " + categoriaId
                + ", MARCAS: " + marcaIds
                + ", RANGO DE PRECIOS: ["
                + minPrecio + "," + maxPrecio
                + "] Y OFERTA: " + enOferta
        );

        return "admin_productos";

    }
    

    // RUTA A LA PAGINA DE DETALLES DE UN PRODUCTO: http://localhost:8080/admin/productos/...
    @GetMapping("/productos/{id_producto}")
    public String MostrarDatosDeUnProducto(@PathVariable Long id_producto, Model model) {
        // ATRIBUTO PARA OBTENER UN PRODUCTO POR SU ID
        model.addAttribute("detallesProducto", productoService.ObtenerProductoPorId(id_producto));
        logger.info("MOSTRANDO EL PRODUCTO CON EL ID: " + id_producto);
        return "admin_un_producto";
    }

    // RUTA A LA PAGINA DE NUEVO PRODUCTO: http://localhost:8080/admin/productos/nuevo
    @GetMapping("/productos/nuevo")
    public String MostrarFormularioDeAgregarProducto(Model model) {
        // ATRIBUTOS PARA OBTENER LA LISTA DE CATEGORIAS Y MARCAS
        model.addAttribute("categorias", categoriaService.ListarCategoriasByEstadoTrue());
        model.addAttribute("marcas", marcaService.ListarMarcasByEstadoTrue());
        // ATRIBUTO PARA CREAR LOS PARAMETROS QUE SE VAN UTILIZAR
        ProductoEntity productoEntity = new ProductoEntity();
        model.addAttribute("productoEntity", productoEntity);
        logger.info("MOSTRANDO EL FORMULARIO PARA AGREGAR UN NUEVO PRODUCTO");
        return "admin_form_nuevo_producto";
    }

    // AL HACER CLIC EN EL BOTON PARA GUARDAR UN NUEVO PRODUCTO, VA IR A LA RUTA: http://localhost:8080/admin/productos
    /*
    @PostMapping("/productos/nuevo")
    public String GuardarNuevoProducto(
            @Validated @ModelAttribute("productoEntity") ProductoEntity productoEntity,
            BindingResult result,
            @RequestPart("imagenproducto_imagen") MultipartFile imagenproducto_imagen,
            Model model
    ) {

        // SI HAY UN ERROR EN LOS CAMPOS O SI NO SE HA SUBIDO UNA IMAGEN
        if (result.hasErrors() || imagenproducto_imagen.isEmpty()) {
            // ATRIBUTOS PARA LISTAR MARCAS Y CATEGORIAS
            model.addAttribute("categorias", categoriaService.ListarCategoriasByEstadoTrue());
            model.addAttribute("marcas", marcaService.ListarMarcasByEstadoTrue());
            // ATRIBUTO PARA RECUPERAR LOS DATOS QUE SE INTENTARON ENVIAR
            model.addAttribute("productoEntity", productoEntity);

            // MOSTRAR UN MENSAJE EN LA PAGINA WEB SI NO SE HA SUBIDO UNA IMAGEN
            if (imagenproducto_imagen.isEmpty()) {
                model.addAttribute("error_imagen", "Es necesario que suba una imagen");
                logger.info("SUBA UNA IMAGEN");
            }

            // IR A LA PAGINA DEL FORMULARIO
            logger.info("COMPLETE LOS CAMPOS FALTANTES");
            return "admin_form_nuevo_producto";
        }

        // Si no hay errores de validación, procede con el guardado del producto
        // productoService.GuardarProducto(productoEntity, imagenproducto_imagen);
        productoService.GuardarProducto(productoEntity);
        logger.info("SE HA GUARDADO EL PRODUCTO: " + productoEntity);
        return "redirect:/admin/productos";

    }

*/
    @PostMapping("/productos/nuevo")
    public String guardarproducto(@ModelAttribute("productoEntity") ProductoEntity productoEntity) {
        productoService.GuardarProducto(productoEntity);
        return "redirect:/admin/productos";
    }

    /*
    @PostMapping("/productos/nuevo")
    public String guardarNuevoProducto(
            @Validated @ModelAttribute("productoEntity") ProductoEntity productoEntity,
            BindingResult result,
            @RequestPart("imagenproducto_imagen") MultipartFile imagenproducto_imagen,
            Model model

            
    ) {
        // Validar errores en los campos o si no se ha subido una imagen
        if (result.hasErrors() || imagenproducto_imagen.isEmpty()) {
            // Atributos para listar marcas y categorías
            model.addAttribute("categorias", categoriaService.ListarCategoriasByEstadoTrue());
            model.addAttribute("marcas", marcaService.ListarMarcasByEstadoTrue());
            // Atributo para recuperar los datos que se intentaron enviar
            model.addAttribute("productoEntity", productoEntity);

            // Mostrar un mensaje en la página web si no se ha subido una imagen
            if (imagenproducto_imagen.isEmpty()) {
                model.addAttribute("error_imagen", "Es necesario que suba una imagen");
                logger.info("Suba una imagen");
            }

            // Ir a la página del formulario
            logger.info("Complete los campos faltantes");
            return "admin_form_nuevo_producto";
        }

        // Si no hay errores de validación, proceder con el guardado del producto
        productoService.GuardarProducto(productoEntity);
        logger.info("Se ha guardado el producto: " + productoEntity);
        return "redirect:/admin/productos";
    }
*/
    
// RUTA A LA PAGINA DE EDITAR UN PRODUCTO QUE EXISTE: http://localhost:8080/admin/productos/.../editar
    @GetMapping("/productos/{id_producto}/editar")
    public String MostrarFormularioDeEditarProducto(
            @PathVariable Long id_producto,
            Model model
    ) {
        // ATRIBUTOS PARA OBTENER LA LISTA DE CATEGORIAS Y MARCAS
        // model.addAttribute("categorias", categoriaService.ListarCategoriasByEstadoTrue());
        // model.addAttribute("marcas", marcaService.ListarMarcasByEstadoTrue());
        var listacategorias = categoriaService.ListarCategoriasByEstadoTrue();
        model.addAttribute("categorias", listacategorias);
        var listamarcas = marcaService.ListarMarcasByEstadoTrue();
        model.addAttribute("marcas", listamarcas);

        // ATRIBUTO PARA RECOGER LOS DATOS DEL PRODUCTO SEGÚN SU ID
        model.addAttribute("productoEntity", productoService.ObtenerProductoPorId(id_producto));
        logger.info("MOSTRANDO EL FORMULARIO PARA EDITAR EL PRODUCTO CON EL ID: " + id_producto);
        return "admin_form_editar_producto";
    }

    // AL HACER CLIC EN EL BOTON PARA GUARDAR LOS CAMBIOS REALIZADOS AL PRODUCTO, VA IR A LA RUTA: http://localhost:8080/admin/productos
    @PostMapping("/productos/{id_producto}/editar")
    public String GuardarProductoEditado(
            @PathVariable Long id_producto,
            @Validated @ModelAttribute("productoEntity") ProductoEntity productoEntity,
            //BindingResult result,
            //@RequestParam("imagenproducto_imagen") MultipartFile imagenproducto_imagen,
            Model model
    ) {
        /*
        if (result.hasErrors()) {
            // ATRIBUTOS PARA LISTAR MARCAS Y CATEGORIAS
            
            //model.addAttribute("categorias", categoriaService.ListarCategoriasByEstadoTrue());
            //model.addAttribute("marcas", marcaService.ListarMarcasByEstadoTrue());
            var listacategorias = categoriaService.ListarCategoriasByEstadoTrue();
            model.addAttribute("categorias", listacategorias);
            var listamarcas = marcaService.ListarMarcasByEstadoTrue();
            model.addAttribute("marcas", listamarcas);

            // ATRIBUTO PARA RECUPERAR LOS DATOS QUE SE INTENTARON ENVIAR
            model.addAttribute("productoEntity", productoEntity);
            logger.info("COMPLETE LOS CAMPOS FALTANTES");
            return "admin_form_editar_producto";
        }
*/
        // NOTA: EL CAMPO IMAGEN NO VA A SER VALIDADO EN ESTE CASO, PUES EL ADMINISTRADOR PUEDE DEJAR EN BLANCO EL CAMPO PARA 
        // SUBIR UNA IMAGEN SI DESEA CONSERVAR LA IMAGEN ANTERIOR Y SI SUBE UNA IMAGEN, ENTONCES LA IMAGEN ANTERIOR SE BORRA 
        // DEFINITIVAMENTE DEL CONTENEDOR Y SE REEMPLAZA POR LA NUEVA
        // Si no hay errores de validación, procede con el editado del producto
        // productoService.ActualizarProducto(id_producto, productoEntity, imagenproducto_imagen);
        productoService.ActualizarProducto(productoEntity);
        logger.info("EL PRODUCTO CON EL ID: " + id_producto + " HA SIDO MODIFICADO Y SE HA GUARDADO");
        return "redirect:/admin/productos";
    }

    // AL HACER CLIC EN EL BOTON PARA INHABILITAR UN PRODUCTO, VA IR A LA RUTA: http://localhost:8080/admin/producto
    @GetMapping("/productos/{id_producto}/eliminar")
    public String EliminarProducto(@PathVariable Long id_producto) {
        // productoService.EliminarProducto(id_producto);
        productoService.CambiarEstadoProductoAFalse(id_producto);
        logger.info("EL ESTADO DEL PRODUCTO CON EL ID " + id_producto + " HA CAMBIADO A FALSE (HA SIDO ELIMINADO)");
        return "redirect:/admin/productos";
    }

    // AL HACER CLIC EN EL BOTON PARA HABILITAR UN PRODUCTO, VA IR A LA RUTA: http://localhost:8080/admin/producto
    @GetMapping("/productos/{id_producto}/restaurar")
    public String RestaurarProducto(@PathVariable Long id_producto) {
        productoService.CambiarEstadoProductoATrue(id_producto);
        logger.info("EL ESTADO DEL PRODUCTO CON EL ID " + id_producto + " HA CAMBIADO A TRUE (HA SIDO RESTAURADO)");
        return "redirect:/admin/productos";
    }

    // CONTROLADOR PARA LA BUSQUEDA DE PRODUCTOS POR PARTE DEL ADMINISTRADOR
    @GetMapping("/productos/buscar")
    public String ListarTodosLosProductosDelSistemaPorPalabraClave(
            @RequestParam(name = "p-categoria", required = false) Long categoriaId,
            @RequestParam(name = "p-marcas", required = false) String[] marcasParams,
            @RequestParam(name = "p-min", required = false) Double minPrecio,
            @RequestParam(name = "p-max", required = false) Double maxPrecio,
            @RequestParam(name = "p-oferta", required = false) Boolean enOferta,
            @RequestParam(name = "p-palabraclave", required = false) String palabraClave,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model
    ) {

        // INICIALIZA UNA LISTA DE IDS DE MARCAS PARA EL FILTRADO
        List<Long> marcaIds = new ArrayList<>();

        // SI SE PROPORCIONARON PARAMETROS DE MARCA, SE CONVIERTEN EN IDS DE MARCAS Y SE AGREGAN A LA LISTA
        if (marcasParams != null) {
            for (String marcaParam : marcasParams) {
                try {
                    Long marcaId = Long.parseLong(marcaParam);
                    marcaIds.add(marcaId);
                } catch (NumberFormatException e) {
                    logger.info("NO SE ENCONTRO LA MARCA " + e);
                }
            }
        }

        // CONSTRUYE UNA URL DE CONSULTA PARA LA PAGINACIÓN Y FILTRACIÓN DE PRODUCTOS.
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/admin/productos/buscar");

        // AGREGA PARAMETROS A LA URL SI LOS VALORES NO SON NULOS O VACIOS.
        if (categoriaId != null) {
            builder.queryParam("p-categoria", categoriaId);
        }
        if (!marcaIds.isEmpty()) {
            String marcasParam = String.join(",", marcaIds.stream().map(String::valueOf).collect(Collectors.toList()));
            builder.queryParam("p-marcas", marcasParam);
        }
        if (marcaIds.isEmpty()) {
            marcaIds = Collections.emptyList();
        }
        if (minPrecio != null) {
            builder.queryParam("p-min", minPrecio);
        }
        if (maxPrecio != null) {
            builder.queryParam("p-max", maxPrecio);
        }
        if (enOferta != null) {
            builder.queryParam("p-oferta", enOferta);
        }
        if (palabraClave != null && !palabraClave.isEmpty()) {
            builder.queryParam("p-palabraclave", palabraClave);
        }

        // OBTIENE LA URL CON LOS PARAMETROS DE BUSQUEDA Y LO AGREGA AL MODELO PARA SU USO EN LA VISTA.
        String urlParams = builder.toUriString();
        model.addAttribute("urlParams", urlParams);

        // 10 REGISTROS COMO MAXIMO POR UNA PAGINA
        Pageable pageRequest = PageRequest.of(page, 10);

        // LLAMA AL SERVICIO PARA PAGINAR Y FILTRAR LOS PRODUCTOS
        Page<ProductoEntity> productoEntity = productoService.PaginarProductosPorFiltrosDeBusqueda(categoriaId, marcaIds, minPrecio, maxPrecio, enOferta, palabraClave, pageRequest);
        PageRender<ProductoEntity> pageRender = new PageRender<>("/productos/buscar", productoEntity);

        // ATRIBUTO PARA LA LISTA DE TODOS LOS PRODUCTOS EN EL SISTEMA
        model.addAttribute("productos", productoEntity);

        // ATRIBUTO PARA LA PAGINACIÓN
        model.addAttribute("page", pageRender);

        // ATRIBUTO PARA LA LISTA DE TODAS LAS CATEGORIAS EN EL SISTEMA
        model.addAttribute("categorias", categoriaService.ListarCategorias());

        // ATRIBUTO PARA CONTAR TODOS LOS PRODUCTOS EN EL SISTEMA
        model.addAttribute("cantidad", productoService.ContarProductosPorFiltrosDeBusqueda(
                categoriaId, marcaIds, minPrecio, maxPrecio, enOferta, palabraClave));

        // ATRIBUTO PARA LA LISTA DE TODAS LAS MARCAS EN EL SISTEMA
        model.addAttribute("marcas", marcaService.ListarMarcas());

        // ATRIBUTO PARA RECOGER LA PALABRA CLAVE Y APLICARLO A LA RUTA
        model.addAttribute("palabraClave", palabraClave);

        logger.info("MOSTRANDO LOS TODOS LOS PRODUCTOS POR LA PALABRA CLAVE: "
                + palabraClave + ", ADEMÁS DE LOS PARAMETROS "
                + "CATEGORIA: " + categoriaId
                + ", MARCAS: " + marcaIds
                + ", RANGO DE PRECIOS: ["
                + minPrecio + "," + maxPrecio
                + "] Y OFERTA: " + enOferta
        );

        return "admin_productos_busqueda";

    }

    
    
    
    
}

package com.tiendaonline.controller;

import com.tiendaonline.entity.CategoriaEntity;
import com.tiendaonline.entity.ProductoEntity;
import com.tiendaonline.service.CategoriaService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("/productos")
public class ProductoController {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private MarcaService marcaService;

    @GetMapping
    public String ListarTodosLosProductos(
            @RequestParam(name = "p-categoria", required = false) Long categoriaId,
            @RequestParam(name = "p-marcas", required = false) String[] marcasParams,
            @RequestParam(name = "p-min", required = false) Double minPrecio,
            @RequestParam(name = "p-max", required = false) Double maxPrecio,
            @RequestParam(name = "p-oferta", required = false) Boolean enOferta,
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
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/productos");

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

        // OBTIENE LA URL CON LOS PARAMETROS DE BUSQUEDA Y LO AGREGA AL MODELO PARA SU USO EN LA VISTA.
        String urlParams = builder.toUriString();
        model.addAttribute("urlParams", urlParams);

        // 10 REGISTROS COMO MAXIMO POR UNA PAGINA
        Pageable pageRequest = PageRequest.of(page, 12);

        // Llama al servicio para paginar y filtrar los productos
        Page<ProductoEntity> productoEntity = productoService.PaginarProductosHabilitadosPorFiltrosDeBusqueda(categoriaId, marcaIds, minPrecio, maxPrecio, enOferta, null, pageRequest);
        PageRender<ProductoEntity> pageRender = new PageRender<>("/productos", productoEntity);

        // Agrega los resultados y otros datos al modelo
        // model.addAttribute("productos", productoService.ListarProductosHabilitadosPorFiltrosDeBusqueda(categoriaId, marcaIds, minPrecio, maxPrecio, enOferta, null));
        model.addAttribute("productos", productoEntity);
        model.addAttribute("page", pageRender);
        model.addAttribute("categorias", categoriaService.ListarCategoriasByEstadoTrue());
        model.addAttribute("marcas", marcaService.ListarMarcasByEstadoTrue());

        logger.info("MOSTRANDO LOS TODOS LOS PRODUCTOS POR "
                + "CATEGORIA: " + categoriaId
                + ", MARCAS: " + marcaIds
                + ", RANGO DE PRECIOS: ["
                + minPrecio + "," + maxPrecio
                + "] Y OFERTA: " + enOferta
        );

        return "productos_general";
    }
    
    @GetMapping("/{categoriaNombre}")
    public String ListarTodosLosProductosPorCategoria(
            @PathVariable String categoriaNombre,
            @RequestParam(value = "p-marcas", required = false) String[] marcasParams,
            @RequestParam(value = "p-min", required = false) Double minPrecio,
            @RequestParam(value = "p-max", required = false) Double maxPrecio,
            @RequestParam(value = "p-oferta", required = false) Boolean enOferta,
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
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/productos/" + categoriaNombre);
        // builder.queryParam("p-categoria", categoriaDto.getId_categoria());

        // AGREGA PARAMETROS A LA URL SI LOS VALORES NO SON NULOS O VACIOS.
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

        // OBTIENE LA URL CON LOS PARAMETROS DE BUSQUEDA Y LO AGREGA AL MODELO PARA SU USO EN LA VISTA.
        String urlParams = builder.toUriString();
        model.addAttribute("urlParams", urlParams);

        // OBTENER EL NOMBRE DE LA CATEGORIA
        CategoriaEntity categoriaEntity = categoriaService.ObtenerCategoriaPorNombre(categoriaNombre);

        // 10 REGISTROS COMO MAXIMO POR UNA PAGINA
        Pageable pageRequest = PageRequest.of(page, 12);
        // Llama al servicio para paginar y filtrar los productos
        Page<ProductoEntity> productoEntity = productoService.PaginarProductosHabilitadosPorFiltrosDeBusqueda(categoriaEntity.getId_categoria(), marcaIds, minPrecio, maxPrecio, enOferta, null, pageRequest);
        PageRender<ProductoEntity> pageRender = new PageRender<>("/productos/{categoriaNombre}", productoEntity);
        model.addAttribute("productos", productoEntity);
        model.addAttribute("page", pageRender);
        model.addAttribute("categorias", categoriaService.ListarCategoriasByEstadoTrue());
        model.addAttribute("categoriaActual", categoriaService.ObtenerCategoriaPorNombre(categoriaNombre));
        model.addAttribute("marcas", marcaService.ListarDistintasMarcasByGrupoCategoriaAndEstadoTrue(categoriaNombre));
        logger.info("MOSTRANDO LOS TODOS LOS PRODUCTOS QUE CORRESPONDEN "
                + "A LA CATEGORIA: " + categoriaEntity.getId_categoria()
                + ", ADEMÁS DE LOS PARAMETROS MARCAS: " + marcaIds
                + ", RANGO DE PRECIOS: ["
                + minPrecio + "," + maxPrecio
                + "], Y OFERTA: " + enOferta
        );
        return "productos_categoria";
    }

    @GetMapping("/oferta")
    public String ListarTodosLosProductosEnOferta(
            @RequestParam(value = "p-categoria", required = false) Long categoriaId,
            @RequestParam(value = "p-marcas", required = false) String[] marcasParams,
            @RequestParam(value = "p-min", required = false) Double minPrecio,
            @RequestParam(value = "p-max", required = false) Double maxPrecio,
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
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/productos/oferta");

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

        // OBTIENE LA URL CON LOS PARAMETROS DE BUSQUEDA Y LO AGREGA AL MODELO PARA SU USO EN LA VISTA.
        String urlParams = builder.toUriString();
        model.addAttribute("urlParams", urlParams);

        // 10 REGISTROS COMO MAXIMO POR UNA PAGINA
        Pageable pageRequest = PageRequest.of(page, 12);

        // Llama al servicio para paginar y filtrar los productos
        Page<ProductoEntity> variosProductoDto = productoService.PaginarProductosHabilitadosPorFiltrosDeBusqueda(categoriaId, marcaIds, minPrecio, maxPrecio, Boolean.TRUE, null, pageRequest);
        PageRender<ProductoEntity> pageRender = new PageRender<>("/productos/oferta", variosProductoDto);

        // Agrega los resultados y otros datos al modelo
        model.addAttribute("productos", variosProductoDto);
        model.addAttribute("page", pageRender);
        model.addAttribute("categorias", categoriaService.ListarCategoriasByEstadoTrue());
        model.addAttribute("marcas", marcaService.ListarMarcasByEstadoTrue());
        logger.info("MOSTRANDO LOS TODOS LOS PRODUCTOS EN OFERTA"
                + ", ADEMÁS DE LOS PARAMETROS "
                + "CATEGORIA: " + categoriaId
                + ", MARCAS: " + marcaIds
                + "Y RANGO DE PRECIOS: ["
                + minPrecio + "," + maxPrecio
                + "],"
        );

        return "productos_oferta";
    }

    @GetMapping("/buscar")
    public String ListarElResultadoDeUnaBusquedaDeProductos(
            @RequestParam(value = "p-categoria", required = false) Long categoriaId,
            @RequestParam(value = "p-marcas", required = false) String[] marcasParams,
            @RequestParam(value = "p-min", required = false) Double minPrecio,
            @RequestParam(value = "p-max", required = false) Double maxPrecio,
            @RequestParam(value = "p-oferta", required = false) Boolean enOferta,
            @RequestParam(value = "p-palabraclave", required = false) String palabraClave,
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
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/productos/buscar");

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
        Pageable pageRequest = PageRequest.of(page, 12);

        // LLAMA AL SERVICIO PARA PAGINAR Y FILTRAR LOS PRODUCTOS
        Page<ProductoEntity> productoEntity = productoService.PaginarProductosHabilitadosPorFiltrosDeBusqueda(categoriaId, marcaIds, minPrecio, maxPrecio, enOferta, palabraClave, pageRequest);
        PageRender<ProductoEntity> pageRender = new PageRender<>("/productos/buscar", productoEntity);

        // ATRIBUTO PARA LA LISTA DE TODOS LOS PRODUCTOS
        model.addAttribute("productos", productoEntity);
        // ATRIBUTO PARA LA PAGINACIÓN
        model.addAttribute("page", pageRender);
        
        model.addAttribute("categorias", categoriaService.ListarCategoriasByEstadoTrue());
        model.addAttribute("cantidad", productoService.ContarProductosHabilitadosPorFiltrosDeBusqueda(
                categoriaId, marcaIds, minPrecio, maxPrecio, enOferta, palabraClave));
        model.addAttribute("marcas", marcaService.ListarMarcasByEstadoTrue());
        model.addAttribute("palabraClave", palabraClave);
        logger.info("MOSTRANDO LOS TODOS LOS PRODUCTOS POR LA PALABRA CLAVE: "
                + palabraClave + ", ADEMÁS DE LOS PARAMETROS "
                + "CATEGORIA: " + categoriaId
                + ", MARCAS: " + marcaIds
                + ", RANGO DE PRECIOS: ["
                + minPrecio + "," + maxPrecio
                + "] Y OFERTA: " + enOferta
        );
        return "productos_busqueda";
    }

    @GetMapping("/{categoriaNombre}/{id_producto}")
    public String MostrarUnProducto(
            @PathVariable Long id_producto,
            @PathVariable String categoriaNombre,
            Model model) {

        model.addAttribute("categorias", categoriaService.ListarCategoriasByEstadoTrue());
        model.addAttribute("detallesProducto", productoService.ObtenerProductoPorId(id_producto));
        logger.info("MOSTRANDO EL PRODUCTO CON EL ID: " + id_producto);
        return "un_producto";
    }


}

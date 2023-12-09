// Función que se ejecuta cuando se presiona el botón "Filtrar" en el formulario.
// Recopila los valores de los controles de filtro, construye una URL con estos valores
// y redirige al usuario a la página de productos filtrada.
function filtrarProductos() {

    // Obtener el valor seleccionado para la categoría
    const categoriaSeleccionada = document.querySelector('input[name="p-categoria"]:checked');

    // Obtener todas las casillas de verificación marcadas
    const checkboxes = document.querySelectorAll('input[name="p-marcas"]:checked');

    // Obtener los valores seleccionados y crear una lista separada por comas
    const valoresMarcas = Array.from(checkboxes).map(checkbox => checkbox.value).join(',');

    // Obtener los valores de precio mínimo y máximo
    const minPrecio = document.querySelector('input[name="p-min"]').value;
    const maxPrecio = document.querySelector('input[name="p-max"]').value;

    // Verificar si la casilla de oferta está marcada
    const enOferta = document.querySelector('input[name="p-oferta"]').checked;

    // Construir la URL con los valores seleccionados
    let url = '/productos?';

    if (categoriaSeleccionada) {
        if (categoriaSeleccionada.value !== "") {
            url += `p-categoria=${categoriaSeleccionada.value}&`;
        }
    }

    if (valoresMarcas) {
        url += `p-marcas=${valoresMarcas}&`;
    }

    if (minPrecio) {
        url += `p-min=${minPrecio}&`;
    }

    if (maxPrecio) {
        url += `p-max=${maxPrecio}&`;
    }

    if (enOferta) {
        url += `p-oferta=true&`;
    }

    // Eliminar el último carácter '&' si está presente
    if (url.endsWith('&')) {
        url = url.slice(0, -1);
    }

    // Prevenir el envío predeterminado del formulario
    event.preventDefault();

    // Redirigir al usuario a la URL filtrada
    window.location.href = url;

}


// Función que marca el radio button de la categoría basado en los parámetros de la URL y filtra productos al cargar la página.
function marcarCategoriaDesdeURL() {

    // Obtener los parámetros de la URL
    const parametrosURL = new URLSearchParams(window.location.search);

    // Obtener el valor de categoría seleccionada de los parámetros de la URL
    const categoriaSeleccionada = parametrosURL.get('p-categoria');

    // Obtener todos los radio buttons de categoría que no tengan el valor "null"
    const otrosRadios = document.querySelectorAll('input[name="p-categoria"]:not([value="null"])');

    // Obtener el radio button "Todos" por su ID
    const rbtodos = document.getElementById('rbocategoria');

    // Verificar si alguna otra categoría está marcada al cargar la página
    otrosRadios.forEach(radio => {
        if (radio.checked) {
            categoriaSeleccionada = true;
        }
    });

    // Marcar el radio button "Todos" si no se selecciona ninguna otra categoría
    if (!categoriaSeleccionada) {
        rbtodos.checked = true;
    }

    // Filtrar productos al cargar la página
    if (categoriaSeleccionada) {
        const radioButtonsCategoria = document.querySelectorAll('input[name="p-categoria"]');
        radioButtonsCategoria.forEach(radio => {
            if (radio.value === categoriaSeleccionada) {
                radio.checked = true;
            }
        });
    }

}

// Llamar a la función cuando la página se cargue
window.addEventListener('load', marcarCategoriaDesdeURL);


// Función para marcar las casillas de verificación (checkboxes) basadas en los parámetros de la URL.
function marcarCheckboxesDesdeURL() {

    // Obtener los parámetros de la URL actual
    const parametrosURL = new URLSearchParams(window.location.search);

    // Obtener los valores seleccionados para la variable 'p-marcas' de la URL
    const marcasSeleccionadas = parametrosURL.get('p-marcas');

    if (marcasSeleccionadas) {

        // Dividir los valores de 'p-marcas' separados por comas en una lista de nombres de marca
        const marcaNombres = marcasSeleccionadas.split(',');

        // Iterar a través de los nombres de marca
        marcaNombres.forEach(marcaNombre => {

            // Buscar la casilla de verificación (checkbox) correspondiente por su valor y marcarla si se encuentra
            const checkbox = document.querySelector(`input[name="p-marcas"][value="${marcaNombre}"]`);
            if (checkbox) {
                checkbox.checked = true;
            }

        });

    }

}

// Llamar a la función cuando la página se carga (evento 'load')
window.addEventListener('load', marcarCheckboxesDesdeURL);


// Función para restaurar los valores de precio al cargar la página basados en los parámetros de la URL.
function restaurarValoresDePrecio() {
    
    // Obtener los parámetros de la URL actual
    const parametrosURL = new URLSearchParams(window.location.search);
    
    // Obtener las referencias a los campos de entrada de precio mínimo y máximo en la página
    const precioMinInput = document.getElementById('txtpreciomin');
    const precioMaxInput = document.getElementById('txtpreciomax');
    
    // Establecer los valores de los campos de entrada de precio a partir de los parámetros de la URL
    // Si los parámetros no existen en la URL, establecer los valores en blanco ('') por defecto
    precioMinInput.value = parametrosURL.get('p-min') || '';
    precioMaxInput.value = parametrosURL.get('p-max') || '';
    
}

// Llamar a la función cuando la página se carga (evento 'load')
window.addEventListener('load', restaurarValoresDePrecio);


// Función para marcar la casilla de verificación de oferta al cargar la página, basada en los parámetros de la URL.
function marcarCheckboxOferta() {
    
    // Obtener los parámetros de la URL actual
    const parametrosURL = new URLSearchParams(window.location.search);
    
    // Obtener el valor de 'p-oferta' de los parámetros de la URL
    const ofertaSeleccionada = parametrosURL.get('p-oferta');

    // Verificar si el valor de 'p-oferta' en la URL es "true"
    if (ofertaSeleccionada === "true") {
        
        // Obtener la referencia a la casilla de verificación de oferta en la página
        const checkbox = document.querySelector('input[name="p-oferta"]');
        
        // Marcar la casilla de verificación si existe
        if (checkbox) {
            checkbox.checked = true;
            
        }
        
    }
    
}

// Llamar a la función cuando la página se carga (evento 'load')
window.addEventListener('load', marcarCheckboxOferta);


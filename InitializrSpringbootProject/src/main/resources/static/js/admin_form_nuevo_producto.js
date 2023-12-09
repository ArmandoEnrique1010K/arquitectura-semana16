function mostrarNombreArchivo() {
    var input = document.getElementById('btnimagen');
    var nombreArchivo = input.files[0].name;
    document.getElementById('nombre-archivo').innerText = nombreArchivo;
}
document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('.formulario');
    const imagenInput = document.getElementById('btnimagen');
    const nombreArchivo = document.getElementById('nombre-archivo');
    const ofertaCheckbox = document.getElementById('chkoferta');
    const precioOfertaInput = document.getElementById('txtpreciooferta');

    form.addEventListener('submit', function (event) {
        // Limpiar mensajes de error
        clearErrorMessages();

        // Validar campos
        const requiredFields = [
            {id: 'txtcodigo', errorId: 'error-codigo', name: 'Código'},
            {id: 'txtnombre', errorId: 'error-nombre', name: 'Nombre'},
            {id: 'cbocategoria', errorId: 'error-categoria', name: 'Categoría'},
            {id: 'cbomarca', errorId: 'error-marca', name: 'Marca'},
            {id: 'txtprecionormal', errorId: 'error-precionormal', name: 'Precio Normal'},
            {id: 'txtdescripcion', errorId: 'error-descripcion', name: 'Descripción'},
            {id: 'txtfichatecnica', errorId: 'error-fichatecnica', name: 'Ficha Técnica'},
            {id: 'btnimagen', errorId: 'error-imagen', name: 'Imagen'}
        ];

        let hasErrors = false;

        requiredFields.forEach(field => {
            if (!validateNotBlank(field.id, field.errorId, field.name)) {
                hasErrors = true;
            }
        });

        if (hasErrors) {
            event.preventDefault(); // Evitar que se envíe el formulario si hay errores
        }
    });

    function validateNotBlank(inputId, errorId, fieldName) {
        const input = document.getElementById(inputId);
        const error = document.getElementById(errorId);

        if (input.value.trim() === '') {
            error.innerText = `El campo ${fieldName} no puede estar en blanco.`;
            return false;
        }

        return true;
    }

    function validateSelect(selectId, errorId, fieldName) {
        const select = document.getElementById(selectId);
        const error = document.getElementById(errorId);

        if (select.value === '') {
            error.innerText = `Seleccione una opción para el campo ${fieldName}.`;
            return false;
        }

        return true;
    }

    function validateFile(inputId, errorId, fieldName) {
        const input = document.getElementById(inputId);
        const error = document.getElementById(errorId);

        if (!input.files || input.files.length === 0) {
            error.innerText = `Debe seleccionar un archivo para el campo ${fieldName}.`;
            return false;
        }

        return true;
    }

    function clearErrorMessages() {
        const errorMessages = document.querySelectorAll('.error-campo');
        errorMessages.forEach(function (error) {
            error.innerText = '';
        });
    }

    // Agregar evento change al input de la imagen
    imagenInput.addEventListener('change', function () {
        // Limpiar mensajes de error y vista previa anterior
        clearErrorMessages();
        clearImagePreview();

        // Verificar si se seleccionó un archivo
        if (imagenInput.files && imagenInput.files[0]) {
            const reader = new FileReader();

            // Configurar la función que se ejecutará cuando se complete la lectura del archivo
            reader.onload = function (e) {
                // Mostrar la vista previa de la imagen
                nombreArchivo.innerText = `Vista previa: ${imagenInput.files[0].name}`;
                mostrarImagenPreview(e.target.result);
            };

            // Leer el archivo como una URL de datos (data URL)
            reader.readAsDataURL(imagenInput.files[0]);
        }
    });

    // Resto del código de validación y envío del formulario...

    function mostrarImagenPreview(source) {
        const previewDiv = document.getElementById('imagen-preview');
        const img = document.createElement('img');
        img.src = source;
        img.alt = 'Vista previa de la imagen';
        previewDiv.appendChild(img);
    }

    function clearImagePreview() {
        const previewDiv = document.getElementById('imagen-preview');
        previewDiv.innerHTML = ''; // Limpiar contenido
    }

    // Agregar evento change al checkbox de oferta
    ofertaCheckbox.addEventListener('change', function () {
        // Habilitar o deshabilitar el campo de precio de oferta según si la opción de oferta está marcada
        precioOfertaInput.disabled = !ofertaCheckbox.checked;

        // Borrar el valor del campo de precio de oferta si la opción de oferta se desmarca
        if (!ofertaCheckbox.checked) {
            precioOfertaInput.value = '';
        }
    });

});

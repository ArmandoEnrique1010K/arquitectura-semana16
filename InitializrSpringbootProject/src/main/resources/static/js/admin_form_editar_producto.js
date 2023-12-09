document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('.formulario');
    const imagenInput = document.getElementById('btnimagen');
    const nombreArchivo = document.getElementById('nombre-archivo');
    const ofertaCheckbox = document.getElementById('chkoferta');
    const precioOfertaInput = document.getElementById('txtpreciooferta');
    const imagenPreviewDiv = document.getElementById('imagen-preview');

    form.addEventListener('submit', function (event) {
        clearErrorMessages();

        const requiredFields = [
            {id: 'txtcodigo', errorId: 'error-codigo', name: 'Código'},
            {id: 'txtnombre', errorId: 'error-nombre', name: 'Nombre'},
            {id: 'cbocategoria', errorId: 'error-categoria', name: 'Categoría'},
            {id: 'cbomarca', errorId: 'error-marca', name: 'Marca'},
            {id: 'txtprecionormal', errorId: 'error-precionormal', name: 'Precio Normal'},
            {id: 'txtdescripcion', errorId: 'error-descripcion', name: 'Descripción'},
            {id: 'txtfichatecnica', errorId: 'error-fichatecnica', name: 'Ficha Técnica'}
        ];

        let hasErrors = false;

        requiredFields.forEach(field => {
            if (!validateNotBlank(field.id, field.errorId, field.name)) {
                hasErrors = true;
            }
        });

        if (!validateSelect('cbocategoria', 'error-categoria', 'Categoría')) {
            hasErrors = true;
        }

        if (!validateSelect('cbomarca', 'error-marca', 'Marca')) {
            hasErrors = true;
        }

        if (!validateFile('btnimagen', 'error-imagen', 'Imagen')) {
            hasErrors = true;
        }

        if (hasErrors) {
            event.preventDefault();
        }
    });

    // Agregar evento change al input de la imagen
    imagenInput.addEventListener('change', function () {
        clearErrorMessages();
        clearImagePreview();

        if (imagenInput.files && imagenInput.files[0]) {
            const reader = new FileReader();

            reader.onload = function (e) {
                nombreArchivo.innerText = `Vista previa: ${imagenInput.files[0].name}`;
                mostrarImagenPreview(e.target.result);
            };

            reader.readAsDataURL(imagenInput.files[0]);
        }
    });

    // Agregar evento change al checkbox de oferta
    ofertaCheckbox.addEventListener('change', function () {
        precioOfertaInput.disabled = !ofertaCheckbox.checked;

        if (!ofertaCheckbox.checked) {
            precioOfertaInput.value = '';
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

    function mostrarImagenPreview(source) {
        const img = document.createElement('img');
        img.src = source;
        img.alt = 'Vista previa de la imagen';
        imagenPreviewDiv.innerHTML = ''; // Limpiar contenido previo
        imagenPreviewDiv.appendChild(img);
    }

    function clearImagePreview() {
        imagenPreviewDiv.innerHTML = ''; // Limpiar contenido
    }
});

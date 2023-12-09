document.addEventListener('DOMContentLoaded', function () {
    var form = document.querySelector('.formulario');
    var nombreInput = document.getElementById('txtnombre');

    form.addEventListener('submit', function (event) {
        if (!validarNombre()) {
            event.preventDefault(); // Evita que el formulario se envíe si la validación falla
        }
    });

    function validarNombre() {
        var nombre = nombreInput.value.trim();
        var errorSpan = document.getElementById('error-nombre');

        if (nombre === '') {
            errorSpan.innerText = 'El nombre es obligatorio';
            return false;
        } else {
            errorSpan.innerText = '';
            return true;
        }
    }

    nombreInput.addEventListener('input', validarNombre);
});

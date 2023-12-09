document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('.formulario');

    form.addEventListener('submit', function (event) {
        // Limpiar mensajes de error
        clearErrorMessages();

        // Validar campos
        const requiredFields = [
            {id: 'txtnombre', errorId: 'error-nombre', name: 'Nombre'},
            {id: 'txtapellido', errorId: 'error-apellido', name: 'Apellido'},
            {id: 'txtedad', errorId: 'error-edad', name: 'Edad'},
            {id: 'txtdni', errorId: 'error-dni', name: 'DNI'},
            {id: 'rbomasculino', errorId: 'error-sexo', name: 'Sexo'},
            {id: 'rbofemenino', errorId: 'error-sexo', name: 'Sexo'},
            {id: 'txtemail', errorId: 'error-email', name: 'Email'},
            {id: 'txtcontraseña', errorId: 'error-contraseña', name: 'Contraseña'}
            // Agrega más campos según sea necesario
        ];

        let hasErrors = false;

        requiredFields.forEach(field => {
            if (!validateField(field.id, field.errorId, field.name)) {
                hasErrors = true;
            }
        });

        if (hasErrors) {
            event.preventDefault(); // Evitar que se envíe el formulario si hay errores
        }
    });

    function validateField(inputId, errorId, fieldName) {
        const input = document.getElementById(inputId);
        const error = document.getElementById(errorId);

        if (input.type === 'radio') {
            // Para campos de radio, verifica que al menos uno esté seleccionado
            const radioGroup = document.getElementsByName(input.name);
            const isSelected = Array.from(radioGroup).some(radio => radio.checked);

            if (!isSelected) {
                error.innerText = `Seleccione una opción para el campo ${fieldName}.`;
                return false;
            }
        } else if (input.value.trim() === '') {
            error.innerText = `El campo ${fieldName} no puede estar en blanco.`;
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
});

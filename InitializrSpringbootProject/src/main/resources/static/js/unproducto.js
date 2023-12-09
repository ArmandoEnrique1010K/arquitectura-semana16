    document.addEventListener('DOMContentLoaded', function () {
        const fichaTecnicaDiv = document.querySelector('.texto-ficha-tecnica');
        const fichaTecnicaText = fichaTecnicaDiv.innerText;

        // Dividir el texto en líneas
        const lineas = fichaTecnicaText.split('\n');

        // Procesar cada línea
        const contenidoFormateado = lineas.map((linea) => {
            const indexDosPuntos = linea.indexOf(':');

            if (indexDosPuntos !== -1) {
                return `&#8226; <strong>${linea.substring(0, indexDosPuntos + 1)}</strong>${linea.substring(indexDosPuntos + 1)}`;
            } else {
                return `&#8226; <strong>${linea}</strong>`;
            }
        });

        // Unir las líneas formateadas con viñetas
        const contenidoFinal = contenidoFormateado.join('<br>');

        // Asignar el contenido al div
        fichaTecnicaDiv.innerHTML = contenidoFinal;
    });


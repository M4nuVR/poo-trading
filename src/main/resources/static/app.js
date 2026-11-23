let codigoSeleccionado = null;

function formatearPrecio(precio) {
    if (precio === null) {
        return "-";
    }
    return precio.toLocaleString("es-CO", { maximumFractionDigits: 4 });
}

function formatearHora(fecha) {
    return new Date(fecha).toLocaleTimeString("es-CO");
}

async function cargarActivos() {
    const respuesta = await fetch("/api/activos");
    const activos = await respuesta.json();
    const tabla = document.getElementById("tabla-activos");
    tabla.innerHTML = "";
    activos.forEach(activo => {
        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td><strong>${activo.codigo}</strong></td>
            <td>${activo.nombre}</td>
            <td>${activo.tipo}</td>
            <td>${formatearPrecio(activo.ultimoPrecio)}</td>
            <td><span class="estado ${activo.estado}">${activo.estado}</span></td>`;
        fila.addEventListener("click", () => {
            codigoSeleccionado = activo.codigo;
            cargarHistorial();
        });
        tabla.appendChild(fila);
    });
    if (codigoSeleccionado === null && activos.length > 0) {
        codigoSeleccionado = activos[0].codigo;
    }
}

async function cargarHistorial() {
    if (codigoSeleccionado === null) {
        return;
    }
    const respuesta = await fetch(`/api/activos/${codigoSeleccionado}/cotizaciones`);
    const cotizaciones = await respuesta.json();
    document.getElementById("activo-seleccionado").textContent = `(${codigoSeleccionado})`;
    const lista = document.getElementById("lista-historial");
    lista.innerHTML = "";
    cotizaciones.slice().reverse().forEach(cotizacion => {
        const item = document.createElement("li");
        item.textContent = `${formatearHora(cotizacion.fecha)}  ${formatearPrecio(cotizacion.precio)}`;
        lista.appendChild(item);
    });
}

async function cargarSenales() {
    const respuesta = await fetch("/api/senales");
    const senales = await respuesta.json();
    const lista = document.getElementById("lista-senales");
    lista.innerHTML = "";
    if (senales.length === 0) {
        lista.innerHTML = '<li class="vacio">Sin alertas por ahora</li>';
        return;
    }
    senales.forEach(senal => {
        const item = document.createElement("li");
        item.textContent = `${formatearHora(senal.fecha)}  ${senal.tipo} ${senal.codigo}: ${senal.mensaje}`;
        lista.appendChild(item);
    });
}

async function actualizar() {
    try {
        await cargarActivos();
        await cargarHistorial();
        await cargarSenales();
    } catch (error) {
        console.error("No se pudo actualizar el panel", error);
    }
}

actualizar();
setInterval(actualizar, 3000);

const lblTitulo = document.getElementById('lblTitulo');
const badgeEstado = document.getElementById('badgeEstado');
const cmbTipoCuenta = document.getElementById('cmbTipoCuenta');
const btnIniciar = document.getElementById('btnIniciar');
const btnReiniciar = document.getElementById('btnReiniciar');
const formTransaccion = document.getElementById('formTransaccion');
const txtValor = document.getElementById('txtValor');
const cmbMovimiento = document.getElementById('cmbMovimiento');
const btnRegistrar = document.getElementById('btnRegistrar');
const txtSaldo = document.getElementById('txtSaldo');
const infoInteres = document.getElementById('infoInteres');
const tbodyHistorial = document.getElementById('tbodyHistorial');
const tablaContenedor = document.getElementById('tablaContenedor');
const contadorFilas = document.getElementById('contadorFilas');
const alertaBox = document.getElementById('alertaBox');
const alertaTitulo = document.getElementById('alertaTitulo');
const alertaMensaje = document.getElementById('alertaMensaje');
const btnCerrarAlerta = document.getElementById('btnCerrarAlerta');

function mostrarAlerta(titulo, mensaje) {
    alertaTitulo.textContent = titulo;
    alertaMensaje.textContent = mensaje;
    alertaBox.classList.remove('hidden');
}

function ocultarAlerta() {
    alertaBox.classList.add('hidden');
}

btnCerrarAlerta.addEventListener('click', ocultarAlerta);

function actualizarVista(estado) {
    document.title = estado.titulo;
    lblTitulo.textContent = estado.titulo;
    txtSaldo.value = estado.saldo;

    const activo = Boolean(estado.iniciada);
    txtValor.disabled = !activo;
    cmbMovimiento.disabled = !activo;
    btnRegistrar.disabled = !activo;
    cmbTipoCuenta.disabled = activo;
    btnIniciar.disabled = activo;

    if (activo) {
        cmbTipoCuenta.value = estado.tipoCuenta;
        badgeEstado.textContent = estado.tipoCuenta;
        badgeEstado.className = 'badge badge-active';
        btnReiniciar.classList.remove('hidden');
        infoInteres.textContent = estado.tipoCuenta === 'Cuenta de ahorros'
            ? 'Abona 1 % de interés acumulativo sobre el saldo tras cada transacción exitosa.'
            : 'Cuenta corriente estándar (sin cálculo de intereses).';
    } else {
        badgeEstado.textContent = 'Sin iniciar';
        badgeEstado.className = 'badge badge-inactive';
        btnReiniciar.classList.add('hidden');
        infoInteres.textContent = 'Seleccione e inicie una cuenta para comenzar a operar.';
    }

    renderizarHistorial(estado.historial || []);

    if (estado.error && estado.mensaje) {
        const titulo = estado.mensaje.includes('Fondos insuficientes')
            ? 'Transacción rechazada'
            : 'Valor inválido';
        mostrarAlerta(titulo, estado.mensaje);
    } else {
        ocultarAlerta();
    }
}

function renderizarHistorial(historial) {
    contadorFilas.textContent = `${historial.length} ${historial.length === 1 ? 'registro' : 'registros'}`;
    tbodyHistorial.innerHTML = '';

    if (historial.length === 0) {
        const tr = document.createElement('tr');
        tr.className = 'empty-row';
        tr.innerHTML = '<td colspan="5">No hay transacciones registradas.</td>';
        tbodyHistorial.appendChild(tr);
        return;
    }

    // Supuesto 5 (Figura 2 del documento): El historial muestra lo más reciente arriba
    const ordenRecienteArriba = [...historial].reverse();

    ordenRecienteArriba.forEach((fila) => {
        const tr = document.createElement('tr');
        if (!fila.exitosa) {
            tr.className = 'row-rejected';
        }
        tr.innerHTML = `
            <td>${fila.numero}</td>
            <td>${escapeHtml(fila.tipo)}</td>
            <td>${escapeHtml(fila.valorFormateado)}</td>
            <td>${escapeHtml(fila.saldoResultanteFormateado)}</td>
            <td>${escapeHtml(fila.observaciones)}</td>
        `;
        tbodyHistorial.appendChild(tr);
    });

    tablaContenedor.scrollTop = 0;
}

function escapeHtml(texto) {
    const div = document.createElement('div');
    div.textContent = texto ?? '';
    return div.innerHTML;
}

async function cargarEstadoInicial() {
    const res = await fetch('/api/cuenta/estado');
    const data = await res.json();
    actualizarVista(data);
}

btnIniciar.addEventListener('click', async () => {
    const res = await fetch('/api/cuenta/iniciar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ tipoCuenta: cmbTipoCuenta.value })
    });
    const data = await res.json();
    actualizarVista(data);
    txtValor.value = '';
    txtValor.focus();
});

btnReiniciar.addEventListener('click', async () => {
    const res = await fetch('/api/cuenta/reiniciar', { method: 'POST' });
    const data = await res.json();
    actualizarVista(data);
    txtValor.value = '';
});

formTransaccion.addEventListener('submit', async (e) => {
    e.preventDefault();
    const res = await fetch('/api/cuenta/transaccion', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            movimiento: cmbMovimiento.value,
            valor: txtValor.value
        })
    });
    const data = await res.json();
    actualizarVista(data);
    txtValor.value = '';
    txtValor.focus();
});

cargarEstadoInicial();

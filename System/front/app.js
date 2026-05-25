const API_BASE = 'http://localhost:8080/api';

// ============ UTILS ============
function showMessage(text, type) {
    const el = document.getElementById('radicar-message');
    if (!el) return;
    el.textContent = text;
    el.className = 'message show message-' + type;
}

function hideMessage() {
    const el = document.getElementById('radicar-message');
    if (el) el.className = 'message';
}

function setButtonLoading(btn, loading) {
    if (loading) {
        btn.dataset.originalText = btn.innerHTML;
        btn.innerHTML = '<span class="spinner"></span> Procesando...';
        btn.disabled = true;
    } else {
        btn.innerHTML = btn.dataset.originalText || btn.innerHTML;
        btn.disabled = false;
    }
}

async function apiGet(endpoint) {
    const res = await fetch(API_BASE + endpoint);
    if (!res.ok) {
        const data = await res.json().catch(() => ({}));
        throw new Error(data.error || 'Error en el servidor');
    }
    return res.json();
}

async function apiPost(endpoint, body) {
    const res = await fetch(API_BASE + endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
    });
    const data = await res.json();
    if (!res.ok) throw new Error(data.error || 'Error en el servidor');
    return data;
}

// ============ LOAD DATA ============
async function loadSelects() {
    try {
        // Cargar Tipos de ID
        const tiposId = await apiGet('/catalogos/tipos-identificacion');
        const selectId = document.getElementById('tipo-id');
        selectId.innerHTML = '<option value="">Seleccione...</option>';
        tiposId.forEach(t => {
            selectId.innerHTML += `<option value="${t.id}">${t.nombre}</option>`;
        });

        // Cargar Tipos de Radicado
        const tiposRad = await apiGet('/catalogos/tipos-radicado');
        const selectRad = document.getElementById('tipo-radicado');
        selectRad.innerHTML = '<option value="">Seleccione...</option>';
        tiposRad.forEach(t => {
            selectRad.innerHTML += `<option value="${t.id}">${t.nombre}</option>`;
        });
    } catch (err) {
        console.error('Error cargando catálogos:', err);
        showMessage('Error al cargar la información del formulario. Verifique que el servidor esté encendido.', 'error');
    }
}

// ============ SUBMIT FORM ============
async function handleRadicar(e) {
    e.preventDefault();
    hideMessage();
    const btn = e.target.querySelector('button[type="submit"]');

    // Recopilar datos
    const dto = {
        idTipoIdentificacion: parseInt(document.getElementById('tipo-id').value),
        numeroIdentificacion: document.getElementById('numero-id').value.trim(),
        nombresCompletos: document.getElementById('nombres').value.trim(),
        correo: document.getElementById('correo').value.trim(),
        telefonoMovil: document.getElementById('telefono').value.trim() || null,
        idTipoRadicado: parseInt(document.getElementById('tipo-radicado').value),
        comentarios: document.getElementById('comentarios').value.trim()
    };

    setButtonLoading(btn, true);
    try {
        const result = await apiPost('/pqrs/radicar-publico', dto);
        
        // Mostrar resultado
        document.getElementById('radicar-form-container').style.display = 'none';
        document.getElementById('radicar-result').style.display = 'block';
        
        document.getElementById('result-numero').textContent = result.numeroRadicado;
        document.getElementById('result-correo').textContent = dto.correo;
        
        e.target.reset();
    } catch (err) {
        showMessage(err.message, 'error');
    } finally {
        setButtonLoading(btn, false);
    }
}

// ============ RESET FORM ============
function resetForm() {
    document.getElementById('radicar-result').style.display = 'none';
    document.getElementById('radicar-form-container').style.display = 'block';
    hideMessage();
}

// ============ INIT ============
document.addEventListener('DOMContentLoaded', () => {
    loadSelects();
    const form = document.getElementById('radicar-form');
    if (form) form.addEventListener('submit', handleRadicar);
});

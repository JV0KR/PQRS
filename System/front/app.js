// ============================================================
// SuperMarket PQRS - Frontend Application
// ============================================================

const API_BASE = 'http://localhost:8080/api';

// Store current modal PQRS ID
let currentModalPqrsId = null;

// ============================================================
// NAVIGATION
// ============================================================

function showView(viewId) {
  document.querySelectorAll('.view').forEach(v => {
    v.classList.remove('active');
  });
  const target = document.getElementById(viewId);
  if (target) {
    target.classList.add('active');
    // Re-trigger animation
    target.style.animation = 'none';
    target.offsetHeight; // force reflow
    target.style.animation = '';
  }
  hideAllMessages();
}

function navigateTo(viewId) {
  showView(viewId);

  switch (viewId) {
    case 'view-radicar':
      loadSelects();
      break;
    case 'view-cliente':
      loadClienteRadicados();
      break;
    case 'view-gestor':
      loadGestorRadicados();
      break;
    default:
      break;
  }
}

// ============================================================
// SESSION MANAGEMENT
// ============================================================

function getSession() {
  const data = sessionStorage.getItem('pqrs_session');
  return data ? JSON.parse(data) : null;
}

function setSession(data) {
  sessionStorage.setItem('pqrs_session', JSON.stringify(data));
}

function clearSession() {
  sessionStorage.removeItem('pqrs_session');
}

function logout() {
  clearSession();
  showView('view-landing');
}

// ============================================================
// API HELPERS
// ============================================================

async function apiGet(endpoint) {
  const response = await fetch(`${API_BASE}${endpoint}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    }
  });

  if (!response.ok) {
    const errorData = await response.json().catch(() => null);
    throw new Error(errorData?.message || errorData?.error || `Error ${response.status}`);
  }

  return response.json();
}

async function apiPost(endpoint, body) {
  const response = await fetch(`${API_BASE}${endpoint}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(body)
  });

  if (!response.ok) {
    const errorData = await response.json().catch(() => null);
    throw new Error(errorData?.message || errorData?.error || `Error ${response.status}`);
  }

  return response.json();
}

async function apiPut(endpoint, body) {
  const response = await fetch(`${API_BASE}${endpoint}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(body)
  });

  if (!response.ok) {
    const errorData = await response.json().catch(() => null);
    throw new Error(errorData?.message || errorData?.error || `Error ${response.status}`);
  }

  return response.json();
}

// ============================================================
// RADICAR PQRS (PUBLIC)
// ============================================================

async function loadSelects() {
  try {
    // Load tipos de identificacion
    const tiposId = await apiGet('/catalogos/tipos-identificacion');
    const selectTipoId = document.getElementById('tipo-id');
    // Keep the default option
    selectTipoId.innerHTML = '<option value="">Seleccionar...</option>';
    tiposId.forEach(item => {
      const opt = document.createElement('option');
      opt.value = item.id;
      opt.textContent = item.nombre;
      selectTipoId.appendChild(opt);
    });

    // Load tipos de radicado
    const tiposRad = await apiGet('/catalogos/tipos-radicado');
    const selectTipoRad = document.getElementById('tipo-radicado');
    selectTipoRad.innerHTML = '<option value="">Seleccionar...</option>';
    tiposRad.forEach(item => {
      const opt = document.createElement('option');
      opt.value = item.id;
      opt.textContent = item.nombre;
      selectTipoRad.appendChild(opt);
    });
  } catch (err) {
    console.error('Error loading selects:', err);
  }
}

async function handleRadicar(e) {
  e.preventDefault();
  const btn = document.getElementById('btn-radicar');
  setButtonLoading(btn, true);

  const formData = new FormData();
  formData.append('idTipoIdentificacion', document.getElementById('tipo-id').value);
  formData.append('numeroIdentificacion', document.getElementById('numero-id').value.trim());
  formData.append('nombresCompletos', document.getElementById('nombres').value.trim());
  formData.append('correo', document.getElementById('correo').value.trim());
  
  const tel = document.getElementById('telefono').value.trim();
  if (tel) formData.append('telefonoMovil', tel);
  
  formData.append('idTipoRadicado', document.getElementById('tipo-radicado').value);
  formData.append('comentarios', document.getElementById('comentarios').value.trim());
  
  const fileInput = document.getElementById('archivo');
  if (fileInput && fileInput.files.length > 0) {
      formData.append('archivo', fileInput.files[0]);
  }

  try {
    const response = await fetch(API_BASE + '/pqrs/radicar-publico', {
      method: 'POST',
      body: formData
    });
    
    if (!response.ok) {
        const errorData = await response.json().catch(() => null);
        throw new Error(errorData?.message || errorData?.error || `Error ${response.status}`);
    }
    const result = await response.json();
    
    // Fallback payload references for the result screen
    const payload = {
        numeroIdentificacion: document.getElementById('numero-id').value.trim(),
        correo: document.getElementById('correo').value.trim()
    };

    // Show result card
    document.getElementById('radicar-form-wrapper').style.display = 'none';
    document.getElementById('radicar-result').style.display = 'block';
    document.getElementById('result-radicado').textContent = result.numeroRadicado || result.radicado || '-';
    document.getElementById('result-correo').textContent = payload.correo;
    document.getElementById('result-password').textContent = payload.numeroIdentificacion;
  } catch (err) {
    showMessage('radicar-message', err.message, 'error');
  } finally {
    setButtonLoading(btn, false);
  }
}

function resetForm() {
  document.getElementById('form-radicar').reset();
  document.getElementById('radicar-form-wrapper').style.display = 'block';
  document.getElementById('radicar-result').style.display = 'none';
  hideAllMessages();
}

// ============================================================
// LOGIN
// ============================================================

async function handleLogin(e) {
  e.preventDefault();
  const btn = document.getElementById('btn-login');
  setButtonLoading(btn, true);

  const payload = {
    correo: document.getElementById('login-correo').value.trim(),
    password: document.getElementById('login-password').value
  };

  try {
    const result = await apiPost('/auth/login', payload);
    setSession(result);

    if (result.perfil === 'CLIENTE') {
      document.getElementById('cliente-name').textContent = result.nombresCompletos;
      navigateTo('view-cliente');
    } else if (result.perfil === 'GESTOR') {
      document.getElementById('gestor-name').textContent = result.nombresCompletos;
      navigateTo('view-gestor');
    } else {
      showMessage('login-message', 'Perfil no reconocido.', 'error');
    }

    // Clear login form
    document.getElementById('form-login').reset();
  } catch (err) {
    showMessage('login-message', err.message, 'error');
  } finally {
    setButtonLoading(btn, false);
  }
}

// ============================================================
// CLIENTE DASHBOARD
// ============================================================

async function loadClienteRadicados() {
  const session = getSession();
  if (!session) {
    logout();
    return;
  }

  const tbody = document.getElementById('cliente-tbody');
  const emptyState = document.getElementById('cliente-empty');
  const tableContainer = document.getElementById('cliente-table');

  tbody.innerHTML = '<tr><td colspan="5" style="text-align:center; padding:2rem; color:var(--text-muted);">Cargando...</td></tr>';
  emptyState.style.display = 'none';
  tableContainer.style.display = 'block';

  try {
    const radicados = await apiGet(`/pqrs/mis-radicados/${session.id}`);

    if (!radicados || radicados.length === 0) {
      tbody.innerHTML = '';
      tableContainer.style.display = 'none';
      emptyState.style.display = 'block';
      return;
    }

    tbody.innerHTML = radicados.map(r => `
      <tr>
        <td><strong>${escapeHtml(r.numeroRadicado || '')}</strong></td>
        <td>${formatDate(r.fechaRadicado)}</td>
        <td>${escapeHtml(r.tipoRadicado || r.tipo || '')}</td>
        <td><span class="badge ${getBadgeClass(r.estado)}">${escapeHtml(r.estado || '')}</span></td>
        <td class="td-comment" style="color: #6ee7b7;">${escapeHtml(r.justificacionEstado || '-')}</td>
        <td class="td-comment">${escapeHtml(truncate(r.comentarios || '', 80))}</td>
      </tr>
    `).join('');
  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="5" style="text-align:center; padding:2rem; color:#f87171;">${escapeHtml(err.message)}</td></tr>`;
  }
}

// ============================================================
// GESTOR DASHBOARD
// ============================================================

async function loadGestorRadicados() {
  const session = getSession();
  if (!session) {
    logout();
    return;
  }

  const tbody = document.getElementById('gestor-tbody');
  const emptyState = document.getElementById('gestor-empty');
  const tableContainer = document.getElementById('gestor-table');

  tbody.innerHTML = '<tr><td colspan="6" style="text-align:center; padding:2rem; color:var(--text-muted);">Cargando...</td></tr>';
  emptyState.style.display = 'none';
  tableContainer.style.display = 'block';

  try {
    const radicados = await apiGet('/gestor/radicados');

    if (!radicados || radicados.length === 0) {
      tbody.innerHTML = '';
      tableContainer.style.display = 'none';
      emptyState.style.display = 'block';
      return;
    }

    tbody.innerHTML = radicados.map(r => `
      <tr>
        <td><strong>${escapeHtml(r.numeroRadicado || '')}</strong></td>
        <td>${formatDate(r.fechaRadicado)}</td>
        <td>${escapeHtml(r.tipoRadicado || r.tipo || '')}</td>
        <td><span class="badge ${getBadgeClass(r.estado)}">${escapeHtml(r.estado || '')}</span></td>
        <td>${escapeHtml(r.nombreCliente || '')}</td>
        <td style="display: flex; gap: 8px; flex-wrap: wrap;">
          <button class="btn btn-secondary btn-sm" onclick="openModalEstado(${r.id}, '${escapeHtml(r.numeroRadicado || '')}')">
            &#9998; Estado
          </button>
          ${r.tieneAnexo ? `<button class="btn btn-primary btn-sm" onclick="descargarAnexo(${r.id})">&#128206; Evidencia</button>` : ''}
        </td>
      </tr>
    `).join('');
  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="6" style="text-align:center; padding:2rem; color:#f87171;">${escapeHtml(err.message)}</td></tr>`;
  }
}

async function loadEstados() {
  try {
    const estados = await apiGet('/gestor/estados');
    const select = document.getElementById('modal-nuevo-estado');
    select.innerHTML = '<option value="">Seleccionar estado...</option>';
    estados.forEach(e => {
      const opt = document.createElement('option');
      opt.value = e.id;
      opt.textContent = e.nombre;
      select.appendChild(opt);
    });
  } catch (err) {
    console.error('Error loading estados:', err);
  }
}

function openModalEstado(idPqrs, numeroRadicado) {
  currentModalPqrsId = idPqrs;
  document.getElementById('modal-radicado').textContent = numeroRadicado;
  document.getElementById('modal-justificacion').value = '';
  document.getElementById('modal-nuevo-estado').value = '';
  hideMessage('modal-message');
  loadEstados();

  const modal = document.getElementById('modal-estado');
  modal.classList.add('show');
}

function closeModal() {
  const modal = document.getElementById('modal-estado');
  modal.classList.remove('show');
  currentModalPqrsId = null;
}

function handleModalOverlayClick(e) {
  if (e.target === e.currentTarget) {
    closeModal();
  }
}

async function handleCambiarEstado() {
  const idEstado = document.getElementById('modal-nuevo-estado').value;
  const justificacion = document.getElementById('modal-justificacion').value.trim();

  if (!idEstado) {
    showMessage('modal-message', 'Selecciona un estado.', 'error');
    return;
  }

  if (!justificacion) {
    showMessage('modal-message', 'Ingresa una justificacion.', 'error');
    return;
  }

  const btn = document.getElementById('btn-cambiar-estado');
  setButtonLoading(btn, true);

  try {
    await apiPut('/gestor/cambiar-estado', {
      idPqrs: currentModalPqrsId,
      idEstado: parseInt(idEstado),
      justificacion: justificacion
    });

    showMessage('modal-message', 'Estado actualizado correctamente.', 'success');

    setTimeout(() => {
      closeModal();
      loadGestorRadicados();
    }, 1200);
  } catch (err) {
    showMessage('modal-message', err.message, 'error');
  } finally {
    setButtonLoading(btn, false);
  }
}

function descargarAnexo(idPqrs) {
  window.open(API_BASE + '/gestor/descargar-anexo/' + idPqrs, '_blank');
}

function descargarPdf() {
  window.open(API_BASE + '/gestor/reporte-pdf', '_blank');
}

// ============================================================
// UTILITIES
// ============================================================

function showMessage(elementId, text, type) {
  const el = document.getElementById(elementId);
  if (!el) return;
  el.className = `message show message-${type}`;
  el.textContent = text;
}

function hideMessage(elementId) {
  const el = document.getElementById(elementId);
  if (!el) return;
  el.className = 'message';
  el.textContent = '';
}

function hideAllMessages() {
  document.querySelectorAll('.message').forEach(el => {
    el.className = 'message';
    el.textContent = '';
  });
}

function setButtonLoading(btn, loading) {
  if (!btn) return;
  if (loading) {
    btn.disabled = true;
    btn._originalHTML = btn.innerHTML;
    btn.innerHTML = '<span class="spinner"></span> Procesando...';
  } else {
    btn.disabled = false;
    if (btn._originalHTML) {
      btn.innerHTML = btn._originalHTML;
    }
  }
}

function formatDate(dateStr) {
  if (!dateStr) return '-';
  try {
    const d = new Date(dateStr);
    return d.toLocaleDateString('es-CO', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  } catch {
    return dateStr;
  }
}

function truncate(str, len) {
  if (!str) return '';
  if (str.length <= len) return str;
  return str.substring(0, len) + '...';
}

function escapeHtml(str) {
  if (!str) return '';
  const div = document.createElement('div');
  div.appendChild(document.createTextNode(str));
  return div.innerHTML;
}

function getBadgeClass(estado) {
  if (!estado) return '';
  const lower = estado.toLowerCase();
  if (lower.includes('nuevo') || lower.includes('nueva')) return 'badge-nuevo';
  if (lower.includes('proceso') || lower.includes('progreso')) return 'badge-proceso';
  if (lower.includes('resuelto') || lower.includes('resuelta') || lower.includes('cerrado')) return 'badge-resuelto';
  if (lower.includes('rechazado') || lower.includes('rechazada')) return 'badge-rechazado';
  return 'badge-nuevo';
}

// ============================================================
// INIT
// ============================================================

document.addEventListener('DOMContentLoaded', () => {
  // Bind forms
  const formRadicar = document.getElementById('form-radicar');
  if (formRadicar) {
    formRadicar.addEventListener('submit', handleRadicar);
    // Remove inline onsubmit to avoid double binding
    formRadicar.removeAttribute('onsubmit');
  }

  const formLogin = document.getElementById('form-login');
  if (formLogin) {
    formLogin.addEventListener('submit', handleLogin);
    formLogin.removeAttribute('onsubmit');
  }

  // Check existing session
  const session = getSession();
  if (session) {
    if (session.perfil === 'CLIENTE') {
      document.getElementById('cliente-name').textContent = session.nombresCompletos;
      navigateTo('view-cliente');
    } else if (session.perfil === 'GESTOR') {
      document.getElementById('gestor-name').textContent = session.nombresCompletos;
      navigateTo('view-gestor');
    } else {
      showView('view-landing');
    }
  } else {
    showView('view-landing');
  }
});

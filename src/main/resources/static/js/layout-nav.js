/* layout-nav.js
   - Sidebar toggle (móvil)
   - Overlay
   - Dropdown de usuario
   - Marcar link activo
   - Cargar vistas por AJAX (fragments Thymeleaf) sin recargar el layout
*/

(function () {
  const ENABLE_AJAX_NAV = true; // pon en false si NO quieres navegación AJAX

  const sidebar = document.getElementById('sidebar');
  const sidebarToggle = document.getElementById('sidebarToggle');
  const sidebarOverlay = document.getElementById('sidebarOverlay');
  const userDropdownToggle = document.getElementById('userDropdownToggle');
  const userDropdownMenu = document.getElementById('userDropdownMenu');
  const sidebarLinksWrapper = document.getElementById('sidebarLinks');
  const contentArea = document.getElementById('contentArea');

  // --- Sidebar (móvil)
  function toggleSidebar(forceState) {
    if (!sidebar) return;
    const willClose = (typeof forceState === 'boolean')
      ? !forceState // si forceState = true => queremos open; willClose = false
      : sidebar.classList.contains('closed') === false;

    if (willClose) {
      sidebar.classList.add('closed');
      sidebarOverlay?.classList.add('hidden');
      document.body.style.overflow = '';
    } else {
      sidebar.classList.remove('closed');
      sidebarOverlay?.classList.remove('hidden');
      document.body.style.overflow = 'hidden';
    }
  }

  sidebarToggle?.addEventListener('click', () => toggleSidebar());
  sidebarOverlay?.addEventListener('click', () => toggleSidebar(false));

  // Ajuste inicial responsive
  function initializeSidebar() {
    if (!sidebar) return;
    if (window.innerWidth < 768) {
      sidebar.classList.add('closed');
      sidebarOverlay?.classList.add('hidden');
      document.body.style.overflow = '';
    } else {
      sidebar.classList.remove('closed');
      sidebarOverlay?.classList.add('hidden');
      document.body.style.overflow = '';
    }
  }
  window.addEventListener('resize', initializeSidebar);
  initializeSidebar();

  // --- Dropdown usuario
  if (userDropdownToggle && userDropdownMenu) {
    userDropdownToggle.addEventListener('click', (e) => {
      e.stopPropagation();
      userDropdownMenu.classList.toggle('hidden');
    });
    document.addEventListener('click', (e) => {
      if (!userDropdownToggle.contains(e.target) && !userDropdownMenu.contains(e.target)) {
        userDropdownMenu.classList.add('hidden');
      }
    });
  }

  // --- Marcar link activo en sidebar
  function setActiveLink(href) {
    if (!sidebarLinksWrapper) return;
    const links = sidebarLinksWrapper.querySelectorAll('a[href]');
    links.forEach(a => {
      const same = a.getAttribute('href') === href;
      a.classList.toggle('nav-active', same);
    });
  }
  // activo inicial
  setActiveLink(window.location.pathname);

  // --- Cargar fragmento dentro del layout (AJAX)
  async function loadIntoLayout(url, title, push = true) {
    if (!ENABLE_AJAX_NAV || !contentArea) { // fallback a navegación normal
      window.location.href = url;
      return;
    }
    try {
      const res = await fetch(url, { headers: { 'X-Requested-With': 'XMLHttpRequest' } });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const html = await res.text();
      contentArea.innerHTML = html;
      document.title = title ? `${title} - Sistema de Gestión` : document.title;
      setActiveLink(url);

      if (push) {
        try { history.pushState({ href: url, title }, title || document.title, url); } catch {}
      }
      // Cerrar sidebar en móvil
      if (window.innerWidth < 768) toggleSidebar(false);

      // Si el fragmento necesita inicialización (dashboard, etc.)
      if (typeof window.onContentLoaded === 'function') {
        window.onContentLoaded(url);
      }
    } catch (e) {
      // Si falla (CSP, auth, etc), navega normal
      window.location.href = url;
    }
  }

  // --- Interceptar clics del sidebar
  sidebarLinksWrapper?.addEventListener('click', (e) => {
    const a = e.target.closest('a[href]');
    if (!a) return;
    const href = a.getAttribute('href');
    const title = a.dataset.title || a.textContent.trim();
    if (!ENABLE_AJAX_NAV) return; // deja que navegue normal
    e.preventDefault();
    loadIntoLayout(href, title, true);
  });

  // --- Historial (atrás/adelante)
  window.addEventListener('popstate', (event) => {
    const state = event.state;
    if (state?.href) {
      loadIntoLayout(state.href, state.title, false);
    } else {
      // Sin estado: marca activo por URL actual
      setActiveLink(window.location.pathname);
    }
  });
})();

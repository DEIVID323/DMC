document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('loginForm');

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const fd = new FormData(form);

    const res = await fetch(form.action, {
      method: 'POST',
      body: fd,                   // form-data, NO JSON
      credentials: 'include',
      headers: { 'X-CSRF-TOKEN': getCookie('XSRF-TOKEN') } // coincide con CookieCsrfTokenRepository
    });

    if (res.redirected) {
      window.location.href = res.url;   // éxito → dashboard  |  fallo → /login?error=true
      return;
    }

    // Si no fue 302, probablemente devolvió el HTML del login (p.ej. con error)
    const html = await res.text();
    document.open(); document.write(html); document.close();
  });

  function getCookie(name) {
    return document.cookie.split('; ').find(c => c.startsWith(name + '='))?.split('=')[1] || '';
  }

  // Animaciones
  const container = document.querySelector('.container');
  document.querySelector('.SignUpLink')?.addEventListener('click', e => { e.preventDefault(); container?.classList.add('active'); });
  document.querySelector('.SignInLink')?.addEventListener('click', e => { e.preventDefault(); container?.classList.remove('active'); });
});

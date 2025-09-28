document.addEventListener('DOMContentLoaded', () => {
  const sidebarLinksWrapper = document.getElementById('sidebarLinks');
  function setActiveLinkByPath(pathname) {
    if (!sidebarLinksWrapper) return;
    const links = sidebarLinksWrapper.querySelectorAll('a[href]');
    links.forEach(a => a.classList.toggle('nav-active', a.getAttribute('href') === pathname));
  }
  setActiveLinkByPath(window.location.pathname);
});

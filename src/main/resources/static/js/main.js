document.addEventListener('DOMContentLoaded', function() {
   // Funcionalidad del botón de búsqueda
   const searchButton = document.getElementById('searchButton');
   const searchInput = document.getElementById('searchInput');
   
   if (searchButton && searchInput) {
       searchButton.addEventListener('click', function () {
           searchInput.classList.toggle('open');
           searchInput.focus();
       });
   }

   // Funcionalidad del menú hamburguesa
   const menuToggle = document.querySelector('.menu-toggle');
   const navMenu = document.querySelector('.nav-menu');

   if (menuToggle && navMenu) {
       menuToggle.addEventListener('click', function() {
           navMenu.classList.toggle('active');
           menuToggle.classList.toggle('active');
           console.log('Menú hamburguesa clickeado');
       });
   }
   
   // Cerrar el menú cuando se hace clic en un enlace
   const navLinks = document.querySelectorAll('.nav-menu li a');
   navLinks.forEach(link => {
       link.addEventListener('click', function() {
           navMenu.classList.remove('active');
           menuToggle.classList.remove('active');
       });
   });
   
   // Cerrar el menú cuando se hace clic fuera de él
   document.addEventListener('click', function(event) {
       if (!navMenu.contains(event.target) && !menuToggle.contains(event.target) && navMenu.classList.contains('active')) {
           navMenu.classList.remove('active');
           menuToggle.classList.remove('active');
       }
   });
});


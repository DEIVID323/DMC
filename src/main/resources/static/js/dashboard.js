document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("jwtToken");

    if (!token) {
        Swal.fire({
            icon: 'warning',
            title: 'No autenticado',
            text: '⚠️ No estás autenticado. Inicia sesión primero.',
            confirmButtonText: 'Ir a Login',
            confirmButtonColor: '#dc2626'
        }).then(() => {
            window.location.href = "/login";
        });
        return;
    }

    console.log("✅ Usuario autenticado con token:", token);

    // Elementos del DOM
    const sidebar = document.getElementById("sidebar");
    const mainContent = document.querySelector(".main-content");
    const sidebarToggle = document.getElementById("sidebarToggle");
    const sidebarOverlay = document.getElementById("sidebarOverlay");
    const userDropdownToggle = document.getElementById("userDropdownToggle");
    const userDropdownMenu = document.getElementById("userDropdownMenu");
    const btnVerVentas = document.getElementById("btnVerVentas");
    const ventaModal = document.getElementById("ventaModal");
    const closeVentaModal = document.getElementById("closeVentaModal");
    const ventaModalContent = document.getElementById("ventaModalContent");
    const totalVentas = document.getElementById("totalVentas");
    const ventaDetalles = document.getElementById("ventaDetalles");
    let sidebarOpen = false;

    // Validar elementos del DOM
    if (!totalVentas || !btnVerVentas || !ventaModal || !closeVentaModal || !ventaModalContent || !ventaDetalles) {
        console.error("⚠️ Algunos elementos del DOM no se encontraron:", {
            totalVentas, btnVerVentas, ventaModal, closeVentaModal, ventaModalContent, ventaDetalles
        });
        return;
    }

    // Función para toggle del sidebar
    function toggleSidebar() {
        sidebarOpen = !sidebarOpen;
        if (sidebarOpen) {
            sidebar.classList.remove("closed", "-translate-x-full");
            sidebar.classList.add("translate-x-0");
            sidebarOverlay.classList.remove("hidden");
            document.body.style.overflow = "hidden";
        } else {
            sidebar.classList.add("closed", "-translate-x-full");
            sidebar.classList.remove("translate-x-0");
            sidebarOverlay.classList.add("hidden");
            document.body.style.overflow = "";
        }

        if (window.innerWidth >= 768) {
            if (sidebarOpen) {
                mainContent.classList.add("md:ml-64");
            } else {
                mainContent.classList.remove("md:ml-64");
            }
        }
    }

    // Event listeners
    sidebarToggle.addEventListener("click", toggleSidebar);
    sidebarOverlay.addEventListener("click", toggleSidebar);

    // Dropdown de usuario
    userDropdownToggle.addEventListener("click", (e) => {
        e.stopPropagation();
        userDropdownMenu.classList.toggle("hidden");
    });

    document.addEventListener("click", () => {
        userDropdownMenu.classList.add("hidden");
    });

    // Modal de ventas
    closeVentaModal.addEventListener("click", () => {
        ventaModal.classList.add("hidden");
        ventaModalContent.classList.remove("scale-100");
        ventaModalContent.classList.add("scale-95");
        document.body.style.overflow = "";
    });

    ventaModal.addEventListener("click", (e) => {
        if (e.target === ventaModal) {
            ventaModal.classList.add("hidden");
            ventaModalContent.classList.remove("scale-100");
            ventaModalContent.classList.add("scale-95");
            document.body.style.overflow = "";
        }
    });

    // Inicializar sidebar
    function initializeSidebar() {
        if (window.innerWidth < 768) {
            sidebarOpen = false;
            sidebar.classList.add("closed", "-translate-x-full");
            sidebarOverlay.classList.add("hidden");
        } else {
            sidebarOpen = true;
            sidebar.classList.remove("closed", "-translate-x-full");
            sidebar.classList.add("translate-x-0");
            mainContent.classList.add("md:ml-64");
        }
    }

    window.addEventListener("resize", initializeSidebar);
    initializeSidebar();

    // Decodificar token
    function parseJwt(token) {
        try {
            const base64Url = token.split(".")[1];
            const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
            return JSON.parse(atob(base64));
        } catch {
            return null;
        }
    }

    const payload = parseJwt(token);
    const nombreUsuario = payload?.sub || "Usuario";
    document.getElementById("usuarioNombre").innerText = nombreUsuario;
    document.getElementById("usuarioNombreBienvenida").innerText = nombreUsuario;

    // Función para obtener todas las ventas
    async function getVentas() {
        try {
            const response = await fetch("http://localhost:8080/api/ventas", {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json"
                }
            });

            const ventas = await response.json(); // Simplified to match working behavior
            console.log("📦 Ventas obtenidas:", ventas);

            // Mostrar el total de ventas
            totalVentas.innerText = ventas.length;

            // Sumar el total de las ventas
            const sumaVentas = ventas.reduce((acc, v) => acc + (v.totalVenta || 0), 0);
            console.log("💰 Suma total de ventas:", sumaVentas);

            return ventas;
        } catch (error) {
            console.error("⚠️ Error al obtener ventas:", error);
            return [];
        }
    }

    // Función para llenar la tabla del modal
    function llenarTablaVentas(ventas) {
        ventaDetalles.innerHTML = ""; // Limpiar antes

        let html = "<div class='overflow-x-auto'><table class='min-w-full divide-y divide-gray-200'>";
        html += "<thead class='bg-gray-50'><tr>";
        html += "<th class='px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider'>ID</th>";
        html += "<th class='px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider'>Cliente</th>";
        html += "<th class='px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider'>Usuario</th>";
        html += "<th class='px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider'>Fecha</th>";
        html += "<th class='px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider'>Total</th>";
        html += "<th class='px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider'>Pago</th>";
        html += "<th class='px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider'>Estado</th>";
        html += "</tr></thead><tbody class='bg-primary divide-y divide-gray-200'>";

        ventas.forEach((v, index) => {
            const rowClass = index % 2 === 0 ? "bg-primary" : "bg-gray-50";
            html += `
                <tr class="${rowClass}">
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-accent">${v.id || v.idVenta}</td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-accent">${v.idCliente ?? "N/A"}</td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-accent">${v.idUsuario}</td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-accent">${new Date(v.fechaVenta).toLocaleString()}</td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-semibold text-secondary">$${v.totalVenta?.toLocaleString() || 0}</td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-accent">${v.metodoPago}</td>
                    <td class="px-6 py-4 whitespace-nowrap">
                        <span class="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-secondary text-primary">
                            ${v.estado}
                        </span>
                    </td>
                </tr>
            `;
        });

        html += "</tbody></table></div>";
        ventaDetalles.innerHTML = html;
    }

    // Evento click en "Ver Detalles"
    btnVerVentas.addEventListener("click", async (e) => {
        e.preventDefault(); // evitar navegación
        const ventas = await getVentas();
        llenarTablaVentas(ventas);

        // Mostrar modal con Tailwind
        ventaModal.classList.remove("hidden");
        ventaModalContent.classList.remove("scale-95");
        ventaModalContent.classList.add("scale-100");
        document.body.style.overflow = "hidden";
    });

    // Ejecutamos carga inicial
    getVentas();

    // Inicializar gráfico (ejemplo, ajusta con datos reales)
    const ctx = document.getElementById('graficoVentas').getContext('2d');
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['Día 1', 'Día 2', 'Día 3', 'Día 4', 'Día 5'],
            datasets: [{
                label: 'Ventas',
                data: [12, 19, 3, 5, 2], // Reemplaza con datos reales
                backgroundColor: 'rgba(220, 38, 38, 0.2)', // Red with opacity
                borderColor: '#dc2626', // Solid red
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
});
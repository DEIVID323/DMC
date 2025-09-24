// 👉 función para guardar el token en localStorage
function saveJwtToken(token) {
    if (token) {
        localStorage.setItem('jwtToken', token);
        console.log('✅ JWT guardado en localStorage');
    } else {
        console.warn('⚠️ No se recibió un token válido para guardar');
    }
}

document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const data = {
        username: formData.get('username'),
        password: formData.get('password')
    };
    console.log('Datos enviados:', JSON.stringify(data));

    try {
        const response = await fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            const result = await response.json(); // ✅ backend responde { jwt: "..." }
            console.log('Respuesta del servidor:', result);

            saveJwtToken(result.jwt); // 🔐 guardamos el token con la función
            window.location.href = '/dashboard'; // redirige a dashboard
        } else {
            const errorText = await response.text();
            alert('Error: ' + (errorText || 'Problema con el servidor. Código: ' + response.status));
        }
    } catch (error) {
        console.error('Error de conexión:', error);
        alert('Error de conexión: ' + (error.message || 'Verifica el servidor.'));
    }
});

    const container = document.querySelector('.container');
    const LoginLink = document.querySelector('.SignInLink');
    const RegisterLink = document.querySelector('.SignUpLink');

    RegisterLink.addEventListener('click', () => {
        container.classList.add('active');
    });

    LoginLink.addEventListener('click', () => {
        container.classList.remove('active');
    });
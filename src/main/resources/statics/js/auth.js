// auth.js

document.addEventListener("DOMContentLoaded", function() {
    // Função para realizar o login
    async function login(username, password) {
        try {
            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username, password })
            });

            if (response.ok) {
                const data = await response.json();
                localStorage.setItem('token', data.token);
                alert("Login realizado com sucesso!");
            } else {
                alert("Erro no login. Verifique suas credenciais.");
            }
        } catch (error) {
            console.error("Erro ao realizar o login:", error);
        }
    }

    // Função para realizar logout
    function logout() {
        localStorage.removeItem('token');
        alert("Logout realizado com sucesso!");
    }

    // Exemplo de uso
    document.getElementById("loginButton").addEventListener("click", function() {
        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;
        login(username, password);
    });

    document.getElementById("logoutButton").addEventListener("click", logout);
});

// agenda.js

document.addEventListener("DOMContentLoaded", function() {
    // Função para carregar agendamentos
    async function loadAgenda() {
        try {
            const response = await fetch('/api/agenda');
            const agenda = await response.json();
            displayAgenda(agenda);
        } catch (error) {
            console.error("Erro ao carregar a agenda:", error);
        }
    }

    // Função para exibir a agenda
    function displayAgenda(agenda) {
        const agendaContainer = document.getElementById("agendaContainer");
        agendaContainer.innerHTML = "";

        agenda.forEach(item => {
            const div = document.createElement("div");
            div.className = "agenda-item";
            div.innerHTML = `
                <h3>${item.title}</h3>
                <p>${item.description}</p>
                <p><strong>Data:</strong> ${item.date}</p>
            `;
            agendaContainer.appendChild(div);
        });
    }

    loadAgenda();
});

// aluno.js

document.addEventListener("DOMContentLoaded", function() {
    // Função para carregar lista de alunos
    async function loadAlunos() {
        try {
            const response = await fetch('/api/alunos');
            const alunos = await response.json();
            displayAlunos(alunos);
        } catch (error) {
            console.error("Erro ao carregar os alunos:", error);
        }
    }

    // Função para exibir os alunos
    function displayAlunos(alunos) {
        const alunosContainer = document.getElementById("alunosContainer");
        alunosContainer.innerHTML = "";

        alunos.forEach(aluno => {
            const div = document.createElement("div");
            div.className = "aluno-item";
            div.innerHTML = `
                <h3>${aluno.nome}</h3>
                <p><strong>Email:</strong> ${aluno.email}</p>
                <p><strong>Idade:</strong> ${aluno.idade}</p>
            `;
            alunosContainer.appendChild(div);
        });
    }

    loadAlunos();
});

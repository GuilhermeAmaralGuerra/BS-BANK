async function logar() {
    const dadosForm = {
        email: document.getElementById('email').value,
        senha: document.getElementById('senha').value
    };

    try {
        const resposta = await fetch("http://localhost:8080/logar", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(dadosForm)
        });

        if (resposta.ok) {
            window.location.href = "/principal";
        } else {
            const errorMsg = await resposta.text();
            console.log(errorMsg);
        }
        
    } catch (error) {
        console.error("Erro ao enviar dados:", error);
    }
}

document.getElementById('login').addEventListener("click", logar);
document.addEventListener("DOMContentLoaded", () => {
    pegarNome();
    pegarSaldo();
    pegarJurosEValor();
});


async function pegarNome() {
    try {
        const resposta = await fetch("http://localhost:8080/pegar_nome")
        const dados = await resposta.text(); 
        colocarNome(dados); 
    } catch (error) {
        console.error("Erro ao buscar nome: ", error);
    }
}

function colocarNome(dados) {
    let nome = document.getElementById('nome')
    nome.append(dados);
}

async function pegarSaldo(){
    try {
        const resposta = await fetch("http://localhost:8080/pegar_saldo")
        const dados = await resposta.text();
        colocarSaldo(dados);
    } catch (error) {
        console.error("Erro ao buscar saldo: ", error)
    }
}

function colocarSaldo(dados) {
    let saldo = document.getElementById('saldo')
    saldo.textContent = dados;
}

async function pegarJurosEValor() {
    try{
        const resposta = await fetch("http://localhost:8080/pegar_jurosEValor")
        const dados = await resposta.json();
        colocarJurosEValor(dados)

    } catch (error){
        console.error("Erro ao buscar saldo: ", error)
    }
}

function colocarJurosEValor(dados) {
    let valor = document.getElementById('valorTotal')
    let juros = document.getElementById('juros')

    valor.textContent = dados[0].toFixed(2); 
    juros.textContent = dados[1].toFixed(2);
}

async function enviarTransferencia(){
    const dadosForm = {
        codigo: document.getElementById('codigo').value,
        valor: document.getElementById('valorTransferir').value,
        descricao: document.getElementById('descricaoTransferir').value
    }

    if (dadosForm.codigo === "") {
        alert("Não é possível realizar uma transferência sem o código!")
        return;
    } else if(dadosForm.valor === ""){
        alert("Não é possível realizar uma transferência sem o valor!")
        return;
    }

    try {
        const resposta = await fetch("http://localhost:8080/save_transferir", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(dadosForm)
        });

        if (!resposta.ok) {
            const errorMsg = await resposta.text();
            throw new Error(errorMsg);
        }else{
            alert("Transferência feita com sucesso")
            window.location.reload();
        }
        
    } catch (error) {
        console.error("Erro ao enviar dados:", error);

        if (error.message.includes("Destinatário não encontrado")) {
            alert("Erro: Destinatário não encontrado");
        } else if (error.message.includes("Você está transferindo para si mesmo, verifique o código de transferência!")) {
            alert("ERRO: Você está transferindo para si mesmo, verifique o código de transferência!");
        } else {
            alert("Ocorreu um erro na transferência. Verifique o console para mais detalhes.");
        }
    }
}

document.getElementById('enviarTransferir').addEventListener('click', enviarTransferencia)

async function enviarSaqueDepostio(){

    let saque = document.getElementById('saque')
    let deposito = document.getElementById('deposito')
    let escolha = "";

    if (saque.checked) {
        escolha = saque.value;
    } else if (deposito.checked) {
        escolha = deposito.value;
    } else {
        alert("Nenhuma opção selecionada");
        return;
    }

    const dadosForm = {
        valor: document.getElementById('valor').value,
        tipo: escolha
    }

    if (dadosForm.valor === "") {
        alert("Não é possível realizar uma operação sem valor!");
        return;
    } else if (dadosForm.tipo === "") { 
        alert("Não é possível realizar uma operação sem escolher o tipo!");
        return;
    }

    try {
        const resposta = await fetch("http://localhost:8080/save_operacao", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(dadosForm)
        });
        if (!resposta.ok) {
            const errorMsg = await resposta.text()
            throw new Error(errorMsg)
        } else{
            alert("Operação feita com sucesso!")
            window.location.reload();
        }
    } catch (error) {
        console.error("Erro ao enviar dados: " + error)
        if (error.message.includes("O valor do saque é superior ao seu saldo!")) {
            alert("O valor do saque é superior ao seu saldo!")
            return;
        }
    }
}

document.getElementById('enviarSaqueDeposito').addEventListener('click', enviarSaqueDepostio)

async function enviarEmprestimo(){

    let valorSelecionado = document.querySelector('input[name="valor"]:checked');
    let valorEmprestimo = "";

    if (valorSelecionado) {
        valorEmprestimo = valorSelecionado.value
    } else {
        alert("Por favor, selecione um valor.");
    }

    try {
        const dadosForm = {
            valor: valorEmprestimo,
            motivo: document.getElementById('motivo').value
        }

        const resposta = await fetch("http://localhost:8080/enviar_emprestimo",{
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(dadosForm)
        })

        if (!resposta.ok) {
            const errorMsg = await resposta.text()
            throw new Error(errorMsg)
        } else{
            alert("Empréstimo feito com sucesso!")
            window.location.reload();
        }
    } catch (error) {
        console.error("Erro ao enviar dados: " + error)

        if (error.message.includes("A conta já possui um empréstimo pendente e não pode fazer outro.")) {
            alert("A conta já possui um empréstimo pendente e não pode fazer outro.")
        } else if (error.message.includes("A conta possui um empréstimo pago parcialmente, pague-o totalmente antes de fazer outro.")) {
            alert("A conta possui um empréstimo pago parcialmente, pague-o totalmente antes de fazer outro.");
        }
    }

}

document.getElementById('enviarEmprestimo').addEventListener('click', enviarEmprestimo);

async function pagarEmprestimo() {

    const dadosForm = {
        valorPago: document.getElementById('valorPagamento').value
    }

    try {
        const resposta = await fetch("http://localhost:8080/pagar_emprestimo", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(dadosForm)
        })

        if (!resposta.ok) {
            const errorMsg = await resposta.text()
            throw new Error(errorMsg)
        } else{
            alert("Pagamento feito com sucesso!")
            window.location.reload();
        }

    } catch (error) {
        console.error("Erro ao enviar dados: " + error)

        if (error.message.includes("O valor do pagamento não pode ser maior que o valor total do empréstimo!")) {
            alert("O valor do pagamento não pode ser maior que o valor total do empréstimo!");
        }
    }
}

document.getElementById('enviarPagarEmprestimo').addEventListener('click', pagarEmprestimo)

async function deslogar() {
    try {
        const resposta = await fetch("http://localhost:8080/deslogar", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
        });
        if (resposta.ok) {
            window.location.href = "/login";
        } else{
            const errorMsg = await resposta.text();
            throw new Error(errorMsg);
        }
    } catch (error) {
        console.log("ERRO: " + error);
    }
}

document.getElementById('deslogar').addEventListener('click', deslogar)
document.addEventListener('DOMContentLoaded', () => {
    pegarInformações();
});

function visibilidadeHeader() {
    const header = document.getElementById('header');

    header.classList.toggle("visibilidadeHeader");

}

document.getElementById('button-header').addEventListener('click', visibilidadeHeader);

async function pegarInformações(params) {
    
    try {
        const resposta = await fetch("http://localhost:8080/pegar_informacoes")
        const dados = await resposta.json();
        if (!resposta.ok) {
            console.log("Erro na requisição!")
        }
        editarInformacoes(dados);
    } catch (error) {
        console.error("Erro ao buscar saldo: ", error)
    }
    
    
}

function editarInformacoes(dados) {

    let nome = document.getElementById('nome')
    nome.setAttribute('placeholder', dados.nome);

    let nomeCompleto = document.getElementById('nomeCompleto')
    nomeCompleto.setAttribute('placeholder', dados.nomeCompleto)

    let email = document.getElementById('email')
    email.setAttribute('placeholder', dados.email);

    let cpf = document.getElementById('cpf')
    cpf.setAttribute('placeholder', dados.cpf);

    let cep = document.getElementById('cep')
    cep.setAttribute('placeholder', dados.cep);

    let dataNascimento = document.getElementById('dataNascimento')
    dataNascimento.setAttribute('placeholder', dados.dataNascimento);

    dados.senha = "*****";

    window.dadosUsuario = dados;
}
                                
async function enviarNovasInformacoes() {

    if (window.dadosUsuario == null) {
        console.log("Erro ao carregar dados");
        return;
    }

    const dadosForm = {
        nome: document.getElementById('nome').value || dadosUsuario.nome,
        nomeCompleto: document.getElementById('nomeCompleto').value || dadosUsuario.nomeCompleto,
        email: document.getElementById('email').value || dadosUsuario.email,
        cpf: document.getElementById('cpf').value || dadosUsuario.cpf,
        cep: document.getElementById('cep').value || dadosUsuario.cep,
        dataNascimento: document.getElementById('dataNascimento').value || dadosUsuario.dataNascimento
    }

    console.log(dadosForm)

    try {
        const resposta = await fetch("http://localhost:8080/atualizar_informacoes",{
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(dadosForm)
        })

        if(!resposta.ok){
            const errorMsg = await resposta.text()
            throw new Error(errorMsg)
        } else{
            alert("Dados atualizados com sucesso!")
            window.location.reload()
        }
    } catch (error) {
        console.error("Erro ao enviar dados: " + error)
    }
}

document.getElementById('enviarInformacoes').addEventListener('click', enviarNovasInformacoes);

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

const senhaBtn = document.getElementById('trocarSenha')
const popUpTrocarSenha = document.getElementById('popUpTrocarSenha')
const fecharTrocarSenha = document.getElementById('fecharTrocarSenha')

senhaBtn.addEventListener('click', () => {
    popUpTrocarSenha.classList.add('mostrar')
})

fecharTrocarSenha.addEventListener('click', () => {
    popUpTrocarSenha.classList.remove('mostrar')
})

document.getElementById('deslogar').addEventListener('click', deslogar)
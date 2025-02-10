// Função para formatar CPF
function formatarCPF(input) {
    let valor = input.value.replace(/\D/g, ''); // Remove caracteres não numéricos
    if (valor.length <= 11) {
        valor = valor.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
    }
    input.value = valor;
}

// Função para formatar CEP
function formatarCEP(input) {
    let valor = input.value.replace(/\D/g, ''); // Remove caracteres não numéricos
    if (valor.length <= 8) {
        valor = valor.replace(/(\d{5})(\d{3})/, '$1-$2');
    }
    input.value = valor;
}

// Função para validar CPF
function validarCPF(cpf) {
    cpf = cpf.replace(/\D/g, '');

    if (cpf.length !== 11 || /^(\d)\1+$/.test(cpf)) {
        return false; 
    }

    let soma = 0;
    for (let i = 0; i < 9; i++) {
        soma += parseInt(cpf.charAt(i)) * (10 - i);
    }
    let resto = soma % 11;
    let primeiroDigito = resto < 2 ? 0 : 11 - resto;

    if (primeiroDigito !== parseInt(cpf.charAt(9))) {
        return false;
    }

    soma = 0;
    for (let i = 0; i < 10; i++) {
        soma += parseInt(cpf.charAt(i)) * (11 - i);
    }
    resto = soma % 11;
    let segundoDigito = resto < 2 ? 0 : 11 - resto;

    return segundoDigito === parseInt(cpf.charAt(10));
}

// Função para validar CEP
function validarCEP(cep) {
    cep = cep.replace(/\D/g, ''); // Remove caracteres não numéricos
    return cep.length === 8; // Verifica se o CEP possui 8 dígitos
}

// Função para enviar os dados do usuário
async function enviarUsuario() {
    const cpfInput = document.getElementById('cpf').value.replace(/\D/g, '');
    const cepInput = document.getElementById('cep').value.replace(/\D/g, '');
    
    const dadosForm = {
        nome: document.getElementById('nome').value,
        nomeCompleto: document.getElementById('nomeCompleto').value,
        email: document.getElementById('email').value,
        senha: document.getElementById('senha').value,
        cpf: cpfInput,
        dataNascimento: document.getElementById('dataNascimento').value,
        cep: cepInput
    };

    // Validação de campos vazios
    for (let campo in dadosForm) {
        if (!dadosForm[campo]) {
            alert(`O campo ${campo} não pode estar vazio!`);
            return;
        }
    }

    if (!validarCPF(dadosForm.cpf)) {
        alert("CPF inválido, insira um CPF válido!");
        cpfInput.focus();
        return;
    }

    if (!validarCEP(dadosForm.cep)) {
        alert("CEP inválido, insira um CEP válido!");
        cepInput.focus();
        return;
    }

    console.log("Enviando dados do usuário:", dadosForm);

    // Enviar os dados para o backend
    try {
        const resposta = await fetch("http://localhost:8080/save", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(dadosForm)
        });

        if (!resposta.ok) {
            let errorText = await resposta.text();
            throw new Error(errorText);
        }

        if (resposta.ok) {
            alert("Usuário cadastrado com sucesso!");
            window.location.href = "/login";
        }
        
    } catch (error) {
        console.error("Erro ao enviar dados:", error);

        if (error.message.includes("CPF já cadastrado")) {
            alert("Erro: CPF já cadastrado no sistema!");
        } else if (error.message.includes("CEP já cadastrado")) {
            alert("Erro: CEP já cadastrado no sistema!");
        } else if(error.message.includes("CEP inexistente!")){
            alert("Erro: esse CEP não existe!")
        }
        else {
            alert("Ocorreu um erro ao cadastrar o usuário. Verifique o console para mais detalhes.");
        }
    }
}

// Adicionar evento ao botão
document.getElementById('cadastrar').addEventListener("click", enviarUsuario);

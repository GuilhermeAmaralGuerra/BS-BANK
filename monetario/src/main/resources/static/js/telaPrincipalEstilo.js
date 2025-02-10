document.addEventListener('DOMContentLoaded', () => {
    const header = document.getElementById('header');
    const button = document.getElementById('button-header');

    button.addEventListener('click', () => {
        header.classList.toggle('visibilidadeHeader');
    });
});


function ocultarSaldo(){
    const saldo = document.getElementById('saldo');
    const botao = document.getElementById('visibilidadeSaldo');

    if (saldo.textContent === "*****") {
        pegarSaldo();
        botao.textContent = "Ocultar saldo";
    } else {
        saldo.textContent = "*****";
        botao.textContent = "Mostrar saldo";
    }
}

document.getElementById('visibilidadeSaldo').addEventListener("click", ocultarSaldo)

const transferirBtn = document.getElementById('transferirBtn');
const popUpTransferir = document.getElementById('popUpTransferir');
const fecharTransferir = document.getElementById('fecharTransferir')

transferirBtn.addEventListener('click', () => {
    popUpTransferir.classList.add('mostrar');
});

fecharTransferir.addEventListener('click', () => {
    popUpTransferir.classList.remove('mostrar');
});

const saqueDepositoBtn = document.getElementById('saqueDepositoBtn');
const popUpSaqueDeposito = document.getElementById('popUpSaqueDeposito');
const fecharSaqueDeposito = document.getElementById('fecharSaqueDeposito');

saqueDepositoBtn.addEventListener('click', () => {
    popUpSaqueDeposito.classList.add('mostrar');
});

fecharSaqueDeposito.addEventListener('click' , () => {
    popUpSaqueDeposito.classList.remove('mostrar');
});

const emprestimoBtn = document.getElementById('emprestimoBtn');
const popUpEmprestimo = document.getElementById('popUpEmprestimo');
const fecharEmprestimo = document.getElementById('fecharEmprestimo');

emprestimoBtn.addEventListener('click', () => {
    popUpEmprestimo.classList.add('mostrar');
});

fecharEmprestimo.addEventListener('click', () => {
    popUpEmprestimo.classList.remove('mostrar');
})

const pagarEmprestimoBtn = document.getElementById('pagarEmprestimoBtn');
const popUpPagarEmprestimo = document.getElementById('popUpPagarEmprestimo');
const fecharPagarEmprestimo = document.getElementById('fecharPagarEmprestimo');

pagarEmprestimoBtn.addEventListener('click', () => {
    popUpPagarEmprestimo.classList.add('mostrar');
});

fecharPagarEmprestimo.addEventListener('click', () => {
    popUpPagarEmprestimo.classList.remove('mostrar');
})

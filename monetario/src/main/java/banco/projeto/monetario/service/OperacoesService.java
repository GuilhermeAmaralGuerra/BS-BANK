package banco.projeto.monetario.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import banco.projeto.monetario.domain.Conta;
import banco.projeto.monetario.domain.Operacoes;
import banco.projeto.monetario.exception.SaqueExcedenteException;
import banco.projeto.monetario.repository.ContaRepository;
import banco.projeto.monetario.repository.OperacoesRepository;
import banco.projeto.monetario.request.OperacoesPostRequestBody;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OperacoesService {

    private final OperacoesRepository operacoesRepository;
    private final ContaService contaService;
    private final ContaRepository contaRepository;

    public Operacoes saveOperacoes(OperacoesPostRequestBody operacoesPostRequestBody, Long userId){

        Long contaId = contaService.pegarContaId(userId);
        Conta conta = contaRepository.findById(contaId)
                        .orElseThrow(() -> new RuntimeException("Conta não encontrada para o ID: " + contaId));
        LocalDateTime dataOperacao = LocalDateTime.now();

        if (operacoesPostRequestBody.getTipo().equals("saque")) {
            if (BigDecimal.valueOf(operacoesPostRequestBody.getValor()).compareTo(conta.getSaldo()) > 0) {
                throw new SaqueExcedenteException("O valor do saque é superior ao seu saldo!");
            }
        }

        if (operacoesPostRequestBody.getTipo().equals("saque")) {
            contaRepository.saque(contaId, operacoesPostRequestBody.getValor());
        } else if (operacoesPostRequestBody.getTipo().equals("deposito")) {
            contaRepository.deposito(contaId, operacoesPostRequestBody.getValor());
        } else{
            System.out.println("Erro");
        }

        Operacoes operacoes = Operacoes.builder()
                                .tipo(operacoesPostRequestBody.getTipo())
                                .dataOperacao(dataOperacao)
                                .valor(BigDecimal.valueOf(operacoesPostRequestBody.getValor()))
                                .conta(conta)
                                .build();

        return operacoesRepository.save(operacoes);

    }
}

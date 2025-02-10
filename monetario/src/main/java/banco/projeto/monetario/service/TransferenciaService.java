package banco.projeto.monetario.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import banco.projeto.monetario.domain.Conta;
import banco.projeto.monetario.domain.Transferencia;
import banco.projeto.monetario.exception.DestinatarioException;
import banco.projeto.monetario.exception.SaldoException;
import banco.projeto.monetario.repository.ContaRepository;
import banco.projeto.monetario.repository.TransferenciaRepository;
import banco.projeto.monetario.request.TransferenciaPostRequestBody;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransferenciaService {

    private final TransferenciaRepository transferenciaRepository;
    private final ContaService contaService;
    private final ContaRepository contaRepository;

    @Transactional
    public Transferencia saveTransferencia(TransferenciaPostRequestBody transferenciaPostRequestBody, Long userId){

        Conta destinatario = contaRepository.findByCodigoTransferencia(transferenciaPostRequestBody.getCodigo());

        if (destinatario == null) {
            throw new DestinatarioException("Destinatário não encontrado");
        }

        Long contaId = contaService.pegarContaId(userId);

        Conta conta = contaRepository.findById(contaId)
                        .orElseThrow(() -> new RuntimeException("Conta não encontrada para o ID: " + contaId));

        if (destinatario.getId().equals(conta.getId())) {
            throw new DestinatarioException("Você está transferindo para si mesmo, verifique o código de transferência!");
        }

        double saldo = contaRepository.findSaldoByUsuarioId(userId);

        if (transferenciaPostRequestBody.getValor() <= saldo) {
            contaRepository.debitar(contaId, transferenciaPostRequestBody.getValor());
            contaRepository.creditar(transferenciaPostRequestBody.getCodigo(), transferenciaPostRequestBody.getValor());
        } else {
            throw new SaldoException("Saldo Insuficiente!");
        }

        Transferencia transferencia = Transferencia.builder()
                                        .valor(BigDecimal.valueOf(transferenciaPostRequestBody.getValor()))
                                        .data(LocalDateTime.now())
                                        .descricao(transferenciaPostRequestBody.getDescricao())
                                        .destinatario(transferenciaPostRequestBody.getCodigo())
                                        .conta(conta)
                                        .build();

        return transferenciaRepository.save(transferencia);

    }

}

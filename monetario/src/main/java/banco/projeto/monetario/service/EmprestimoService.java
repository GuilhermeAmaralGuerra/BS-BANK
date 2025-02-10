package banco.projeto.monetario.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import banco.projeto.monetario.domain.Conta;
import banco.projeto.monetario.domain.Emprestimo;
import banco.projeto.monetario.domain.EmprestimoParcial;
import banco.projeto.monetario.exception.EmprestimoException;
import banco.projeto.monetario.exception.EmprestimoPagamentoException;
import banco.projeto.monetario.repository.ContaRepository;
import banco.projeto.monetario.repository.EmprestimoParcialRepository;
import banco.projeto.monetario.repository.EmprestimoRepository;
import banco.projeto.monetario.request.EmprestimoPostRequestBody;
import banco.projeto.monetario.request.EmprestimoPutRequestBody;
import lombok.RequiredArgsConstructor;

//Service da classe emprestimo
@Service
@RequiredArgsConstructor
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final ContaService contaService;
    private final ContaRepository contaRepository;
    private final EmprestimoParcialRepository emprestimoParcialRepository;

    @Transactional
    public Emprestimo saveEmprestimo(EmprestimoPostRequestBody emprestimoPostRequestBody, Long userID){

        Long contaId = contaService.pegarContaId(userID);
        Conta conta = contaRepository.findById(contaId)
                        .orElseThrow(() -> new RuntimeException("Conta não encontrada para o ID: " + contaId));

        List<Emprestimo> emprestimosPendentes = emprestimoRepository.findEmprestimosByContaId(contaId);

        for(Emprestimo emprestimo : emprestimosPendentes) {
            if (emprestimo.getSituacao().equals("Pendente")) {
                throw new EmprestimoException("A conta já possui um empréstimo pendente e não pode fazer outro.");
            } else if (emprestimo.getSituacao().equals("pago_parcialmente")) {
                throw new EmprestimoException("A conta possui um empréstimo pago parcialmente, pague-o totalmente antes de fazer outro.");
            }
        }

        String situacaoEmprestimo = "Pendente";
        LocalDateTime dataEmprestimo = LocalDateTime.now();
        double juros;

        if (emprestimoPostRequestBody.getValor() < 10000) {
            juros = 1;
        } else{
            juros = 1.5;
        }

        contaRepository.emprestimo(contaId, emprestimoPostRequestBody.getValor());

        Emprestimo emprestimo = Emprestimo.builder()
                                .valor(BigDecimal.valueOf(emprestimoPostRequestBody.getValor()))
                                .situacao(situacaoEmprestimo)
                                .dataEmprestimo(dataEmprestimo)
                                .juros(juros)
                                .motivo(emprestimoPostRequestBody.getMotivo())
                                .conta(conta)
                                .build();

        return emprestimoRepository.save(emprestimo);

    }

    public List<Double> pegarValorEJuros(Long userId) {
        Long contaId = contaService.pegarContaId(userId);
    
        double juros = emprestimoRepository.findJurosByContaId(contaId);
        double valor = emprestimoRepository.findValorByContaId(contaId);
        Double valorPago = emprestimoRepository.findValorPagoByContaId(contaId);
        LocalDateTime dataEmprestimo = emprestimoRepository.findDataEmprestimoByContaId(contaId);
        LocalDateTime dataAtual = LocalDateTime.now();
    
        long diferencaMeses = ChronoUnit.MONTHS.between(dataEmprestimo, dataAtual);
        
        if (valorPago == null) {
            valorPago = 0.0;
        }

        double valorTotal = (valor - valorPago) * Math.pow(1 + (juros / 100), diferencaMeses);
    
        return List.of(valorTotal, juros);
    }

    @Transactional
    public String processarPagamento(EmprestimoPutRequestBody emprestimoPutRequestBody, Long userId){

        Long contaId = contaService.pegarContaId(userId);
        Conta conta = contaRepository.findById(contaId)
                    .orElseThrow(() -> new RuntimeException("Conta não encontrada para o ID: " + contaId));

        Optional<Long> optionalEmprestimoId = emprestimoRepository.findIdEmprestimoNaoPagoByContaID(contaId);
        if (optionalEmprestimoId.isEmpty()) {
            throw new RuntimeException("Nenhum empréstimo pendente encontrado para a conta ID: " + contaId);
        }

        Long emprestimoId = optionalEmprestimoId.get();
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado para o ID: " + emprestimoId));

                    
        List<Double> valorTotalList = pegarValorEJuros(userId);
        Double valorTotal = valorTotalList.get(0); 

        BigDecimal valorPagamento = BigDecimal.valueOf(emprestimoPutRequestBody.getValorPago());


        if (emprestimo.getValorPago() == null) {
            emprestimo.setValorPago(BigDecimal.ZERO);
        }

        System.out.println(valorTotal);
        System.out.println(valorPagamento);

        BigDecimal valorTotalBigDecimal = BigDecimal.valueOf(valorTotal).setScale(2, RoundingMode.HALF_UP);

        if (valorPagamento.compareTo(valorTotalBigDecimal) > 0) {
            throw new EmprestimoPagamentoException("O valor do pagamento não pode ser maior que o valor total do empréstimo!");
        } 

        if (valorTotalBigDecimal.compareTo(valorPagamento) == 0) {
            emprestimo.setSituacao("Pago");
            emprestimo.setDataPagamentoFinal(LocalDateTime.now());

            BigDecimal saldoNovoConta = conta.getSaldo().subtract(valorPagamento);
            conta.setSaldo(saldoNovoConta);

            emprestimo.setSituacao("pago");

            BigDecimal novoValorPago = emprestimo.getValorPago().add(valorPagamento);
            emprestimo.setValorPago(novoValorPago);

            emprestimoRepository.save(emprestimo);

            EmprestimoParcial emprestimoParcial = EmprestimoParcial.builder()
                                                .dataPagamento(LocalDateTime.now())
                                                .valorPagoParcialmente(BigDecimal.valueOf(emprestimoPutRequestBody.getValorPago()))
                                                .conta(conta)
                                                .emprestimo(emprestimo)
                                                .build();

            emprestimoParcialRepository.save(emprestimoParcial);
                                            
            return "Empréstimo quitado com sucesso!";
        }else{

            EmprestimoParcial emprestimoParcial = EmprestimoParcial.builder()
                                                .dataPagamento(LocalDateTime.now())
                                                .valorPagoParcialmente(BigDecimal.valueOf(emprestimoPutRequestBody.getValorPago()))
                                                .conta(conta)
                                                .emprestimo(emprestimo)
                                                .build();
            
            emprestimo.setSituacao("pago_parcialmente");

            BigDecimal saldoNovoConta = conta.getSaldo().subtract(valorPagamento);
            conta.setSaldo(saldoNovoConta);
            
            emprestimoParcialRepository.save(emprestimoParcial);

            BigDecimal novoValorPago = emprestimo.getValorPago().add(valorPagamento);
            emprestimo.setValorPago(novoValorPago);

            return "Pagamento parcial registrado com sucesso!";

        }
    
    }
}

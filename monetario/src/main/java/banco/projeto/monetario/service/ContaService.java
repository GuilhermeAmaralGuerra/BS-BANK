package banco.projeto.monetario.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import banco.projeto.monetario.domain.Conta;
import banco.projeto.monetario.repository.ContaRepository;
import banco.projeto.monetario.repository.UsuarioRepository;
import banco.projeto.monetario.request.UsuarioPostRequestBody;
import banco.projeto.monetario.utils.ContaAleatorioUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContaService {
    private final UsuarioRepository usuarioRepository;
    private final ContaRepository contaRepository;

    public Conta saveConta(UsuarioPostRequestBody usuarioPostRequestBody){

        var usuario = usuarioRepository.findByEmail(usuarioPostRequestBody.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        Conta conta = Conta.builder()
                        .numeroConta(ContaAleatorioUtils.numeroConta())
                        .saldo(BigDecimal.ZERO)
                        .codigoTransferencia(ContaAleatorioUtils.codigoTransferencia())
                        .dataAbertura(LocalDateTime.now())
                        .usuario(usuario)
                        .build();

        return contaRepository.save(conta);
    }

    public double pegarSaldo(Long userId){
        double saldo = contaRepository.findSaldoByUsuarioId(userId);
        return saldo;
    }

    public Long pegarContaId(Long userId){
        Long contaId = contaRepository.findIdByUsuarioId(userId);
        return contaId;
    }
}

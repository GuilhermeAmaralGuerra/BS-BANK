package banco.projeto.monetario.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Emprestimo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 13, scale = 2)
    private BigDecimal valor;

    private String situacao; //pago, pendente, pago parcialmente

    private LocalDateTime dataEmprestimo;

    private LocalDateTime dataPagamentoFinal;

    private double juros;

    private String motivo;

    @Column(precision = 13, scale = 2)
    private BigDecimal valorPago; //emprestimo de 1000, pago 500, restou 500 pra pagar

    @ManyToOne
    @JoinColumn(name = "conta_id")
    private Conta conta;

    @OneToMany(mappedBy = "emprestimo", cascade = CascadeType.ALL)
    private List<EmprestimoParcial> emprestimosParcialmenteData;
}

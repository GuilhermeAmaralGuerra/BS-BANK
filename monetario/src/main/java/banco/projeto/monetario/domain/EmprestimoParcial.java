package banco.projeto.monetario.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class EmprestimoParcial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataPagamento;

    @Column(precision = 13, scale = 2)
    private BigDecimal valorPagoParcialmente;

    @ManyToOne
    @JoinColumn(name = "emprestimo_id") 
    private Emprestimo emprestimo;

    @ManyToOne
    @JoinColumn(name = "conta_id") 
    private Conta conta;

}

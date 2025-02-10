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
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 7, scale = 2)
    private BigDecimal valor;

    private LocalDateTime data;

    private String descricao;

    private Long destinatario; //codigo para quem vai

    @ManyToOne
    @JoinColumn(name = "conta_id", nullable = false) // Chave estrangeira para a tabela `Conta`
    private Conta conta;

    @ManyToOne
    @JoinColumn(name = "tags_id")
    private Tags tags;
}

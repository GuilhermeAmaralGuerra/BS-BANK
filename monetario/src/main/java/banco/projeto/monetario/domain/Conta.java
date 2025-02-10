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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 15)
    private String numeroConta;

    @Column(precision = 13, scale = 2)
    private BigDecimal saldo;

    @Column(unique = true)
    private long codigoTransferencia;

    private LocalDateTime dataAbertura;

    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;

    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL)
    private List<Operacoes> operacoes;

    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL)
    private List<Emprestimo> emprestimos;

    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL)
    private List<EmprestimoParcial> emprestimosParcial;

    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL)
    private List<Transferencia> transferencias;

}

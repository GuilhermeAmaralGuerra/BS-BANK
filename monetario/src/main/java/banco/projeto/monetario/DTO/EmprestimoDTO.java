package banco.projeto.monetario.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmprestimoDTO {

    private BigDecimal valor;

    private String situacao; 

    private LocalDateTime dataEmprestimo;

    private double juros;

    private String motivo;

}

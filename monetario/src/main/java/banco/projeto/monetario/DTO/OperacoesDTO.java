package banco.projeto.monetario.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperacoesDTO {

    private String tipo;
    private LocalDateTime dataOperacao;
    private BigDecimal valor;

}

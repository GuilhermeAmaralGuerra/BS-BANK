package banco.projeto.monetario.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferenciaDTO {

    private BigDecimal valor;
    private LocalDateTime data;
    private String descricao;
    private Long destinatario;
    
}

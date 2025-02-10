package banco.projeto.monetario.request;

import lombok.Data;

@Data
public class TransferenciaPostRequestBody {

    private Long codigo;

    private double valor;

    private String descricao;
    
}

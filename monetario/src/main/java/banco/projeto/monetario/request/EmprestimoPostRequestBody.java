package banco.projeto.monetario.request;

import lombok.Data;

@Data
public class EmprestimoPostRequestBody {

    private double valor;

    private String motivo;
}

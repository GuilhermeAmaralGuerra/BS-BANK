package banco.projeto.monetario.request;

import lombok.Data;

@Data
public class OperacoesPostRequestBody {

    private double valor;

    private String tipo;

}
package banco.projeto.monetario.request;

import lombok.Data;

@Data
public class TrocarSenhaPutRequestBody {

    private String senhaAntiga;
    
    private String senhaNova;

}

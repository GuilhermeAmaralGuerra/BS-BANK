package banco.projeto.monetario.request;

import lombok.Data;

@Data
public class UsuarioLoginRequestBody {
    private String email;

    private String senha;
}

package banco.projeto.monetario.DTO;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private String nome;
    private String nomeCompleto;
    private String email;
    private LocalDate dataNascimento;
    private String senha;
    private String cpf;
    private String cep;

}

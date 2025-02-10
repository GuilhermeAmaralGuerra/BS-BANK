package banco.projeto.monetario.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UsuarioPutRequestBody {
private String nome;

    private String nomeCompleto;

    private String email;

    private String cpf;

    private LocalDate dataNascimento;
    
    private String cep;
}

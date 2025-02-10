package banco.projeto.monetario.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DadoJaExisteException extends RuntimeException{
    public DadoJaExisteException(String message){
        super(message);
    }


}

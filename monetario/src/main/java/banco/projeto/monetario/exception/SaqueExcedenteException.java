package banco.projeto.monetario.exception;

public class SaqueExcedenteException extends RuntimeException{
    public SaqueExcedenteException(String message){
        super(message);
    }
}

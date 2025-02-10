package banco.projeto.monetario.utils;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class VerificacaoUtils {
    public static boolean verificarSenha(String senha, String senhaCodificada) {
        
        Argon2 argon2 = Argon2Factory.create();

        return argon2.verify(senhaCodificada, senha);
    }
}


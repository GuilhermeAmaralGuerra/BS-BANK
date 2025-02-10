package banco.projeto.monetario.utils;

import java.util.Random;

public class ContaAleatorioUtils {

    public static String numeroConta(){
        Random random = new Random();

        StringBuilder numero = new StringBuilder();
        
        for (int i = 0; i < 15; i++) {
            int digito = random.nextInt(10);
            numero.append(digito);
        }

        return numero.toString();
    }

    public static Long codigoTransferencia(){
        Random random = new Random();

        long codigo = 1_000_000_000L + (long)(random.nextDouble() * 9_000_000_000L);

        return codigo;
    }

}

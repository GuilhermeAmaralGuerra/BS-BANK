package banco.projeto.monetario.utils;

public class ValidacaoUtils {

    public static boolean validarCPF(String cpf){
        cpf = cpf.replaceAll("\\D", ""); 

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int soma = 0, peso = 10;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * peso--;
        }

        int resto = 11 - (soma % 11);
        char digito1 = (resto == 10 || resto == 11) ? '0' : (char) (resto + '0');

        soma = 0;
        peso = 11;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * peso--;
        }

        resto = 11 - (soma % 11);
        char digito2 = (resto == 10 || resto == 11) ? '0' : (char) (resto + '0');

        return digito1 == cpf.charAt(9) && digito2 == cpf.charAt(10);
    }

    public static boolean validarCEP(String cep){
        cep = cep.replaceAll("\\D", "");
        return cep.matches("\\d{8}");
    }
    
}

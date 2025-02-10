package banco.projeto.monetario.utils;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import banco.projeto.monetario.exception.DescriptografiaException;

public class DescriptografiaUtils {

    private static final String ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";
    private static final String SECRET_KEY = "chave-secreta-12"; //Apenas para fins de estudo a chave não está em um lugar seguro
    private static final int IV_SIZE = 12; 
    private static final int TAG_SIZE = 128;

    public static String descriptografarCepCef(String text){
        try {
            byte[] ivAndCipherText = Base64.getDecoder().decode(text);

            byte[] iv = new byte[IV_SIZE];
            System.arraycopy(ivAndCipherText, 0, iv, 0, IV_SIZE);

            byte[] cipherText = new byte[ivAndCipherText.length - IV_SIZE];
            System.arraycopy(ivAndCipherText, IV_SIZE, cipherText, 0, cipherText.length);

            SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_SIZE, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);

            byte[] textDescriptografado = cipher.doFinal(cipherText);
            return new String(textDescriptografado);
        } catch (Exception e) {
            throw new DescriptografiaException("Erro ao criptografar texto", e);
        }
    }

}

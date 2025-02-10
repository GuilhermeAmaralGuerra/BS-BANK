package banco.projeto.monetario.utils;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import banco.projeto.monetario.exception.CriptografiaException;

import java.util.Base64;

public class CriptografiaUtils {

    private static final String ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";
    private static final String SECRET_KEY = "chave-secreta-12"; //Apenas para fins de estudo a chave não está em um lugar seguro
    private static final int IV_SIZE = 12; 
    private static final int TAG_SIZE = 128;

    public static String codificarSenha(String password) {
        Argon2 argon2 = Argon2Factory.create();
        String senhaCodificada = null;

        try {
            senhaCodificada = argon2.hash(2, 65536, 1, password); 
        } finally {
            argon2.wipeArray(password.toCharArray()); 
        }

        return senhaCodificada;
    }


    public static String criptografarCepCpf(String text) {
    try {
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        byte[] iv = new byte[IV_SIZE];
        new java.security.SecureRandom().nextBytes(iv);

        SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_SIZE, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);

        byte[] cipherText = cipher.doFinal(text.getBytes());
        byte[] ivAndCipherText = new byte[IV_SIZE + cipherText.length];
        System.arraycopy(iv, 0, ivAndCipherText, 0, IV_SIZE);
        System.arraycopy(cipherText, 0, ivAndCipherText, IV_SIZE, cipherText.length);

        return Base64.getEncoder().encodeToString(ivAndCipherText);
    } catch (Exception e) {
        throw new CriptografiaException("Erro ao criptografar texto", e);
    }
}

}


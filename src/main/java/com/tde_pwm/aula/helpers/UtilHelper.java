package com.tde_pwm.aula.helpers;

import java.security.MessageDigest;
import java.util.concurrent.atomic.AtomicInteger;

public class UtilHelper {
      // Criptografia para a senha do usuário
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] hashedBytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.substring(0, 25);

        } catch (Exception e) {
            return null;
        }
    }

}

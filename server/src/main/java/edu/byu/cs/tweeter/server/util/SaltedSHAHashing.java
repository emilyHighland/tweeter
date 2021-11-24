package edu.byu.cs.tweeter.server.util;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Base64;

// This is pretty strong security
public class SaltedSHAHashing {
    public Pair<String,String> getSecurePassword(String password) {
        // Given at registration
//        System.out.println("Non-Secure Password: " + password);

        // Store this in the database
        String salt = getSalt();
//        System.out.println("Salt: " + salt);

        // Store this in the database
        String securePassword = getSecurePassword(password, salt);
//        System.out.println("Secured Password: " + securePassword);

        return new Pair<String,String>(salt,securePassword);

        }

    public static String getSecurePassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "FAILED TO HASH PASSWORD";
    }

    private static String getSalt() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
            byte[] salt = new byte[16];
            sr.nextBytes(salt);
            return Base64.getEncoder().encodeToString(salt);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return "FAILED TO GET SALT";
    }
}
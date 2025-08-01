package com.example.shopping.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptTest {
    public static void main(String[] args) {
        String passphrase = "admin123";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String hash = encoder.encode(passphrase);
        System.out.println(passphrase + " → " + hash);
        System.out.println(
                "mysql -u root -p shopping_db -e \"UPDATE users SET password = '"
                        + hash.replaceAll("\\$", "\\\\\\$") + "' WHERE username = 'admin';\"");
    }
}
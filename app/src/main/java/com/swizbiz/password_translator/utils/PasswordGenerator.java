package com.swizbiz.password_translator.utils;

import java.util.Random;

public class PasswordGenerator {
    public static String generate(int length, boolean useUppercase,  boolean useDigits, boolean useSpecialSymbols) {
        String characters = "abcdefghijklmnopqrstuvwxyz";
        if (useUppercase)
            characters += "ABCDEFGHIJLMNOPQRSTUVWXYZ";
        if (useDigits)
            characters += "1234567890";
        if (useSpecialSymbols)
            characters += "!@#$%^&*()_+";

        StringBuilder result = new StringBuilder();
        Random random = new Random();
        while(length > 0) {
            result.append(characters.charAt(random.nextInt(characters.length())));
            length--;
        }
        return result.toString();
    }
}

package com.swizbiz.password_translator.utils;

import java.util.HashMap;
import java.util.Map;

public class PasswordHelper {

    private Map<String, String> letters;

    public PasswordHelper(String[] russians, String[] latins) {
        if (russians.length != latins.length) {
            throw new IllegalArgumentException();
        }
        letters = new HashMap<>(russians.length);
        for (int i = 0; i < russians.length; i++) {
            letters.put(russians[i], latins[i]);
        }
    }

    public String convert(CharSequence source) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            String key = String.valueOf(Character.toLowerCase(c));
            String value = letters.get(key);

            if (value != null) {
                result.append(Character.isUpperCase(c) ?
                        value.toUpperCase() : value);
            } else
                result.append(c);

            if (result.length() <= i) {
                result.append(c);
            }
        }

        return result.toString();
    }

    public int getQuality(CharSequence password) {
        return Math.min(password.length(), 10);
    }

}

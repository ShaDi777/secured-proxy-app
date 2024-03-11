package ru.shadi777.proxyapplication.utils;

import org.json.JSONObject;

public interface CustomSpelFunctions {
    static String jsonParser(String line, String key) {
        return String.valueOf(new JSONObject(line).getLong(key));
    }
}
package com.verycoolprojects.youtubeapp.util;

import java.util.Objects;

public class Utils {
    public static String maskToken(String token) {
        int length = 4;
        if (Objects.isNull(token) || token.length() < length) {
            return token;
        } else {
            return "..." + token.substring(token.length() - 4);
        }
    }
}

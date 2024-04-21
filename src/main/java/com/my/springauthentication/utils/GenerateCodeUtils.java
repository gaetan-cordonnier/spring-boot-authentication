package com.my.springauthentication.utils;

import java.util.Random;

public class GenerateCodeUtils {

    public static Integer randomCode() {
        Random random = new Random();

        return random.nextInt(9000) + 1000;
    }

}

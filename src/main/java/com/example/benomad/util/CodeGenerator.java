package com.example.benomad.util;


import net.bytebuddy.utility.RandomString;

import java.util.Random;

public class CodeGenerator {
    public static String generateActivationCode(){
        Random random = new Random();
        String code = "000000" + (random.nextInt(999900) + 100);
        return code.substring(code.length() - 6);
    }

    public static String generateResetPasswordCode(){
        return RandomString.make(35);
    }
}

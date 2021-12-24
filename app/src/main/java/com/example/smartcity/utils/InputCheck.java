package com.example.smartcity.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputCheck {
    public static Boolean isEmailValid(String email){
        Pattern pattern = Pattern.compile("^(.+)@(.+)$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return isWordCorrect(email) && matcher.matches();
    }

    public static Boolean isPasswordValid(String password){
        return password != null && password.trim().length() > 5;
    }

    public static Boolean isWordCorrect(String word){
        return word != null && word.trim().length() > 2;
    }

    public static Boolean isDateValid(String date){
        return !date.isEmpty();
    }

    public static boolean isANumber(String number){
        try{
            Integer.parseInt(number);
        }catch(NumberFormatException e){
            return false;
        }
        return true;
    }
}

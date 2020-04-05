package com.ushill.utils;

import com.ushill.exception.http.ParameterException;

public class BoolUtils {

    public static boolean trueOrFalse(String str, int code){
        switch (str){
            case "1":
            case "true":
            case "True":
            case "TRUE":
            case "t":
            case "T":
                return true;
            case "0":
            case "false":
            case "False":
            case "FALSE":
            case "F":
            case "f":
                return false;
            default:
                throw new ParameterException(code);
        }
    }

    public static boolean trueOrFalse(String str){
        return trueOrFalse(str, 10001);
    }
}

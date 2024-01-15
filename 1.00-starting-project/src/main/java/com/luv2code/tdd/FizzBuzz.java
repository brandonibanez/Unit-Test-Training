package com.luv2code.tdd;

public class FizzBuzz {

    public static String compute(int i) {

        StringBuilder result = new StringBuilder();

        result = i % 3 == 0 ? result.append("Fizz") : result;
        result = i % 5 == 0 ? result.append("Buzz") : result;
        result = result.isEmpty() ? result.append(i) : result;

        return result.toString();
    }

//    public static String compute(int i) {
//
//        if(i % 3 == 0 && i % 5 == 0) {
//            return "FizzBuzz";
//        }
//        else if(i % 3 == 00) {
//            return "Fizz";
//        }
//        else if(i % 5 == 00) {
//            return "Buzz";
//        }
//        else {
//            return String.valueOf(i);
//        }
//    }
}

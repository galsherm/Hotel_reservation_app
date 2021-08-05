package com.example.finalproject;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class CheckIfValid {
    //check if email is valid
    public static boolean isValidEmail(String email) //check with regular expression
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }


    //check if credit card is valid

    // Return true if the card number is valid
    public static boolean isValidCreditCard(long number) //it uses Luhn algorithm
    {
        return (getSize(number) >= 13 &&
                getSize(number) <= 16) &&
                (prefixMatched(number, 4) ||
                        prefixMatched(number, 5) ||
                        prefixMatched(number, 37) ||
                        prefixMatched(number, 6)) &&
                ((sumOfDoubleEvenPlace(number) +
                        sumOfOddPlace(number)) % 10 == 0);
    }

    // Get the result from Step 2
    public static int sumOfDoubleEvenPlace(long number)
    {
        int sum = 0;
        String num = number + "";
        for (int i = getSize(number) - 2; i >= 0; i -= 2)
            sum += getDigit(Integer.parseInt(num.charAt(i) + "") * 2);

        return sum;
    }

    // Return this number if it is a single digit, otherwise,
    // return the sum of the two digits
    public static int getDigit(int number)
    {
        if (number < 9)
            return number;
        return number / 10 + number % 10;
    }

    // Return sum of odd-place digits in number
    public static int sumOfOddPlace(long number)
    {
        int sum = 0;
        String num = number + "";
        for (int i = getSize(number) - 1; i >= 0; i -= 2)
            sum += Integer.parseInt(num.charAt(i) + "");
        return sum;
    }

    // Return true if the digit d is a prefix for number
    public static boolean prefixMatched(long number, int d)
    {
        return getPrefix(number, getSize(d)) == d;
    }

    // Return the number of digits in d
    public static int getSize(long d)
    {
        String num = d + "";
        return num.length();
    }

    // Return the first k number of digits from
    // number. If the number of digits in number
    // is less than k, return number.
    public static long getPrefix(long number, int k)
    {
        if (getSize(number) > k) {
            String num = number + "";
            return Long.parseLong(num.substring(0, k));
        }
        return number;
    }

    public  static String checkPersonDetails(Person person) {
        String errMsg = "", rMsg = "";
        int partPhoneNumLength = 7;
        if (person.getfirstName().isEmpty()) {
            errMsg = "first name ";
        }
        if (person.getlastName().isEmpty()) {
            errMsg += "last name ";
        }
        if (person.getEmail().isEmpty() || !CheckIfValid.isValidEmail(person.getEmail())) {
            errMsg += "Email ";
        }
        System.out.println("phone number "+person.getphoneNum());
        String[] phone = person.getphoneNum().split("-",0);//phone number get in phone[0] -> prefix ,in phone[1] -> get the rest of the phone number
        if(phone.length==1) {       // if phone contains only prefix in phone[0] it means: there is no data in phone[1] -> get the rest of the phone number
            errMsg += "phone number ";
            return errMsg;

        }
        if (phone[1].length() != partPhoneNumLength) {
            errMsg += "phone number ";
        }

        return errMsg;
    }
}
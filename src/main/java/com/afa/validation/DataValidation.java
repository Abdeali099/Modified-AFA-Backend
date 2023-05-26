package com.afa.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidation {

    public static boolean validateSignup(String userName,String userEmail,String userPhone, String userPassword){

        try {

            /* Validation of userName : */
           boolean isNameValidate=validateUserName(userName);

            if (!isNameValidate) {
                return false;
            }

            /* Validation of userEmail : */
           boolean isEmailValidate=validateUserEmail(userEmail);

            if (!isEmailValidate) {
                return false;
            }

            /* Validation of userPhone : */
           boolean isPhoneValidate=validateUserPhone(userPhone);

            if (!isPhoneValidate) {
                return false;
            }

            /* Validation of userPassword : */
           boolean isPasswordValidate=validateUserPassword(userPassword);

            if (!isPasswordValidate) {
                return false;
            }

        } catch (Exception exception) {
            System.out.println("Error in Signup validation : " + exception);
            return false;
        }

        return true;
    }

    private static boolean validateUserPassword(String userPassword) {

        /* Requirement :
         * 1) Minimum length of 6
         * 2) any type of pattern allow
         * 3) Must include Character,digit,special character
         * */

        if (userPassword.length() < 6) {
            return false;
        }

        Pattern pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?!.*\\s).*$");
        Matcher matcher = pattern.matcher(userPassword);

        return matcher.matches();
    }

    private static boolean validateUserPhone(String userPhone) {

        /* Requirement :
         * 1) Must be length of 10
         * 2) Indian Numbers only
         * */

        Pattern pattern = Pattern.compile("^[6789]\\d{9}$");
        Matcher matcher = pattern.matcher(userPhone);

        return matcher.matches();
    }

    private static boolean validateUserEmail(String userEmail) {

        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userEmail);

        return matcher.matches();
    }

    private static boolean validateUserName(String userName){

            /* Requirement :
             * 1) Minimum length of 2
             * 2) Not have any special character
             * 3) Not have any digit
             * */

            if (userName.length() < 2) {
                return false;
            }

            Pattern pattern = Pattern.compile("^[a-zA-Z ]+$");
            Matcher matcher = pattern.matcher(userName);

        return matcher.find();
    }

}

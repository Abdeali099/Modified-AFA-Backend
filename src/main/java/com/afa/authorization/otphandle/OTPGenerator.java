package com.afa.authorization.otphandle;

import java.util.Random;

public class OTPGenerator {

    protected static String getOTP() {

        /* OTP length : 4 */
        String OTP = null;

        try {

            Random random = new Random();
            int randomNumber = random.nextInt(9000) + 1000;
            OTP=""+randomNumber;

        } catch (Exception exception) {
            System.out.println("Error in generating OTP " + exception);
        }

        return OTP;
    }

}

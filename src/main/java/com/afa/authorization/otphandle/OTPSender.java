package com.afa.authorization.otphandle;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@MultipartConfig
public class OTPSender extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            /* Add in all servlet and filter : by this cors policy will not break*/
            response.addHeader("Access-Control-Allow-Origin", "*");

            /* Get mail from request */
            String user_mail = request.getParameter("afa_userEmail");

            /* Generate OTP */
            String OTP = OTPGenerator.getOTP();

            /* Send OTP through mail */

            boolean isMailSend=MailSender.sendMail(user_mail,OTP);

            /* Sending response */

            if (isMailSend) {
                response.setStatus(200);
                response.getWriter().println(OTP);
            } else {
                response.setStatus(500);
                response.getWriter().println("Error");
            }
        } catch (Exception exception) {
            System.out.println("Error in sending OTP : " + exception);

            response.setStatus(500);
            response.getWriter().println("Error");
        }

    }
}

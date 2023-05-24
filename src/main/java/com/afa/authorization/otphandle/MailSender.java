package com.afa.authorization.otphandle;

import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class MailSender {

    public static boolean sendMail(String user_mail,String OTP){

        boolean isMailSend=true;

        /* Setting AFA credential */
        final String afa_email_id = "anytimefileaccess@gmail.com";
        final String afa_email_password = "mmnhywyvyaewzvbt";

        /* Set up the properties for the email server : Gmail server */
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        try {

            /* Authinticate AFA with credential and create session */
            Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(afa_email_id, afa_email_password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(afa_email_id));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user_mail));
            message.setSubject("AFA OTP verification");
            message.setText("Your OTP is  : " + OTP);

            Transport.send(message);

        }catch (Exception e) {
            System.out.println("\n => Error in sending confirmation mail : " + e);
             isMailSend=false;
        }

        return isMailSend;
    }

}

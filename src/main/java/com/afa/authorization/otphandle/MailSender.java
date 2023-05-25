package com.afa.authorization.otphandle;

import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;
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
            /* Authenticate AFA with credential and create session */
            Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(afa_email_id, afa_email_password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(afa_email_id));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user_mail));
            message.setSubject("AFA OTP verification");

            /* Get Mail Content */
            String mailContent=getMailContent(OTP);

            /* Set the content of the message as HTML */
            message.setContent(mailContent, "text/html");

            /* Sending mail */
            Transport.send(message);

        }catch (Exception e) {
            System.out.println("\n => Error in sending confirmation mail : " + e);
             isMailSend=false;
        }

        return isMailSend;
    }

    private static String getMailContent(String OTP) throws IOException {

        File mailContentFile;
        BufferedReader mailBufferReader = null;
        StringBuilder stringBuilder;
        String mailContent="";
        
        try {
            /* Fetching content file from resources */
            ClassLoader classLoader = MailSender.class.getClassLoader();
            mailContentFile = new File(Objects.requireNonNull(classLoader.getResource("MailContent.html")).getFile());

            /* Fetching content from file */
            mailBufferReader=new BufferedReader(new FileReader(mailContentFile));
            stringBuilder=new StringBuilder();

            while (mailContent!=null) {

                mailContent=mailBufferReader.readLine();

                if (mailContent!=null) {
                    stringBuilder.append(mailContent);
                    stringBuilder.append(System.lineSeparator());
                }
            }

            /* Replacing "XXXX" with  OTP  */
            mailContent=stringBuilder.toString();
            mailContent=mailContent.replace("XXXX",OTP);

            /* Replacing "DDDD" with Date */
            String date= String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            mailContent=mailContent.replace("YYYY",date);

        } catch (Exception exception) {
            System.out.println("Error in reading Content of mail + " + exception);
            return "<html> " +
                    "<body> " +
                    "<img style='width:150px' src='https://png.pngtree.com/png-vector/20191121/ourmid/pngtree-access-content-free-internet-open-png-image_2008031.jpg' alt='logo_AFA'> <br>" +
                    "<strong style='font-size: 1.5rem;'>Your OTP  is : <span style='color:red'>"+ OTP +" </span> </strong>" +
                    "<p style='color:green;font-size: 1.2rem;'>Valid for 2 minutes only.</p> " +
                    "</body> " +
                    "</html>";
        }finally {
            if (mailBufferReader != null) {
                mailBufferReader.close();
            }
        }

        return mailContent;
    }

}

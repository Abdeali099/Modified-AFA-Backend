package com.afa.authorization.signup;

import com.afa.database.ConnectionToDatabase;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

@MultipartConfig
public class AFASignup extends HttpServlet {

    /* Note : Create one filter which will check validation and other input fields */

    /* Global Object */
    Connection connection=null;

    @Override
    public void init() throws ServletException {

        /* Establishing connection : only once */
        try {
            connection= ConnectionToDatabase.getConnection();
        } catch (Exception e) {
            System.out.println("Error at signup connection establishment : " + e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");

        /* <-- Global variables --> */
        String userEmail,userPassword,userName,userPhone;

        try {

            /* <-- get details from request object --> */
            userName = request.getParameter("afa_username");
            userEmail = request.getParameter("afa_email");
            userPhone = request.getParameter("afa_phone");
            userPassword = request.getParameter("afa_password");

            System.out.println("\n => Name : " + userName +
                                "\n => EMail : " + userEmail +
                                "\n => Phone : " + userPhone +
                                "\n => Password : " + userPassword);

            response.getWriter().println("Success!!");

        } catch (Exception e) {
            System.out.print("\n => Error at Inside SignupServlet : " + e);
        }

    }

    @Override
    public void destroy() {
        try {
                if(connection!=null){
                    connection.close();
                }
        } catch (Exception exception) {
            System.out.println("Error in closing connection (signup) :  " + exception);
        }
    }
}
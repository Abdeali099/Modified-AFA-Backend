package com.afa.authorization.signup;

import com.afa.database.ConnectionToDatabase;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Connection;

@MultipartConfig
public class AFASignup extends HttpServlet {

    /*  Objects */
    Connection connection=null;

    @Override
    public void init(){

        /* Establishing connection : only once */
        try {
            connection= ConnectionToDatabase.getConnection();
        } catch (Exception e) {
            System.out.println("Error at signup connection establishment : " + e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)  {
        response.addHeader("Access-Control-Allow-Origin", "*");

        /* <-- Global variables --> */
        String userEmail,userPassword,userName,userPhone;

        try {

            /* <-- get details from request object --> */
            userName = request.getParameter("afa_username");
            userEmail = request.getParameter("afa_email");
            userPhone = request.getParameter("afa_phone");
            userPassword = request.getParameter("afa_password");



            response.setStatus(200);
            response.getWriter().println("Successfully Registered!!");

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

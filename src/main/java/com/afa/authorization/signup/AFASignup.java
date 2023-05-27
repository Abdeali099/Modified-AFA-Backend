package com.afa.authorization.signup;

import com.afa.database.ConnectionToDatabase;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.security.Key;
import java.sql.Connection;
import java.sql.PreparedStatement;

@MultipartConfig
public class AFASignup extends HttpServlet {

    /*  Objects */
    private Connection connection = null;
    private PreparedStatement psForSignup = null;
    private Key JWT_SECRETE_KEY = null;

    @Override
    public void init() {

        /* <-- Establishing connection and preparing statement : only once --> */
        try {

            connection = ConnectionToDatabase.getConnection();

            String SignupQuery = "INSERT INTO user_details VALUES (?,?,?,?) ";

            psForSignup = connection.prepareStatement(SignupQuery);

            JWT_SECRETE_KEY = ConnectionToDatabase.getSigningKey();

        } catch (Exception e) {
            System.out.println("Error at signup connection establishment : " + e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");

        /* <-- Global variables --> */
        String userEmail, userPassword, userName, userPhone;

        try {

            /* <-- get details from request object --> */
            userName = request.getParameter("afa_username");
            userEmail = request.getParameter("afa_email");
            userPhone = request.getParameter("afa_phone");
            userPassword = request.getParameter("afa_password");

            /* <-- Generating  Token (JWT) --> */

            JwtBuilder builder = Jwts.builder()
                    .setSubject(userEmail)
                    .signWith(JWT_SECRETE_KEY);

            String jwtToken = builder.compact();

            /* <-- Secure Password --> */

            /* Generate a salt for BCrypt */
            String salt = BCrypt.gensalt();

            /* Hash the password using BCrypt */
            String hashedPassword = BCrypt.hashpw(userPassword, salt);


            /* <-- Save data in database --> */

            psForSignup.setString(1, userName);
            psForSignup.setString(2, userEmail);
            psForSignup.setString(3, userPhone);
            psForSignup.setString(4, hashedPassword);

            int rowCount = psForSignup.executeUpdate();

            if (rowCount <= 0) {
                response.setStatus(500);
                response.getWriter().println("Server Error...");
                return;
            }

            response.setStatus(200);
            response.getWriter().println(jwtToken);

        } catch (Exception e) {
            System.out.print("\n => Error at Inside SignupServlet : " + e);
            response.setStatus(500);
            response.getWriter().println("Server Error...");
        }

    }

    @Override
    public void destroy() {

        try {
            if (connection != null) {
                connection.close();
            }

        } catch (Exception exception) {
            System.out.println("Error in closing connection (signup) :  " + exception);
        }
    }
}

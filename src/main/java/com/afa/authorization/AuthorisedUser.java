package com.afa.authorization;

import com.afa.database.ConnectionToDatabase;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.security.Key;
public class AuthorisedUser extends HttpServlet {

    /*  Objects */
    private Key JWT_SECRETE_KEY = null;

    public void init() {

        /* <-- Establishing connection and preparing statement : only once --> */
        try {
            JWT_SECRETE_KEY = ConnectionToDatabase.getSigningKey();
        } catch (Exception e) {
            System.out.println("Error at signup connection establishment : " + e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JSONObject responseJSON=null;

        try {

            responseJSON=new JSONObject();

            /* Getting user email */
            String userEmail= (String) request.getAttribute("userEmail");

            /* <-- Generating  Token (JWT) --> */

            JwtBuilder builder = Jwts.builder()
                    .setSubject(userEmail)
                    .signWith(JWT_SECRETE_KEY);

            String jwtToken = builder.compact();

            /* Setting Response */
            responseJSON.put("success",true);
            responseJSON.put("token",jwtToken);

            response.setStatus(200);
            response.getWriter().println(responseJSON);

        } catch (Exception e) {
            System.out.println("=> Error in generating JWT token : " + e);

            /* Setting Response */
            assert responseJSON != null;
            responseJSON.put("success",false);
            responseJSON.put("error","Error in generating token");

            response.setStatus(501);
            response.getWriter().println(responseJSON);
        }

    }

}

package com.afa.authorization;

import com.afa.database.ConnectionToDatabase;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;


public class AuthorisedFilter extends HttpFilter {

    /* Objects and variables */
    Connection connection = null;
    PreparedStatement psForEmailValidation = null,psForUserAddition = null;
    ResultSet rsForEmailValidation = null;

    public void init(FilterConfig filterConfig) {

        /* <-- Establishing connection prepare statement and : only once --> */
        try {
            connection = ConnectionToDatabase.getConnection();
            String queryForMailValidation = "SELECT email FROM user_details WHERE email = ? ;";
            String queryForUserAddition = "INSERT INTO user_details(email)  values(?) ;";
            psForEmailValidation = connection.prepareStatement(queryForMailValidation);
            psForUserAddition = connection.prepareStatement(queryForUserAddition);
        } catch (Exception e) {
            System.out.println("Error at signup filter connection establishment : " + e);
        }

    }
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {

        try {

            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.setContentType("application/json");

            HttpServletRequest request=(HttpServletRequest) servletRequest;
            String requestBody;

            /* Reading Request object and Saved as JSON */
            try (BufferedReader reader = request.getReader()) {
                requestBody = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }

            /* <-- Taking details from request --> */
            JSONObject jsonObject = new JSONObject(requestBody);

            String userEmail = jsonObject.getString("email");
            String joinedVia = jsonObject.getString("joinedVia");


            /* <-- Checking user is already exist or not : duplicate email case --> */
            boolean isUserAlreadyExist = isUserExist(userEmail);

            /* Error If this condition is true : return
            *
            * Error condition :
            *  Join through signup but email already exist
            *  Join through login but email not exist
            *  */
            JSONObject responseJson = new JSONObject();
            responseJson.put("success", false);
            responseJson.put("error", "authorization error");

            if ( (isUserAlreadyExist && joinedVia.equals("signup")) || (!isUserAlreadyExist && joinedVia.equals("login")) ) {
                response.setStatus(400);
                response.getWriter().println(responseJson);
                return;
            }

            /* Add new user email to database  */
            if(!isUserAlreadyExist && joinedVia.equals("signup")){

                int isUserAdded=addUser(userEmail);

                if (isUserAdded <= 0) {
                    response.setStatus(400);
                    response.getWriter().println(responseJson);
                    return;
                }

            }

            /* <-- validation successful then forward to servlet --> */
            servletRequest.setAttribute("userEmail",userEmail);
            filterChain.doFilter(servletRequest,servletResponse);

        } catch (Exception exception) {
            System.out.println("Error in filter of signup : " + exception);
        }

    }
    private boolean isUserExist(String userEmail) throws SQLException {

        try {

            psForEmailValidation.setString(1, userEmail);
            rsForEmailValidation = psForEmailValidation.executeQuery();

        } catch (Exception exception) {
            System.out.println("Error in checking , is user exist or not : " + exception);
        }

        return rsForEmailValidation.next();
    }
    private int addUser(String userEmail) {

        int affectedRow=0;

        try {

            psForUserAddition.setString(1, userEmail);
            affectedRow = psForUserAddition.executeUpdate();

        } catch (Exception exception) {
            System.out.println("Error in adding new user" + exception);
        }

        return affectedRow;
    }

    @Override
    public void destroy() {

        try {

            if (connection != null) {
                connection.close();
            }

            if (psForEmailValidation != null) {
                psForEmailValidation.close();
            }

            if (psForUserAddition != null) {
                psForUserAddition.close();
            }

            if (rsForEmailValidation != null) {
                rsForEmailValidation.close();
            }

        } catch (Exception exception) {
            System.out.println("Error in closing statement and result set ");
        }

    }

}

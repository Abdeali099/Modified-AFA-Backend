package com.afa.authorization.signup;

import com.afa.database.ConnectionToDatabase;
import com.afa.validation.DataValidation;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FilterSignup implements Filter {

    /* Objects and variables */
    Connection connection = null;
    PreparedStatement psForEmailValidation = null;
    ResultSet rsForEmailValidation = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        /* Establishing connection prepare statement and : only once */
        try {
            connection = ConnectionToDatabase.getConnection();
            String queryForMailValidation = "SELECT * FROM user_details WHERE email = ? ;";
            psForEmailValidation = connection.prepareStatement(queryForMailValidation);
        } catch (Exception e) {
            System.out.println("Error at signup filter connection establishment : " + e);
        }

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("Filter is called");

        try {

            HttpServletResponse response = (HttpServletResponse) servletResponse;

            /* Taking details from request */
            String userName = servletRequest.getParameter("afa_username");
            String userEmail = servletRequest.getParameter("afa_email");
            String userPhone = servletRequest.getParameter("afa_phone");
            String userPassword = servletRequest.getParameter("afa_password");

            System.out.println("\n => Name : " + userName +
                    "\n => EMail : " + userEmail +
                    "\n => Phone : " + userPhone +
                    "\n => Password : " + userPassword);

            /* Doing Data validation */
            boolean isDataValidate = DataValidation.validateSignup(userName, userEmail, userPhone, userPassword);

            if (!isDataValidate) {
                response.setStatus(400);
                response.getWriter().println(false);
                return;
            }

            /* <-- Checking user is already exist or not : duplicate email case --> */
            boolean isUserAlreadyExist = isUserExist(userEmail);

            if (isUserAlreadyExist) {
                response.setStatus(400);
                response.getWriter().println(false);
                return;
            }

            /* validation successful then forward to servlet */

            response.setStatus(200);
            response.getWriter().println(true);

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

    @Override
    public void destroy() {

        try {

            if (psForEmailValidation != null) {
                psForEmailValidation.close();
            }

            if (rsForEmailValidation != null) {
                rsForEmailValidation.close();
            }

        } catch (Exception exception) {
            System.out.println("Error in closing statement and result set ");
        }

    }

}

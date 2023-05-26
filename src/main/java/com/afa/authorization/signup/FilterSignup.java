package com.afa.authorization.signup;

import com.afa.validation.DataValidation;
import jakarta.servlet.*;

import java.io.IOException;

public class FilterSignup implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("Filter is called");

        try {

            /* Taking details from request */
            String userName = servletRequest.getParameter("afa_username");
            String userEmail = servletRequest.getParameter("afa_email");
            String userPhone = servletRequest.getParameter("afa_phone");
            String userPassword = servletRequest.getParameter("afa_password");

            System.out.println("\n => Name : " + userName +
                    "\n => EMail : " + userEmail +
                    "\n => Phone : " + userPhone +
                    "\n => Password : " + userPassword);

            /* Doing validation */
            boolean isValidate=DataValidation.validateSignup(userName,userEmail,userPhone,userPassword);

            /* validation successful then forward to servlet */

        } catch (Exception exception) {
            System.out.println("Error in filter of signup : " + exception);
        }

    }
}

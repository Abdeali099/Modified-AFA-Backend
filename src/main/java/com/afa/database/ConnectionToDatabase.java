package com.afa.database;

import java.sql.DriverManager;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionToDatabase {

    /* <---- static attribute ----> */
    private static Connection connection;
    private static String URL="",USERNAME="",PASSWORD="";
    private static final String PATH="E:\\Modified_AJT_Project\\AnyTImeFileAccess\\src\\assets\\credential.props";
    /* This have to make relative, but it is not working currently */

    /* <---- Only One time execute ----> */
    static {

        try {

            /* Fetching properties from props */
            FileInputStream fis = new FileInputStream(PATH);
            Properties prop = new Properties();
            prop.load(fis);

                  URL = prop.getProperty("url");
             USERNAME = prop.getProperty("userName");
             PASSWORD = prop.getProperty("password");

            /* Registering Driver class for 'mysql' */
            Class.forName("com.mysql.cj.jdbc.Driver");

            /* Initialize connection object */
            connection = null;

            /* Establishing Connection  */
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (Exception exception) {
                System.out.println("=> Error at Creating connection : " + exception);
            }

        } catch (Exception exception) {
            System.out.println(" => Error at static block Database connection : " + exception);
        }

    }

    /* <---- Whenever connection need call this via Class (ConnectionToDatabase.getConnection) ---->*/

    public static Connection getConnection() throws SQLException {

        try {
            if (connection!=null){
                return connection;
            }
        }

        catch (Exception exp) {
            System.out.println("=> Error at returning connection : " + exp);
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }

        /* If by chance connection is null */
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

}

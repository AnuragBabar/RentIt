package com.user;

import java.sql.*;

public class FindUser {
    public static void main(String[] args) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/rental_item_db", "root",
                "Anurag@11");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt
                .executeQuery("SELECT user_id, user_email FROM user WHERE user_email = 'rahul.sharma@gmail.com'");
        if (rs.next()) {
            System.out.println("USER_ID_FOUND: " + rs.getLong("user_id"));
        } else {
            System.out.println("USER_NOT_FOUND");
        }
    }
}

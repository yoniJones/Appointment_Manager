/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static utils.DBConnection.conn;

/**
 *
 * @author yonij
 */
public class SQLinteractivity {
    public static String query;
    public static Statement statement;
    public static ResultSet resultSet;
    
    // method that determine the type of query and executes it accordingly 
    public static void createQuery(String s){
        query = s;
        try{
            // statement object
            statement = conn.createStatement();
            
            if(s.toLowerCase().startsWith("select")){
                resultSet = statement.executeQuery(query);
            }if(s.toLowerCase().startsWith("delete") || s.toLowerCase().startsWith("update") || s.toLowerCase().startsWith("insert")){
                statement.executeUpdate(query);
                        
            }
                
        }catch(SQLException ex){
            System.out.println("Error --->A" + ex.getMessage());
        }
    }
    // retunrs the result set
    public static ResultSet getResultSet(){
        return resultSet;
    }
}

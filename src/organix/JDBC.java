
package organix;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;

interface JDBCInterface {
    public void getURL();
    public void getUser();
    public void getPassword();
}

public class JDBC implements JDBCInterface{
    
    private String url = "jdbc:mysql://localhost:3306/organix";
    private String user = "root";
    private String password = "root";
    public Connection conn;
    public Statement stmt;
    public ResultSet rs;
    
    JDBC(){
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        try{
            Class.forName("com.mysql.cj.jbc.Driver");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());

        }
        try{
            
            this.conn = DriverManager.getConnection(this.url, this.user, this.password);
            this.stmt = conn.createStatement();
            System.out.println("Connected to database");
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            System.out.println("Code execution done");
        }
        
    }
    
    public void getUser(){
        System.out.println(this.user);
    }
     
    public void getPassword(){
        System.out.println(this.password);
    }
    
    public void getURL(){
        System.out.println(this.url);
    }
   
    
}

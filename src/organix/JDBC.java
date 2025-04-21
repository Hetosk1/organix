
package organix;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC {
    
    private String url = "jdbc:mysql://localhost:3306/organix";
    private String user = "root";
    private String password = "drishti12345@#";
    
    public Connection conn;
    public Statement stmt;
    public ResultSet rs;
    
    JDBC(){
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
   
    
}

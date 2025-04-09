
package organix;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC {
    
    private String url = "jdbc:mysql://localhost:3306/organix";
    private String user = "root";
    private String password = "root";
    
    public Connection conn;
    public Statement stmt;
    public ResultSet rs;
    
    JDBC(){
        try{
            
            this.conn = DriverManager.getConnection(this.url, this.user, this.password);
            this.stmt = conn.createStatement();
            System.out.println("Connected to database");
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        
    }
   
    
}

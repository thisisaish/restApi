package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnect {
		
	private static Connection con = null;
	public static void initConnect() throws SQLException,ClassNotFoundException{
			
		Class.forName("com.mysql.cj.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/college1","me-aish","nopassword");
			
	}
		
	public static Connection getConn(){
		return con;
	}
	
}

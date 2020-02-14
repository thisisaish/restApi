package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DeptController {
	
	public JSONArray getDepartments(){
		JSONArray departments = new JSONArray();
		try{
			if(DbConnect.getConn() == null)
				DbConnect.initConnect();
			Connection con = DbConnect.getConn();
			PreparedStatement ps = con.prepareStatement("select * from departments");
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				JSONObject dpt = new JSONObject();
				
				dpt.put("deptName",rs.getString("name"));
				dpt.put("deptId",rs.getInt("dept_id"));				
				departments.add(dpt);
			}
			
		}catch(SQLException ex){
			System.out.println(ex);
		}catch(ClassNotFoundException ex){
			System.out.println(ex);
		}
		return departments;
		
	}
	
	public boolean addDept(String dept){
		try{
			if(DbConnect.getConn() == null)
				DbConnect.initConnect();
			Connection con = DbConnect.getConn();
			PreparedStatement ps = con.prepareStatement("insert into departments(name) values(?)");
			ps.setString(1,dept);
			return ps.execute();
			
		}catch(SQLException ex){
			System.out.println(ex);
		}catch(ClassNotFoundException ex){
			System.out.println(ex);
		}
		return false;
	}
	
}

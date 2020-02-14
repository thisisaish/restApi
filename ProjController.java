package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class ProjController {

	public JSONArray getProjects(){
		JSONArray projects = new JSONArray();
		try{
			if(DbConnect.getConn() == null)
				DbConnect.initConnect();
						
			Connection con = DbConnect.getConn();
			PreparedStatement ps = con.prepareStatement("select title,proj_id from projects");
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				JSONObject proj = new JSONObject();
				
				proj.put("title",rs.getString("title"));
				proj.put("id",rs.getInt("proj_id"));
				
				projects.add(proj);
			}
			
			rs.close();
			ps.close();
			
		}catch(SQLException ex){
			System.out.println(ex);
		}catch(ClassNotFoundException ex){
			System.out.println(ex);
		}
		return projects;
		
	}
	
	public JSONArray getProjects(int courseid){
		JSONArray projects = new JSONArray();
		try{
						
			Connection con = DbConnect.getConn();
			PreparedStatement ps = con.prepareStatement("select projects.title,projects.proj_id,status.proj_status from projects"
					+ " inner join students on students.proj_id = projects.proj_id"
					+ " inner join status on status.id = projects.status"
					+ " inner join courses_opted on students.stud_id = courses_opted.stud_id and courses_opted.course_id = ?");
			ps.setInt(1, courseid);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				JSONObject proj = new JSONObject();
				
				proj.put("title",rs.getString("projects.title"));
				proj.put("id",rs.getInt("projects.proj_id"));
				proj.put("status", rs.getString("status.proj_status"));
				projects.add(proj);
			}
			
			rs.close();
			ps.close();
			
		}catch(SQLException ex){
			System.out.println(ex);
		}
		return projects;
		
	}
	
	public void addProject(String title,int studId){
		try{
			if(DbConnect.getConn() == null){
				DbConnect.initConnect();
			}
			Connection con = DbConnect.getConn();
			
			PreparedStatement ps = con.prepareStatement("insert into projects(title,status,time) value(?,?,?)");
			ps.setString(1, title);
			ps.setInt(2, 1);
			ps.setLong(3, new Date().getTime());
			ps.executeUpdate();
			
			ps = con.prepareStatement("select proj_id from projects where title = ?");
			ps.setString(1,title);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				ps = con.prepareStatement("update students set proj_id = ? where stud_id = ?");
				ps.setInt(1, rs.getInt(1));
				ps.setInt(2, studId);
				
				ps.executeUpdate();
			}
			
			rs.close();
			ps.close();
			
		}catch(SQLException ex){
			System.out.println(ex);
		}catch(ClassNotFoundException ex){
			System.out.println(ex);
		}
	}
	
	public void updateProj(int id){
		try{
			if(DbConnect.getConn() == null){
				DbConnect.initConnect();
			}
			Connection con = DbConnect.getConn();
			
			PreparedStatement ps = con.prepareStatement("select status from projects where proj_id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				ps = con.prepareStatement("update projects set status = ?,time = ? where proj_id = ?");
				ps.setInt(1, rs.getInt("status") + 1);
				ps.setLong(2,new Date().getTime());
				ps.setInt(3, id);
				ps.executeUpdate();
			}
			
		}catch(ClassNotFoundException ex){
			System.out.println(ex);
		}catch(SQLException ex){
			System.out.println(ex);
		}
	}
	
	public void joinProj(int studId,int projId){
		try{
			if(DbConnect.getConn() == null)
				DbConnect.initConnect();
			Connection con = DbConnect.getConn();
			
			PreparedStatement ps = con.prepareStatement("update students set proj_id = ? where stud_id = ?"); 
			ps.setInt(1, projId);
			ps.setInt(2, studId);
			ps.executeUpdate();
			
			
		}catch(Exception ex){
			System.out.println(ex);
		}
	}
	
}

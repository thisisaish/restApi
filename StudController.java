package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class StudController {

	public void deleteStud(int id){
		try{
			if(DbConnect.getConn() == null){
				DbConnect.initConnect();
			}
			Connection con = DbConnect.getConn();
			
			PreparedStatement ps = con.prepareStatement("delete from students where stud_id = ?");
			ps.setInt(1, id);
			ps.executeUpdate();
			
			ps = con.prepareStatement("delete from people where id = ?");
			ps.setInt(1, id);
			ps.executeUpdate();
			
			ps.close();
		}catch(SQLException ex){
			System.out.println(ex);
		}catch(ClassNotFoundException ex){
			System.out.println(ex);
		}
	}
	
	public JSONArray getScoreCard(int id){
		JSONArray scores = new JSONArray();
		
		try{
			
			if(DbConnect.getConn() == null)
				DbConnect.initConnect();
			Connection con = DbConnect.getConn();
			PreparedStatement ps = con.prepareStatement("select courses.course_name,courses_opted.marks from courses_opted"
					+ " inner join courses on courses_opted.course_id = courses.course_id"
					+ " and courses_opted.stud_id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				JSONObject score = new JSONObject();
				score.put("subject", rs.getString(1));
				score.put("marks", rs.getInt(2));
				scores.add(score);
			}
			rs.close();
			ps.close();
			
		}catch(SQLException ex){
			System.out.println(ex);
		}catch(ClassNotFoundException ex){
			System.out.println(ex);
		}
		
		return scores;
	}
	
	public void addStudent(String name,String deptId){
		try{
			if(DbConnect.getConn() == null){
				DbConnect.initConnect();
			}
			Connection con = DbConnect.getConn();
			PreparedStatement ps = con.prepareStatement("insert into people(name,dept_id) values(?,?)");
			ps.setString(1, name);
			ps.setInt(2, Integer.parseInt(deptId));			
			ps.executeUpdate();
			
			ps = con.prepareStatement("select id from people where name = ?");
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				ps = con.prepareStatement("insert into students(stud_id) values(?)");
				ps.setInt(1, rs.getInt(1));
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
	
	public JSONArray getAllStudents(){
		
		JSONArray studs = new JSONArray();
		
		try{
			
			if(DbConnect.getConn() == null){
				DbConnect.initConnect();
			}
			
			Connection con = DbConnect.getConn();
			PreparedStatement ps = con.prepareStatement("select people.id,people.name,departments.name,projects.title from people" + 
					" inner join students on students.stud_id = people.id" + 
					" inner join departments on departments.dept_id = people.dept_id"+
					" inner join projects on projects.proj_id = students.proj_id");
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Student stud = new Student();
				stud.setId(rs.getInt("people.id"));
				stud.setName(rs.getString("people.name"));
				stud.setDept(rs.getString("departments.name"));
				stud.setTitle(rs.getString("projects.title"));
				
				studs.add(getJsonObject(stud));
			}
			
			rs.close();
			ps.close();			
			
		}catch(ClassNotFoundException ex){
			System.out.println(ex);
		}catch(SQLException ex){
			System.out.println(ex);
		}
		return studs;
	}
	
public JSONArray getAllStudents(int courseId){
		
		JSONArray studs = new JSONArray();
		
		try{
			
			if(DbConnect.getConn() == null){
				DbConnect.initConnect();
			}
			
			Connection con = DbConnect.getConn();
			PreparedStatement ps = con.prepareStatement("select people.id,people.name,departments.name,projects.title from people" + 
					" inner join students on students.stud_id = people.id" + 
					" inner join departments on departments.dept_id = people.dept_id"+
					" inner join projects on projects.proj_id = students.proj_id"+
					" inner join courses_opted on courses_opted.stud_id = students.stud_id and courses_opted.course_id = ?");
			ps.setInt(1, courseId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Student stud = new Student();
				stud.setId(rs.getInt("people.id"));
				stud.setName(rs.getString("people.name"));
				stud.setDept(rs.getString("departments.name"));
				stud.setTitle(rs.getString("projects.title"));
				
				studs.add(getJsonObject(stud));
			}
			
			rs.close();
			ps.close();			
			
		}catch(ClassNotFoundException ex){
			System.out.println(ex);
		}catch(SQLException ex){
			System.out.println(ex);
		}
		return studs;
	}
	
	public JSONArray getStudInfo(int id){
		Student stud = null;
		try{
			
			if(DbConnect.getConn() == null){
				DbConnect.initConnect();
			}
			
			Connection con = DbConnect.getConn();
			PreparedStatement ps = con.prepareStatement("select people.id,people.name,departments.name,projects.title from people" + 
					" inner join students on students.stud_id = people.id and students.stud_id = ?" + 
					" inner join departments on departments.dept_id = people.dept_id"+
					" inner join projects on projects.proj_id = students.proj_id");
			ps.setInt(1, id);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				stud = new Student();
				stud.setId(rs.getInt("people.id"));
				stud.setName(rs.getString("people.name"));
				stud.setDept(rs.getString("departments.name"));
				stud.setTitle(rs.getString("projects.title"));
			}
			
			rs.close();
			ps.close();				
			
		}catch(ClassNotFoundException ex){
			System.out.println(ex);
		}catch(SQLException ex){
			System.out.println(ex);
		}
		
		JSONArray arr = new JSONArray();
		arr.add(getJsonObject(stud));
		
		return arr;
	}
	
	public JSONObject getJsonObject(Student stud){
		JSONObject obj = new JSONObject();
		
		obj.put("id", stud.getId());
		obj.put("name", stud.getName());
		obj.put("department", stud.getDept());
		obj.put("title", stud.getTitle());
		
		return obj;
	}
	
}

package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CourseController {
	
	public JSONArray getCourses(){
		
		JSONArray courses = new JSONArray();
		
		try{
			if(DbConnect.getConn() == null)
				DbConnect.initConnect();
			Connection con = DbConnect.getConn();
			PreparedStatement ps = con.prepareStatement("select course_id,course_name from courses");
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				JSONObject course = new JSONObject();
				course.put("courseId",rs.getInt("course_id"));
				course.put("courseName",rs.getString("course_name"));
				
				courses.add(course);
			}
			rs.close();
			ps.close();
			
		}catch(Exception ex){
			System.out.println(ex);
		}
		
		return courses;
	}
	
	public JSONArray getAvailableCourses(int id){
		JSONArray courses = new JSONArray();
		
		try {
			if(DbConnect.getConn() == null)
				DbConnect.initConnect();
			Connection con = DbConnect.getConn();
			PreparedStatement ps = con.prepareStatement("select course_id,course_name from courses "
					+ "where course_id not in ("
					+ "select courses_opted.course_id from courses_opted "
					+ "inner join students on students.stud_id = courses_opted.stud_id "
					+ "and students.stud_id = ?)");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				JSONObject course = new JSONObject();
				course.put("courseId",rs.getInt("course_id"));
				course.put("courseName",rs.getString("course_name"));
				
				courses.add(course);
			}
			
			rs.close();
			ps.close();
		}catch(SQLException ex) {
			System.out.println(ex);
		}catch(ClassNotFoundException ex){
			System.out.println(ex);
		}
		
		return courses;
	}
	
	public void addCourse(Course course){
		try{
			if(DbConnect.getConn() == null)
				DbConnect.initConnect();
			Connection con = DbConnect.getConn();
			PreparedStatement ps = con.prepareStatement("insert into courses(course_name,fee) values(?,?)");
			ps.setString(1, course.getCourseName());
			ps.setFloat(2, course.getFee());
			ps.execute();
			
		}catch(SQLException ex){
			System.out.println(ex);
		}catch(ClassNotFoundException ex){
			System.out.println(ex);
		}
	}
	
	public void addCourse(int studId,int courseId){
		try{
			if(DbConnect.getConn() == null)
				DbConnect.initConnect();
			Connection con = DbConnect.getConn();
			PreparedStatement ps = con.prepareStatement("insert into courses_opted(course_id,stud_id) values(?,?)");
			ps.setInt(1, courseId);
			ps.setInt(2, studId);
			ps.execute();
			
		}catch(SQLException ex){
			System.out.println(ex);
		}catch(ClassNotFoundException ex){
			System.out.println(ex);
		}
	}
}

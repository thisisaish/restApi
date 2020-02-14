package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class StaffController {

	public JSONArray getStaffs(){
		
		JSONArray staffs = new JSONArray();
		try{
			if(DbConnect.getConn() == null){
				DbConnect.initConnect();
			}
			Connection con = DbConnect.getConn();
			PreparedStatement ps = con.prepareStatement("select people.id,people.name,departments.name,courses.course_name from people" + 
					" inner join staffs on staffs.staff_id = people.id" + 
					" inner join departments on departments.dept_id = people.dept_id"+
					" inner join courses on courses.course_id = staffs.course_id");
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				JSONObject staff = new JSONObject();
				
				staff.put("id", rs.getInt("people.id"));
				staff.put("name", rs.getString("people.name"));
				staff.put("department", rs.getString("departments.name"));
				staff.put("course", rs.getString("courses.course_name"));
				
				staffs.add(staff);
			}
			
			rs.close();
			ps.close();
			
		}catch(SQLException ex){
			System.out.println(ex);
		}catch(ClassNotFoundException ex){
			System.out.println(ex);
		}
		return staffs;
	}
	
	public void addStaff(Staff staff){
		try{
			if(DbConnect.getConn() == null){
				DbConnect.initConnect();
			}
			Connection con = DbConnect.getConn();
			PreparedStatement ps = con.prepareStatement("insert into people(name,dept_id) values(?,?)");
			ps.setString(1, staff.getName());
			ps.setInt(2, staff.getDeptId());			
			ps.executeUpdate();
			
			ps = con.prepareStatement("select id from people where name = ?");
			ps.setString(1, staff.getName());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				ps = con.prepareStatement("insert into staffs(staff_id,course_id) values(?,?)");
				ps.setInt(1, rs.getInt(1));
				ps.setInt(2, staff.getCourseId());
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
	
	public void updateMarks(int marks,int id,int courseId){
		try{
			if(DbConnect.getConn() == null){
				DbConnect.initConnect();
			}
			Connection con = DbConnect.getConn();
			PreparedStatement ps = con.prepareStatement("update courses_opted set marks = ? where stud_id = ? and course_id = ?");
			ps.setInt(1,marks);
			ps.setInt(2, id);
			ps.setInt(3, courseId);
			ps.executeUpdate();

			ps.close();
			
		}catch(SQLException ex){
			System.out.println(ex);
		}catch(ClassNotFoundException ex){
			System.out.println(ex);
		}
	}
	
}

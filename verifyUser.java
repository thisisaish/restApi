package app;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@WebServlet("/verify/*")

public class verifyUser extends HttpServlet{

	public void doGet(HttpServletRequest req,HttpServletResponse res)throws IOException{
		
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();
		String path = req.getPathInfo();
		
		JSONArray menu = new JSONArray();
		if(path.equals("/nopassword")){
			try{
				
				if(DbConnect.getConn() == null){
					DbConnect.initConnect();
				}
				Connection con = DbConnect.getConn();
				PreparedStatement ps = con.prepareStatement("select exists(select id from people inner join staffs "
						+ " on staffs.staff_id = people.id)");
				ResultSet rs = ps.executeQuery();
				if(rs.next() && rs.getInt(1) > 0){
					JSONObject op = new JSONObject();
					op.put("option", "view staffs");
					menu.add(op);
				}
				ps = con.prepareStatement("select exists(select id from people inner join students "
						+ " on students.stud_id = people.id)");
				rs = ps.executeQuery();
				if(rs.next() && rs.getInt(1) > 0){
					JSONObject op = new JSONObject();
					op.put("option", "view students");
					menu.add(op);
				}
				
				rs.close();
				ps.close();
				
				JSONObject op1 = new JSONObject();
				op1.put("option", "add student");
				menu.add(op1);
				
				JSONObject op2 = new JSONObject();
				op2.put("option", "add staff");
				menu.add(op2);
				
				JSONObject op3 = new JSONObject();
				op3.put("option", "add department");
				menu.add(op3);
				
				JSONObject op4 = new JSONObject();
				op4.put("option", "add course");
				menu.add(op4);
				
				JSONObject op5 = new JSONObject();
				op5.put("option", "logout");				
				menu.add(op5);
				
				out.print(menu);
					
			}catch(SQLException ex){
				System.out.println(ex);
			}catch(ClassNotFoundException ex){
				System.out.println(ex);
			}
			
		}
		
		else{
			int id = Integer.parseInt(path.substring(1));
			System.out.println(id);
			JSONObject obj = new JSONObject();
		
			try{
				if(DbConnect.getConn() == null){
					DbConnect.initConnect();
				}
				Connection con = DbConnect.getConn();
				PreparedStatement ps = con.prepareStatement("select exists(select id from people inner join staffs "
						+ " on staffs.staff_id = people.id and people.id = ?)");
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				if(rs.next() && rs.getInt(1) == 1){
					
					//prepare staff menu
					ps = con.prepareStatement("select people.name,staffs.course_id from people inner join "
							+ " staffs on staffs.staff_id = people.id and id = ?");
					ps.setInt(1, id);
					
					rs = ps.executeQuery();
					int courseId = 0;
					while(rs.next()){
						
						JSONObject stf = new JSONObject();
						stf.put("id", id);
						stf.put("name", rs.getString("people.name"));
						stf.put("courseid", rs.getInt("staffs.course_id"));
						menu.add(stf);
						courseId = rs.getInt("staffs.course_id");
					}
								
					int studCount = 0;
					int opt;
					boolean projectsExist = false;
					
					if(courseId != 0){
						
						ps = con.prepareStatement("select count(*) from courses_opted where course_id = ?");
						ps.setInt(1,courseId);
						rs = ps.executeQuery();
						while(rs.next())
							studCount = rs.getInt(1);
						if(studCount != 0) {
							JSONObject op = new JSONObject();
							op.put("option", "View students list");
							menu.add(op);	
						}
						
						ps = con.prepareStatement("select count(*) from students inner join courses_opted on courses_opted.stud_id = students.stud_id "+ 
									"where students.proj_id is not null and courses_opted.course_id = ?");
						ps.setInt(1,courseId);
						rs = ps.executeQuery();
						
						while(rs.next() && rs.getInt(1) > 0) {
							JSONObject op = new JSONObject();
							op.put("option", "View project details");
							menu.add(op);
							projectsExist = true;
						}
	
						if(studCount != 0){
							JSONObject op = new JSONObject();
							op.put("option", "update marks");
							menu.add(op);
						}
						if(!projectsExist && studCount == 0){
							JSONObject op = new JSONObject();
							op.put("option", "no options available");
							menu.add(op);
						}
					}else{
						JSONObject op = new JSONObject();
						op.put("option", "no options available");
						menu.add(op);
					}

					JSONObject op = new JSONObject();
					op.put("option", "logout");
					menu.add(op);
					
					out.println(menu);				
					System.out.println("staff");
					
					
				}else{
					ps = con.prepareStatement("select exists(select id from people inner join students "
							+ " on students.stud_id = people.id and people.id = ?)");
					ps.setInt(1, id);
					rs = ps.executeQuery();
					if(rs.next() && rs.getInt(1) == 1){
						
						//prepare student menu
						
						ps = con.prepareStatement("select people.name from people inner join "
								+ " students on students.stud_id = people.id and id = ?");
						ps.setInt(1, id);
						rs = ps.executeQuery();
						
						while(rs.next()){
							JSONObject stud = new JSONObject();
							stud.put("id", id);
							stud.put("name", rs.getString(1));
							menu.add(stud);
						}
								
						Project proj = null;
						boolean projFlag = false;
						
						ps = con.prepareStatement("select exists(select proj_id from students where stud_id = ? and proj_id is not null)");
						ps.setInt(1, id);
						rs = ps.executeQuery();
						
						while(rs.next() && rs.getInt(1) > 0){
							ps = con.prepareStatement("select proj_id from students where stud_id = ?");
							ps.setInt(1, id);
							rs = ps.executeQuery();
							
							while(rs.next()) {
								proj = new Project();
								proj.setId(rs.getInt("proj_id"));
								
								ps = con.prepareStatement("select title,status,time from projects where proj_id = ?");
								ps.setInt(1, proj.getId());
								rs = ps.executeQuery();
								
								while(rs.next()) {
									
									proj.setTitle(rs.getString("title"));
									proj.setStatus(rs.getInt("status"));
									proj.setTime(rs.getLong("time"));
									
									JSONObject project = new JSONObject();
									project.put("pid", proj.getId());
									project.put("title", proj.getTitle());
									project.put("status", proj.getStatus());
									project.put("time", proj.getTime());
									
									menu.add(project);
									
								}
								
								if(proj.getStatus() < 3){
									
									JSONObject op = new JSONObject();
									op.put("option", "update project");
									menu.add(op);
								}
							}
						}
						if(proj == null){
							
							JSONObject op = new JSONObject();
							op.put("option", "add project");
							menu.add(op);
							
							JSONObject op1 = new JSONObject();
							op1.put("option", "join existing project");
							menu.add(op1);
							
							projFlag = true;
							
						}
						
						int index = 0;
						ps = con.prepareStatement("select courses_opted.course_id from courses_opted inner join students"
								+ " on students.stud_id = courses_opted.stud_id and students.stud_id = ?");
						ps.setInt(1, id);
						rs = ps.executeQuery();
						while(rs.next()) {
							index++;				
						}

						
						if(index < 3){
							JSONObject op = new JSONObject();
							op.put("option", "select course");
							menu.add(op);
						}
						
						if(index > 0){
						
							JSONObject op = new JSONObject();
							op.put("option", "view score card");
							menu.add(op);	
							
						}
						
						JSONObject op = new JSONObject();
						op.put("option", "logout");
						menu.add(op);
						
						out.print(menu);
						
						System.out.println("student");
					}else{
						obj.put("userType", "not found");
						System.out.println("not found");
						out.print(obj);
					}
					
				}
				
				rs.close();
				ps.close();
				
			}catch(SQLException ex){
				System.out.println(ex);
			}catch(ClassNotFoundException ex){
				System.out.println(ex);
			}
		}
		
	}
	
}

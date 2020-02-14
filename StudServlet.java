package app;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@WebServlet("/students/*")

public class StudServlet extends HttpServlet{
	
	StudController studController = new StudController();
	
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException{
		String path = req.getPathInfo();
//		System.out.println(path);
		
		if(path == null){
			res.getWriter().print(studController.getAllStudents());
		}else{
		
			String[] reqPath = path.split("/");
//			for(int iter = 0;iter < reqPath.length;iter++)
//				System.out.println(iter+"->"+reqPath[iter]);
			
			res.setContentType("application/json");
			String param = req.getParameter("param");
			if(param == null){
				
				int id = Integer.parseInt(reqPath[reqPath.length - 1]);
				res.getWriter().print(studController.getStudInfo(id));
				
			}else if(param.equals("courseid")){
				
				int id = Integer.parseInt(reqPath[1]);
				res.getWriter().print(studController.getAllStudents(id));
				
			}else if(param.equals("student")){
				
				int id = Integer.parseInt(reqPath[1]);
				res.getWriter().print(studController.getScoreCard(id));
			}
		}
	}
	
	public void doPost(HttpServletRequest req,HttpServletResponse res){
		String path = req.getPathInfo();
		String[] reqPath = path.split("/");
		
		studController.addStudent(reqPath[reqPath.length - 2],reqPath[reqPath.length - 1]);
		
	}
	
	public void doDelete(HttpServletRequest req,HttpServletResponse res){
		String path = req.getPathInfo();
		String[] reqPath = path.split("/");
		
		studController.deleteStud(Integer.parseInt(reqPath[reqPath.length - 1]));
		
	}
	
	
}
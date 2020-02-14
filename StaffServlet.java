package app;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/staffs/*")

public class StaffServlet extends HttpServlet{
	
	StaffController staffController = new StaffController();
	
	public void doGet(HttpServletRequest req,HttpServletResponse res)throws IOException{
		
		res.setContentType("application/json");
		res.getWriter().print(staffController.getStaffs());
		
	}
	
	public void doPut(HttpServletRequest req,HttpServletResponse res){
		
		String[] info = req.getPathInfo().split("/");
//		for(int iter = 0;iter < info.length;iter++)
//			System.out.println(iter+"->"+info[iter]);
		staffController.updateMarks(Integer.parseInt(info[1]),Integer.parseInt(info[2]),Integer.parseInt(info[3]));
	}
	
	public void doPost(HttpServletRequest req,HttpServletResponse res){
		String path = req.getPathInfo();
		String[] reqPath = path.split("/");
//		for(int iter = 0;iter < reqPath.length;iter++)
//			System.out.println(iter+"->"+reqPath[iter]);
		Staff staff = new Staff();
		staff.setName(reqPath[1]);
		staff.setDeptId(Integer.parseInt(reqPath[2]));
		staff.setCourseId(Integer.parseInt(reqPath[3]));
		staffController.addStaff(staff);
	}

}

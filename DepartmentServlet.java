package app;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/departments/*")

public class DepartmentServlet extends HttpServlet{

	DeptController deptController = new DeptController();
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException{
		res.setContentType("application/json");
		res.getWriter().print(deptController.getDepartments());
	}
	
	public void doPost(HttpServletRequest req,HttpServletResponse res){
		String path = req.getPathInfo();
		deptController.addDept(path.substring(1));
	}
	
}

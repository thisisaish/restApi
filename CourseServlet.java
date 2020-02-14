package app;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/courses/*")

public class CourseServlet extends HttpServlet{

	CourseController courseController = new CourseController();
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException{
		res.setContentType("application/json");
		String info = req.getPathInfo();
		if(info == null)
			res.getWriter().print(courseController.getCourses());
		else{
			String[] param = info.split("/");
			System.out.println(param[1]);
			res.getWriter().print(courseController.getAvailableCourses(Integer.parseInt(param[1])));
			
		}
	}
	
	public void doPost(HttpServletRequest req,HttpServletResponse res){
		String[] courseInfo = req.getPathInfo().split("/");
//		for(int iter = 0;iter < courseInfo.length;iter++)
//			System.out.println(iter+"->"+courseInfo[iter]);
		String param = req.getParameter("param");
		if(param == null){			
			Course course = new Course();
			course.setName(courseInfo[1]);
			course.setFee(Float.parseFloat(courseInfo[2]));
			courseController.addCourse(course);
		}else{
			courseController.addCourse(Integer.parseInt(courseInfo[1]),Integer.parseInt(courseInfo[2]));
		}
	}
}

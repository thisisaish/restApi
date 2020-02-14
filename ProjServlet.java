package app;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/projects/*")

public class ProjServlet extends HttpServlet {

	ProjController projController = new ProjController();

	public void doGet(HttpServletRequest req,HttpServletResponse res)throws IOException{
		String path = req.getPathInfo();
		System.out.print(path);
		res.setContentType("application/json");
		if(path == null){
			res.getWriter().print(projController.getProjects());
		}else{
			
			String[] params = path.split("/");
			System.out.println(params[2]);
			res.getWriter().print(projController.getProjects(Integer.parseInt(params[2])));
			
		}
	}
	
	public void doPost(HttpServletRequest req,HttpServletResponse res){
		
		String[] info = req.getPathInfo().split("/");
//		for(int iter = 0;iter < info.length;iter++)
//			System.out.println(iter+"->"+info[iter]);
		projController.addProject(info[1], Integer.parseInt(info[2]));
		
	}
	
	public void doPut(HttpServletRequest req,HttpServletResponse res){
		
		String[] pathInfo = req.getPathInfo().split("/");
		if(pathInfo.length == 3){
			projController.joinProj(Integer.parseInt(pathInfo[1]), Integer.parseInt(pathInfo[2]));
		}else
			projController.updateProj(Integer.parseInt(pathInfo[0]));
		
	}
	
}

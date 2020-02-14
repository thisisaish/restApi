package app;

public class Person {
	private int id;
	private String name;
	private int deptId;
	private String dept;
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDeptId(int dept) {
		deptId = dept;
	}
	
	public void setDept(String dept) {
		this.dept = dept;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getDeptId() {
		return deptId;
	}
	
	public String getDept() {
		return dept;
	}
}

class Staff extends Person{
	private String course;
	private int courseId;
	public void setCourse(String course){
		this.course = course;
	}
	public String getCourse(){
		return course;
	}
	public void setCourseId(int courseId){
		this.courseId = courseId;
	}
	public int getCourseId(){
		return courseId;
	}
}

class Student extends Person{
    private String projectTitle;

    public void setTitle(String title){
        projectTitle = title;
    }
    public String getTitle(){
        return projectTitle;
    }

}

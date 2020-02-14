package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Course {
	private int courseId;
	private String courseName;
	private float fee;
	
	public void setId(int id) {
		courseId = id;
	}
	
	public void setName(String name) {
		courseName = name;
	}
	
	public void setFee(float fee) {
		this.fee = fee;
	}
	
	public int getId() {
		return courseId;
	}
	
	public String getCourseName() {
		return courseName;
	}
	
	public float getFee() {
		return fee;
	}
	
}

package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Project {
	private int id;
	private String title;
	private int status;
	private long time;
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	public int getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getStatus() {
		return status;
	}
	
	public long getTime() {
		return time;
	}
	
}

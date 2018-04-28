package cn.edu.dgut.model;

import java.sql.Timestamp;

/**
 * this is a bean for data 
 * 
 * Id will automatically increment			type -- int		11
 * user_id is the id in idform				type -- String 	16
 * stream 									type -- String	32
 * data										type -- String 	255
 * time										type -- timestamp
 * device									type -- String	16
 * 
 * 
 * @author Administrator
 *
 */
public class DataForm {
	
	private int id;
	private int user_id = 0;
	private String stream;
	private String data;
	private String device;
	private Timestamp time;
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getStream() {
		return stream;
	}
	public void setStream(String stream) {
		this.stream = stream;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public int getId() {
		return id;
	}
	public DataForm(int user_id, String stream, String data, String device) {
		super();
		this.user_id = user_id;
		this.stream = stream;
		this.data = data;
		this.device = device;
	}
	public DataForm(int user_id, String stream, String data, String device, Timestamp time) {
		super();
		this.user_id = user_id;
		this.stream = stream;
		this.data = data;
		this.device = device;
		this.time = time;
	}
	public DataForm(int user_id, String stream, String device) {
		super();
		this.user_id = user_id;
		this.stream = stream;
		this.device = device;
	}
	public DataForm(int user_id, String device) {
		super();
		this.user_id = user_id;
		this.device = device;
	}
	public DataForm() {
		super();
	}
	
	
	
	
}

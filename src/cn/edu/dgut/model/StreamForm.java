package cn.edu.dgut.model;

import java.sql.Timestamp;

/**
 * this is a bean for stream 
 * id										type -- int		11
 * user_id									type -- String	16
 * stream 									type -- String	32
 * unit										type -- String 	255
 * device									type -- String	16
 * createTime								type -- Timestamp	
 * 
 * @author Administrator
 *
 */
public class StreamForm {
	
	private int id;
	private int user_id = 0;
	private String stream;
	private String unit;
	private String device;
	private Timestamp createTime;
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
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public int getId() {
		return id;
	}
	public StreamForm(int user_id, String stream, String unit, String device, Timestamp createTime) {
		super();
		this.user_id = user_id;
		this.stream = stream;
		this.unit = unit;
		this.device = device;
		this.createTime = createTime;
	}
	public StreamForm(int user_id, String stream, String device) {
		super();
		this.user_id = user_id;
		this.stream = stream;
		this.device = device;
	}
	public StreamForm() {
		super();
	}
	public StreamForm(int user_id, String stream, String unit, String device) {
		super();
		this.user_id = user_id;
		this.stream = stream;
		this.unit = unit;
		this.device = device;
	}
	
	
	
}

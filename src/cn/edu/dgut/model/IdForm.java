package cn.edu.dgut.model;

import java.sql.Timestamp;

/**
 * this is a bean for idForm 
 * 
 * Id										type -- String	16
 * pwd 										type -- String	16
 * name										type -- String 	32
 * registerTime								type -- timestamp
 * 
 * 
 * @author Administrator
 *
 */
public class IdForm {
	
	private String id;
	private String pwd;
	private String name;
	private Timestamp registerTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(Timestamp registerTime) {
		this.registerTime = registerTime;
	}
	public IdForm() {
		super();
	}
	public IdForm(String id, String pwd, String name, Timestamp registerTime) {
		super();
		this.id = id;
		this.pwd = pwd;
		this.name = name;
		this.registerTime = registerTime;
	}
	public IdForm(String id, String pwd, String name) {
		super();
		this.id = id;
		this.pwd = pwd;
		this.name = name;
	}
	public IdForm(String id, String pwd) {
		super();
		this.id = id;
		this.pwd = pwd;
	}
	
	
	
	
	
	
}

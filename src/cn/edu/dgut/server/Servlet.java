package cn.edu.dgut.server;

import cn.edu.dgut.model.DataForm;

public class Servlet {
	
	
	private static Servlet instance = null;
	public static final String CRLF = "\r\n";
	public static final String BLANK = " ";
	
	private Servlet(){};
	
	static{
		if(instance == null){
			instance = new Servlet();
		}
	}
	
	public static Servlet getInstance() {
		return instance;
	}
	
	
	public void service(MicroRequest req,MicroResponse rep){
		switch(req.method){
			case "a":
				insert(req,rep);
				break;
			case "b":
				delete(req,rep);
				break;
			case "c":
				update(req,rep);
				break;
			case "d":
				select(req,rep);
				break;
			default:
				error(req,rep);
				req.isAlive = false;
				break;
		}
	}
	
	
	
	/**
	 * 查询数据
	 * 对应method d
	 */
	public void select(MicroRequest req, MicroResponse rep){
		if(req.isClient()){
			DataForm data = req.getClientRequestData();
			String msg;
			if(data != null){
				rep.append(req.method).append(BLANK).append(req.getId())
				.append(BLANK).append(req.getDevice())
				.append(BLANK).append(req.isAlive())
				.append(BLANK).append("OK").append(CRLF);

				
				msg = rep.createMessage(data);
				
				/************************************************/
				/**
				 * 修改返回值，修复当 msg.equals("") 时没有返回正文
				 * 也就是
				 * else{
				 *	rep.append("null");
				 *	}
				 * 不起作用
				 * verson 1.2 
				 * 	
				 */
				if(msg != null && !msg.trim().equals("")){
					
					
					rep.append(msg);
				}else{
					rep.append("null");
				}
				/************************************************/
				
				rep.pushToClient();
			}else{
				error(req,rep);
			}
		}else{
			error(req,rep);
		}
	}
	
	
	public void insert(MicroRequest req, MicroResponse rep){
		if(req.isClient()){
			rep.append(req.method).append(BLANK).append(req.getId())
			.append(BLANK).append(req.getDevice())
			.append(BLANK).append(req.isAlive())
			.append(BLANK).append("OK").append(CRLF);
			
			rep.pushToClient();
		}else{
			error(req,rep);
		}
		
	}
	
	
	
	public void error(MicroRequest req, MicroResponse rep){
		rep.clearContent();
		rep.append("error");
		rep.pushToClient();
		req.isAlive = false;
	}
	
	
	public void delete(MicroRequest req, MicroResponse rep){
		if(req.isClient()){
			rep.append(req.method).append(BLANK).append(req.getId())
			.append(BLANK).append(req.getDevice())
			.append(BLANK).append(req.isAlive())
			.append(BLANK).append("server error : can not delete any data").append(CRLF);
			
			rep.pushToClient();
		}else{
			error(req,rep);
		}
		
	}
	
	
	public void update(MicroRequest req, MicroResponse rep){
		if(req.isClient()){
			rep.append(req.method).append(BLANK).append(req.getId())
			.append(BLANK).append(req.getDevice())
			.append(BLANK).append(req.isAlive())
			.append(BLANK).append("server error : can not update any data").append(CRLF);
			
			rep.pushToClient();
		}else{
			error(req,rep);
		}
	}
	
}

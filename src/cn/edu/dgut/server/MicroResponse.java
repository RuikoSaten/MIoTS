package cn.edu.dgut.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import cn.edu.dgut.model.DataForm;
import cn.edu.dgut.util.CloseUtil;

public class MicroResponse {
	
	//响应报文
	protected StringBuilder content;
	
	//从 socket 获得的数据输出流
	protected BufferedWriter bw;
	
	/**
	 * 空构造负责初始化
	 */
	public MicroResponse() {
		content = new StringBuilder();
	}
	
	/**
	 *  获取输出流
	 *  
	 * @param client socket实例
	 */
	public MicroResponse(Socket client){
		this();
		try {
			bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {
			return;
		}
	}
	
	/**
	 *  获取输出流
	 *  
	 * @param os socket实例的输出流
	 */
	public MicroResponse(OutputStream os){
		this();
		bw = new BufferedWriter(new OutputStreamWriter(os));
	}
	
	
	public MicroResponse append(String msg){
		content.append(msg);
		return this;
	}
	

	public void pushToClient(){
		try {
			bw.append(content.toString());
			bw.flush();
			
			//测试代码
			System.out.println(content.toString());
			
			
			content.delete(0, content.length());
		} catch (IOException e) {			
			close();
		}
	}
	
	/**
	 * 关闭方法
	 */
	public void close(){
		CloseUtil.closeIO(bw);
	}
	
	
	/**
	 * 构造一个按参数格式存储的数据
	 * name={data:time}
	 * @param data
	 * @return
	 */
	public String createMessage(DataForm data){
		StringBuilder msg = new StringBuilder();
		if((data.getData() != null) && (data.getTime() != null)){
			msg.append(data.getStream()).append("={").append(data.getData()).append(":")
			
			
			/*******************************************************/
				/**
				 * 修改返回值形式，
				 * 		verson 1.2 返回时间类型为 long   
				 * 		verson 1.2 返回时间类型为 Date Format --  java.sql.Timestamp.toString()
				 * verson 1.2
				 */
			
			
			.append(data.getTime().toString()).append("}");
			
			/*******************************************************/
		}
		return msg.toString();
	}
	
	
	public void clearContent(){
		content.delete(0, content.length());
	}
	
	
	
}

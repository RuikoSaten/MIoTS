package cn.edu.dgut.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import cn.edu.dgut.model.DataForm;
import cn.edu.dgut.util.CloseUtil;

public class MicroResponse {
	
	//��Ӧ����
	protected StringBuilder content;
	
	//�� socket ��õ����������
	protected BufferedWriter bw;
	
	/**
	 * �չ��츺���ʼ��
	 */
	public MicroResponse() {
		content = new StringBuilder();
	}
	
	/**
	 *  ��ȡ�����
	 *  
	 * @param client socketʵ��
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
	 *  ��ȡ�����
	 *  
	 * @param os socketʵ���������
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
			
			//���Դ���
			System.out.println(content.toString());
			
			
			content.delete(0, content.length());
		} catch (IOException e) {			
			close();
		}
	}
	
	/**
	 * �رշ���
	 */
	public void close(){
		CloseUtil.closeIO(bw);
	}
	
	
	/**
	 * ����һ����������ʽ�洢������
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
				 * �޸ķ���ֵ��ʽ��
				 * 		verson 1.2 ����ʱ������Ϊ long   
				 * 		verson 1.2 ����ʱ������Ϊ Date Format --  java.sql.Timestamp.toString()
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

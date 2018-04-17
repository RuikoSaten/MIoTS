package cn.edu.dgut.server;

import java.io.IOException;
import java.net.Socket;
import cn.edu.dgut.util.CloseUtil;

public class MicroDispatcher implements Runnable{
	
	private Socket client;
	private MicroRequest req;
	private MicroResponse rep;
	
	public Socket getClient() {
		return client;
	}

	public MicroRequest getReq() {
		return req;
	}

	public MicroResponse getRep() {
		return rep;
	}

	public MicroDispatcher(Socket client){
		this.client = client;
		req = new MicroRequest(client);
		rep = new MicroResponse(client);
	}

	@Override
	public void run() {
		while(req.isAlive){
			
			
			/********************************************/
				/**
				 * ɾ������Ҫ���쳣�׳����µ� try catch
				 * 
				 * version 1.2
				 */
			if(req.parseMessage()){
				Servlet.getInstance().service(req, rep);
			}
			/********************************************/
			
			/********************************************/
			
				/**
				 * ���һ���Ƿ��ɿͻ��������رյ��ж�
				 * version 1.2
				 * since 1.2
				 */
			try {
				client.sendUrgentData(0xff);
			} catch (IOException e1) {
				//System.out.println("breaking");
				break;
			}
			//System.out.println("req.isAlive"+req.isAlive);
			/********************************************/
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.out.println("Thread.sleep error");
				break;
			}
		}
	//	System.out.println("�Ƴ�"+this);
		MicroServer.getInstance().reomveDispatcher(this);
		CloseUtil.closeSocket(client);
	}
	
}

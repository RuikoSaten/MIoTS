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
				 * 删除不必要的异常抛出导致的 try catch
				 * 
				 * version 1.2
				 */
			if(req.parseMessage()){
				Servlet.getInstance().service(req, rep);
			}
			/********************************************/
			
			/********************************************/
			
				/**
				 * 添加一个是否由客户端主动关闭的判断
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
	//	System.out.println("移除"+this);
		MicroServer.getInstance().reomveDispatcher(this);
		CloseUtil.closeSocket(client);
	}
	
}

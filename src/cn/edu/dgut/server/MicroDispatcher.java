package cn.edu.dgut.server;

import java.net.Socket;

import cn.edu.dgut.util.CloseUtil;

public class MicroDispatcher implements Runnable{
	
	private Socket client;
	private MicroRequest req;
	private MicroResponse rep;
	
	
	public MicroDispatcher(Socket client){
		this.client = client;
		req = new MicroRequest(client);
		rep = new MicroResponse(client);
	}

	@Override
	public void run() {
		int i = 0;
		while(req.isAlive){
			System.out.println("µÚ"+i+"´Î");
			if(req.parseMessage()){
				try {
					Servlet.getInstance().service(req, rep);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				System.out.println("Thread.sleep error");
				break;
			}
			i++;
		}
		CloseUtil.closeSocket(client);
		
	}
	
}

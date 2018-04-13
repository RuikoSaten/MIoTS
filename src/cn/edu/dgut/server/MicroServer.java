package cn.edu.dgut.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import cn.edu.dgut.util.CloseUtil;

public class MicroServer {
	private boolean isRunning = true;
	private List<Socket> clientList = null;
	
	private ServerSocket server;
	
	
	public MicroServer() {
		clientList = new LinkedList<Socket>();
	}
	
	
	public void start(){
		try {
			server = new ServerSocket(Integer.valueOf(ServerContext.getPort()));
			this.receive();
		} catch (Exception e) {
			System.out.println("·þÎñÆ÷Æô¶¯Ê§°Ü");
			System.exit(1);
		}
	}
	
	private void receive(){
		Socket client = null;
		while(isRunning){
			try {
				client = server.accept();
				ServerContext.serviceTimes++;
				clientList.add(client);
				new Thread(new MicroDispatcher(client)).start();
			} catch (IOException e) {
				stopServer();
			}
		}
	}
	
	public void stopServer(){
		isRunning = false;
		CloseUtil.closeSocket(server);
	}
	
	
}

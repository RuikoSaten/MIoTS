package cn.edu.dgut.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import cn.edu.dgut.util.CloseUtil;


/**
 * server 负责监听端口
 * @version	1.2 修改为单例模式
 * @since 1.0 
 * @author Administrator
 *
 */
public class MicroServer {
	private boolean isRunning = true;
	private List<Socket> clientList = null;
	
	private ServerSocket server;
	
	private static MicroServer instance = null;
	
	static{
		instance = new MicroServer();
	}
	
	public static MicroServer getInstance() {
		return instance;
	}
	
	
	private MicroServer() {
		clientList = new LinkedList<Socket>();
	}
	
	public List<Socket> getClientList() {
		return clientList;
	}
	
	
	public void start(){
		try {
			server = new ServerSocket(Integer.valueOf(ServerContext.getPort()));
			this.receive();
		} catch (Exception e) {
			System.out.println("服务器启动失败");
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

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
	private List<MicroDispatcher> dispatcherList;
	
	private ServerSocket server;
	
	private static MicroServer instance = null;
	
	private MicroServer(){
		dispatcherList = new LinkedList<MicroDispatcher>();		
	}
	
	
	static{
		instance = new MicroServer();
	}
	
	
	/*************************************************************/
	
		/**
		 * 将原有的 socketlist 改为 dispatcherList 
		 * 原因：
		 * 		1. 存 socket 是没有意义的
		 * 		2. 与客户端对接是在 request 和 response
		 * 		3. dispatcher 中保存有这两个类
		 *  所以应该操作 dispatcher 而不是 socket
		 *  
		 *  verson 1.2
		 */
	/**
	 * get dispatcher list
	 * @return
	 */
	public List<MicroDispatcher> getDispatcherList() {
		return dispatcherList;
	}
	
	public boolean addDispatcher(MicroDispatcher dispatcher){
		return dispatcherList.add(dispatcher);
	}
	
	public boolean reomveDispatcher(MicroDispatcher dispatcher){
		return dispatcherList.remove(dispatcher);
	}
	
	
	/*************************************************************/
	
	public static MicroServer getInstance() {
		return instance;
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
		MicroDispatcher dispatcher;
		while(isRunning){
			try {
				client = server.accept();
				ServerContext.serviceTimes++;
				dispatcher = new MicroDispatcher(client);
				new Thread(dispatcher).start();
				
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

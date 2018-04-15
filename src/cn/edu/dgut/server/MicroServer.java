package cn.edu.dgut.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import cn.edu.dgut.util.CloseUtil;


/**
 * server ��������˿�
 * @version	1.2 �޸�Ϊ����ģʽ
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
		 * ��ԭ�е� socketlist ��Ϊ dispatcherList 
		 * ԭ��
		 * 		1. �� socket ��û�������
		 * 		2. ��ͻ��˶Խ����� request �� response
		 * 		3. dispatcher �б�������������
		 *  ����Ӧ�ò��� dispatcher ������ socket
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
			System.out.println("����������ʧ��");
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

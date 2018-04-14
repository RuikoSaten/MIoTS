package cn.edu.dgut.server;

import java.io.IOException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;



/**
 * ����server��һЩ������
 * �˿�\�汾\\\\
 * @author Administrator
 *
 */
public class ServerContext {
	
	private static String host = null;
	private static String port = null;
	private static String serverName = null;
	private static String version = null;
	private static Properties properties = null;
	
	
	
	private static ServerContext instance = null;
	
	
	/**
	 * ÿ����һ�����Ӿ�����1
	 */
	public static int serviceTimes = 0;
	
	
	/**
	 * ÿ����һ�����ݵ������1
	 */
	private static int numberOfData;
	
	/**
	 * ÿ����һ����Ч���ݰ�������
	 */
	private static int numberOfMessage;
	
	public static synchronized void IncremeNtnumberOfData(int count){
		if(count < 0)return ;
		numberOfData += count;
	}
	
	
	
	
	public static synchronized void IncrementNumberOfMessage(){
		numberOfMessage++;
	}
	
	
	
	public static ServerContext getInstance() {
		return instance;			
	}
	
	static{
		if(instance == null){
			instance = new ServerContext();
		}
		numberOfMessage = 0;
		numberOfData = 0;
		properties = new Properties();
		try {
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("serverContext.properties"));
			host = properties.getProperty("Host");
			port = properties.getProperty("Port");
			serverName = properties.getProperty("ServerName");
			version = properties.getProperty("Version");
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	public static String getHost() {
		return host;
	}
	public static String getPort() {
		return port;
	}
	public static String getServerName() {
		return serverName;
	}
	public static String getVersion() {
		return version;
	}
	public static Properties getProperties() {
		return properties;
	}
	
	
	/**
	 * GP2 engine! GP2 engine! It feels like it��s GP2. 
	 * 							--Fernando Alonso
	 * 	ServerContextEngine 
	 * 						1.��Ҫ��¼��־��������
	 * 							1) ������־
	 * 							2) �쳣��־
	 * 							3) ������־
	 * @author Administrator
	 *
	 */
	private class GP2Engine{
		Timer  timer =new Timer(true);   
		RunLogsTask runLogsTask = new RunLogsTask();
		//timer.schedule(task,0,30*60*1000);
		
		public boolean startRunLogsTask(){
			timer.schedule(runLogsTask, 0, 24*60*60*1000);
			return runLogsTask.taskRunning = true;
		}
		
		public boolean stopRunLogsTask(){
			runLogsTask.taskRunning = false;
			return runLogsTask.cancel();
		}
		
		/**
		 * ������־��
		 * 		�����¼�д�����ݿ�
		 * 		��Ҫ��¼ 
		 * 				1. ���ݰ�����
		 * 				2. ���ݵ�����
		 * 				3. �� method ����
		 * 				4. ��������
		 * 				
		 * @author Administrator
		 *
		 */
		private class RunLogsTask extends TimerTask{
			boolean taskRunning;
			
			@Override
			public void run() {
				//����־д��sql
				if(taskRunning){
					
				}
			}
			
		}
		
		
		
	}
	
	
	
	
}

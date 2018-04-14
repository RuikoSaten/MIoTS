package cn.edu.dgut.server;

import java.io.IOException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;



/**
 * 关于server的一些上下文
 * 端口\版本\\\\
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
	 * 每建立一个连接就自增1
	 */
	public static int serviceTimes = 0;
	
	
	/**
	 * 每解析一个数据点就自增1
	 */
	private static int numberOfData;
	
	/**
	 * 每解析一个有效数据包就自增
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
	 * GP2 engine! GP2 engine! It feels like it‘s GP2. 
	 * 							--Fernando Alonso
	 * 	ServerContextEngine 
	 * 						1.需要记录日志，包括：
	 * 							1) 运行日志
	 * 							2) 异常日志
	 * 							3) 错误日志
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
		 * 运行日志类
		 * 		负责将事件写进数据库
		 * 		主要记录 
		 * 				1. 数据包数量
		 * 				2. 数据点数量
		 * 				3. 各 method 数量
		 * 				4. 连接数量
		 * 				
		 * @author Administrator
		 *
		 */
		private class RunLogsTask extends TimerTask{
			boolean taskRunning;
			
			@Override
			public void run() {
				//将日志写入sql
				if(taskRunning){
					
				}
			}
			
		}
		
		
		
	}
	
	
	
	
}

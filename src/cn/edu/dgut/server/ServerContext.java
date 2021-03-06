package cn.edu.dgut.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
	public class GP2Engine{
		Timer  timer =new Timer(true);   
		RunLogsTask runLogsTask = new RunLogsTask();
		ErrorLogsTask errorLogsTask = new ErrorLogsTask();
		ExceptionLogsTask exceptionLogsTask = new ExceptionLogsTask();
		
		
		/**
		 * 每天运行一次 运行日志类的 run 方法。
		 * @return
		 */
		public boolean startRunLogsTask(){
			runLogsTask.taskRunning = true;
			timer.schedule(runLogsTask, 0, 24*60*60*1000);
			return runLogsTask.taskRunning;
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
				if(taskRunning){
					//将日志写入sql
					
				}
			}
			
		}
		
		
		/**
		 * 每 10 分钟运行一次异常类的  run 方法
		 * @return
		 */
		public boolean startExceptionLogsTask(){
			exceptionLogsTask.taskRunning = true;
			timer.schedule(exceptionLogsTask, 0, 10*60*1000);
			return exceptionLogsTask.taskRunning;
		}
		
		
		/**
		 * 停止运行异常任务
		 * @return
		 */
		public boolean stopExceptionLogsTask(){
			exceptionLogsTask.taskRunning = false;
			return exceptionLogsTask.cancel();
		}
		
		
		/**
		 * 向 exceptionList 写入异常信息
		 * @param exceptionInfo
		 */
		public void throwsException(String exceptionInfo){
			exceptionLogsTask.exceptionList.add(exceptionInfo);
		}
		
		/**
		 * 向 exceptionList 写入异常信息列表
		 * @param exceptionInfo
		 */
		public void throwsException(List<String> exceptionInfoList){
			exceptionLogsTask.exceptionList.addAll(exceptionInfoList);
		}
		
		
		/**
		 * 异常日志类
		 * 		负责将异常信息写入数据库
		 * 		主要记录
		 * 				1. 空指针异常
		 * 				2. 数据包异常
		 * 				
		 * @author Administrator
		 *
		 */
		private class ExceptionLogsTask extends TimerTask {
			List<String> exceptionList = new ArrayList<String>();
			boolean taskRunning;
			
			
			@Override
			public void run() {
				if(taskRunning){
					//遍历 list 将日志写入 sql 
					
					
					
					
					
					exceptionList.clear();
				}
			}
			
		}
		
		
		
		/**
		 * 每 10 分钟运行一次错误类的  run 方法
		 * @return
		 */
		public boolean startErrorLogsTask(){
			errorLogsTask.taskRunning = true;
			timer.schedule(errorLogsTask, 0, 10*60*1000);
			return errorLogsTask.taskRunning;
		}
		
		
		/**
		 * 停止运行错误任务
		 * @return
		 */
		public boolean stopErrorLogsTask(){
			errorLogsTask.taskRunning = false;
			return errorLogsTask.cancel();
		}
		
		/**
		 * 添加错误信息
		 * @param errorInfo
		 */
		public void addErrorInfo(String errorInfo){
			errorLogsTask.errorList.add(errorInfo);
		}
		
		
		/**
		 * 添加错误信息列表
		 * @param errorInfoList
		 */
		public void addErrorInfo(List<String> errorInfoList){
			errorLogsTask.errorList.addAll(errorInfoList);
		}
		
		
		
		
		/**
		 * 错误日志类
		 * 		负责将错误信息写入数据库
		 * 		主要记录
		 * 				1. 用户名、密码匹配错误
		 * 				2. ....
		 * @author Administrator
		 *
		 */
		private class ErrorLogsTask extends TimerTask{
			List<String>errorList = new ArrayList<String>();
			boolean taskRunning; 
			
			@Override
			public void run() {
				if(taskRunning){
					
					
					
					errorList.clear();
				}
				
			}
		}
		
	}
	
	
	
	
}

package cn.edu.dgut.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
	public class GP2Engine{
		Timer  timer =new Timer(true);   
		RunLogsTask runLogsTask = new RunLogsTask();
		ErrorLogsTask errorLogsTask = new ErrorLogsTask();
		ExceptionLogsTask exceptionLogsTask = new ExceptionLogsTask();
		
		
		/**
		 * ÿ������һ�� ������־��� run ������
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
				if(taskRunning){
					//����־д��sql
					
				}
			}
			
		}
		
		
		/**
		 * ÿ 10 ��������һ���쳣���  run ����
		 * @return
		 */
		public boolean startExceptionLogsTask(){
			exceptionLogsTask.taskRunning = true;
			timer.schedule(exceptionLogsTask, 0, 10*60*1000);
			return exceptionLogsTask.taskRunning;
		}
		
		
		/**
		 * ֹͣ�����쳣����
		 * @return
		 */
		public boolean stopExceptionLogsTask(){
			exceptionLogsTask.taskRunning = false;
			return exceptionLogsTask.cancel();
		}
		
		
		/**
		 * �� exceptionList д���쳣��Ϣ
		 * @param exceptionInfo
		 */
		public void throwsException(String exceptionInfo){
			exceptionLogsTask.exceptionList.add(exceptionInfo);
		}
		
		/**
		 * �� exceptionList д���쳣��Ϣ�б�
		 * @param exceptionInfo
		 */
		public void throwsException(List<String> exceptionInfoList){
			exceptionLogsTask.exceptionList.addAll(exceptionInfoList);
		}
		
		
		/**
		 * �쳣��־��
		 * 		�����쳣��Ϣд�����ݿ�
		 * 		��Ҫ��¼
		 * 				1. ��ָ���쳣
		 * 				2. ���ݰ��쳣
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
					//���� list ����־д�� sql 
					
					
					
					
					
					exceptionList.clear();
				}
			}
			
		}
		
		
		
		/**
		 * ÿ 10 ��������һ�δ������  run ����
		 * @return
		 */
		public boolean startErrorLogsTask(){
			errorLogsTask.taskRunning = true;
			timer.schedule(errorLogsTask, 0, 10*60*1000);
			return errorLogsTask.taskRunning;
		}
		
		
		/**
		 * ֹͣ���д�������
		 * @return
		 */
		public boolean stopErrorLogsTask(){
			errorLogsTask.taskRunning = false;
			return errorLogsTask.cancel();
		}
		
		/**
		 * ��Ӵ�����Ϣ
		 * @param errorInfo
		 */
		public void addErrorInfo(String errorInfo){
			errorLogsTask.errorList.add(errorInfo);
		}
		
		
		/**
		 * ��Ӵ�����Ϣ�б�
		 * @param errorInfoList
		 */
		public void addErrorInfo(List<String> errorInfoList){
			errorLogsTask.errorList.addAll(errorInfoList);
		}
		
		
		
		
		/**
		 * ������־��
		 * 		���𽫴�����Ϣд�����ݿ�
		 * 		��Ҫ��¼
		 * 				1. �û���������ƥ�����
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

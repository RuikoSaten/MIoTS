package cn.edu.dgut.server;

import java.io.IOException;
import java.util.Properties;



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
	 * ÿ����һ����Ч���ݰ�������1
	 */
	private static int numberOfMessage;
	
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
	
	
	
}

package cn.edu.dgut.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Properties;

public class JDBCUtil {
	
	private static final String profile = "database.properties";
	
	private static String driverClass = null;
	private static String host = null;
	private static String database = null;
	private static String userName = null;
	private static String userPassword = null; 
	private static Properties properties = null;
	
	static{
		properties = new Properties();
		try {
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(profile));
			driverClass = properties.getProperty("Driver");
			host = properties.getProperty("Host");
			database = properties.getProperty("DataBase");
			userName = properties.getProperty("UserName");
			userPassword = properties.getProperty("UserPwd");
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	
	public static String getDriverClass() {
		return driverClass;
	}

	public static void setDriverClass(String driverClass) {
		JDBCUtil.driverClass = driverClass;
	}

	public static String getHost() {
		return host;
	}

	public static void setHost(String host) {
		JDBCUtil.host = host;
	}

	public static String getDatabase() {
		return database;
	}

	public static void setDatabase(String database) {
		JDBCUtil.database = database;
	}

	public static String getUserName() {
		return userName;
	}

	public static void setUserName(String userName) {
		JDBCUtil.userName = userName;
	}

	public static String getUserPassword() {
		return userPassword;
	}

	public static void setUserPassword(String userPassword) {
		JDBCUtil.userPassword = userPassword;
	}

	public static Connection getMySqlConnection(){
		try {
			Class.forName(driverClass);
			return DriverManager.getConnection(host+database,userName,userPassword);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static Connection getMySqlConnection(String database){
		if(database == null || database.trim().equals("")){
			return null;
		}
		try {
			Class.forName(driverClass);
			return DriverManager.getConnection(host+database,userName,userPassword);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static Connection getMySqlConnection(String host,String database){
		if(host == null || host.trim().equals("")){
			return null;
		}
		if(database == null || database.trim().equals("")){
			return null;
		}
		try {
			Class.forName(driverClass);
			return DriverManager.getConnection(host+database,userName,userPassword);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static Connection getMySqlConnection(String host,String database,String userName,String userPassword){
		if(database == null || database.trim().equals("")){
			return null;
		}
		if(host == null || host.trim().equals("")){
			return null;
		}
		if(userName == null || userName.trim().equals("")){
			return null;
		}
		if(userPassword == null || userPassword.trim().equals("")){
			return null;
		}
		
		try {
			Class.forName(driverClass);
			return DriverManager.getConnection(host+database,userName,userPassword);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static void close(ResultSet rs,Statement ps,Connection conn){
		try {
			if(rs != null){
				rs.close();				
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		try {
			if(ps != null){
				ps.close();				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(conn != null){
				conn.close();				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void close(ResultSet rs){
		try {
			if(rs != null){
				rs.close();				
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}		
	}
	
	public static void close(Statement ps){
		try {
			if(ps != null){
				ps.close();				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public static void close(Connection conn){
		try {
			if(conn != null){
				conn.close();				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public static void close(ResultSet rs,Statement ps){
		try {
			if(rs != null){
				rs.close();				
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		try {
			if(ps != null){
				ps.close();				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static Timestamp now(){
		return new Timestamp(System.currentTimeMillis());
	}
	
	
}

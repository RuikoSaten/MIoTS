package cn.edu.dgut.dao;

import java.sql.Connection;

import cn.edu.dgut.util.JDBCUtil;

/**
 * sql ������,���𿪻����� sql �Լ�ͳ�Ʋ�������(δʵ��)
 * @author Administrator
 *
 */
public class SQLContext {
	
	private SQLContext(){
		
	}
	
	private static SQLContext instance = null;
	public static Connection connection = null;
	
	
	
	public static SQLContext getInstance() {
		return instance;
	}
	
	static{
		instance = new SQLContext();
		connection = JDBCUtil.getMySqlConnection();
	}
	
	
	
	
	
	
	
}

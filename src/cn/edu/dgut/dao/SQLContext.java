package cn.edu.dgut.dao;

import java.sql.Connection;

import cn.edu.dgut.util.JDBCUtil;

/**
 * sql 上下文,负责开机连接 sql 以及统计操作数量(未实现)
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

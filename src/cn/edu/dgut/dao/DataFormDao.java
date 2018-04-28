package cn.edu.dgut.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.edu.dgut.model.DataForm;
import cn.edu.dgut.util.JDBCUtil;

/**
 * 
 * @author Administrator
 *
 */
public class DataFormDao {

	private static DataFormDao instance = null; 
	
	private DataFormDao(){};
	
	static{
		if(instance == null){
			instance = new DataFormDao();
		}
	}
	
	public static DataFormDao getInstance() {
		return instance;
	}
	
	
	/**
	 * insert a single data into dataForm
	 * 除了 time 以外的数据都不能是null
	 * @param data bean 
	 * @return
	 */
	public static int insert(DataForm data) {
		if(data == null){
			return 0;
		}
		if(data.getData() == null || data.getDevice() == null ||
				data.getStream() == null || data.getUser_id() == 0){
			return 0;
		}
		if(data.getTime() == null){
			data.setTime(JDBCUtil.now());
		}
		String sql = "insert into dataForm (user_id,stream,data,device,time) values(?,?,?,?,?);";
		PreparedStatement ps = null;
		int i = 0;
		try {
			ps = SQLContext.connection.prepareStatement(sql);
			ps.setInt(1, data.getUser_id());
			ps.setString(2, data.getStream());
			ps.setString(3, data.getData());
			ps.setString(4, data.getDevice());
			ps.setTimestamp(5, data.getTime());
			
			i = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtil.close(ps);
		}
		return i;
	}


	/**
	 * select 某 user_id 下( device stream ) 的 data and time
	 * @param data
	 * @return
	 */
	public static int select(DataForm data){
		if(data == null){
			return 0;
		}
		if(data.getDevice() == null || data.getStream() == null || data.getUser_id() == 0){
			return 0;
		}
		int i = 0;
		String sql = "select data,time from dataForm where user_id=? and stream=? and device=? order by time desc limit 1;";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = SQLContext.connection.prepareStatement(sql);
			ps.setInt(1, data.getUser_id());
			ps.setString(2, data.getStream());
			ps.setString(3, data.getDevice());
			rs = ps.executeQuery();
			
			if(rs.next()){
				data.setData(rs.getString(1));
				data.setTime(rs.getTimestamp(2));
				i++;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCUtil.close(rs,ps);
		}
		return i;
	}
	
//	/**
//	 * 测试刚写的两个方法
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		DataForm data = new DataForm("19333","temp","36","002");
//		//DataFormDao.getInstance().insert(data);
//		DataFormDao.getInstance().select(data);
//		System.out.println(data.getData());
//		System.out.println(data.getTime());
//	}
	
	
	
	
	
	
	
	
	
}

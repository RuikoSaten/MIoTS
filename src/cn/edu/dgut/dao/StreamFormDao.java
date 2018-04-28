package cn.edu.dgut.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.edu.dgut.model.StreamForm;
import cn.edu.dgut.util.JDBCUtil;

/**
 * 
 * @author Administrator
 *
 */
public class StreamFormDao {
	private static StreamFormDao instance = null; 
	
	private StreamFormDao(){};
	
	static{
		if(instance == null){
			instance = new StreamFormDao();
		}
	}
	
	public static StreamFormDao getInstance() {
		return instance;
	}
	
	/**
	 * 增加一个流
	 * @param streamForm
	 * @return
	 */
	public static int insert(StreamForm streamForm){
		if(streamForm == null){
			return 0;
		}
		if(streamForm.getUser_id() == 0 ||
				streamForm.getDevice() == null || 
				streamForm.getStream() == null){
			return 0;
		}
		if(streamForm.getCreateTime() == null){
			streamForm.setCreateTime(JDBCUtil.now());
		}
		if(streamForm.getUnit() == null){
			streamForm.setUnit("null");
		}
		
		String sql = "insert into StreamForm (user_id,stream,unit,device,createTime) values(?,?,?,?,?);";
		int i = 0;
		PreparedStatement ps = null;
		try {
			ps = SQLContext.connection.prepareStatement(sql);
			ps.setInt(1, streamForm.getUser_id());
			ps.setString(2, streamForm.getStream());
			ps.setString(3, streamForm.getUnit());
			ps.setString(4, streamForm.getDevice());
			ps.setTimestamp(5, streamForm.getCreateTime());
			
			i = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			JDBCUtil.close(ps);
		}
		return i;
	}
	
	/**
	 * 查询流是否存在，存在则查出创建时间以及单位
	 * @param streamForm
	 * @return 
	 */
	public static int select(StreamForm streamForm){
		if(streamForm == null){
			return 0;
		}
		if(streamForm.getUser_id() == 0 ||
				streamForm.getDevice() == null || 
				streamForm.getStream() == null){
			return 0;
		}
		
		String sql = "select createTime,unit from StreamForm where user_id=? and device=? and stream=?;";
		int i = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = SQLContext.connection.prepareStatement(sql);
			ps.setInt(1, streamForm.getUser_id());
			ps.setString(2, streamForm.getDevice());
			ps.setString(3, streamForm.getStream());
			
			rs = ps.executeQuery();
			
			if(rs.next()){
				streamForm.setCreateTime(rs.getTimestamp(1));
				streamForm.setUnit(rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			JDBCUtil.close(rs,ps);
		}
		return i;
	}
	
	
//	
//	/**
//	 * 测试
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		StreamForm streamForm = new StreamForm("19333","humi","001");
//		//StreamFormDao.insert(streamForm);
//
//		StreamFormDao.select(streamForm);
//		System.out.println(streamForm.getUnit());
//		System.out.println(streamForm.getCreateTime());
//	}
//	
}

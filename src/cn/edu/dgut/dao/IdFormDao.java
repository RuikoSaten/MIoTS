package cn.edu.dgut.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.edu.dgut.model.IdForm;
import cn.edu.dgut.util.JDBCUtil;

/**
 * ���Ǹ�����id�����
 * Ϊ�˰�ȫ��ֻʵ�ֲ�ѯ����
 * @author Administrator
 *
 */
public class IdFormDao {
	
	private static IdFormDao instance = null;
	
	private IdFormDao(){}
	
	static{
		instance = new IdFormDao();
	}
	
	public static IdFormDao getInstance() {
		return instance;
	}
	
	
	/**
	 * �����ʺ������ѯע��ʱ����û��� �����Է���ֵ�ж��Ƿ�ƥ��id and pwd��
	 * @param idForm
	 * @return
	 */
	public static int select(IdForm idForm){
		if(idForm == null){
			return 0;
		}
		if(idForm.getId() == 0 || idForm.getPwd() == null){
			return 0;
		}
		
		String sql = "select name,registerTime from IdForm where id=? and pwd=?";
		int i = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = SQLContext.connection.prepareStatement(sql);
			ps.setInt(1, idForm.getId());
			ps.setString(2, idForm.getPwd());
			rs = ps.executeQuery();
			
			if(rs.next()){
				idForm.setName(rs.getString(1));
				idForm.setRegisterTime(rs.getTimestamp(2));
				i++;	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtil.close(rs,ps);
		}
		return i;
	}

	
	
	
//	
//	public static void main(String[] args) {
//		IdForm id = new IdForm("1933","19334");
//		if(IdFormDao.getInstance().select(id) == 1){
//			System.out.println(id.getName());
//			System.out.println(id.getRegisterTime());
//		}else{
//			System.out.println("id and pwd is not matcher");
//		}
//	}

}

package cn.edu.dgut.util;

public class StringUtil {
	
	private StringUtil(){}
	
	
	/**
	 * ����ַ���stringΪ�ջ򲻴����򷵻�false
	 * @param string
	 * @return
	 */
	public static final boolean checkStringTrim(String string){
		if(null == string ||(string = string.trim()).equals("")){
			return true;
		}
		return false;
	}
	
	public static final boolean checkString(String string){
		if(null == string || string.equals("")){
			return true;
		}
		return false;
	}
	
}

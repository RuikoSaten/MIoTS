package cn.edu.dgut.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.dgut.dao.DataFormDao;
import cn.edu.dgut.dao.IdFormDao;
import cn.edu.dgut.model.DataForm;
import cn.edu.dgut.model.IdForm;
import cn.edu.dgut.util.JDBCUtil;
import cn.edu.dgut.util.StringUtil;

/**
 * ���Ǹ������豸���͹��������ݵ���
 * ��ʱ�����ʽΪ
 * method+blank+id+blank+pwd+blank+device+blank+"alive"+blank+"len="+length+crlf+crlf+message
 * 
 * message ��ʽ��
 * name1={value1:time1}&name2={value2:time2}&name1={value3:time3}
 * 
 * 	��ʽ �ʺ� ���� �豸 �Ƿ񱣳����� ���� 
 * "a 19333 19334 1 T len=xx\r\n\r\nmessage"	//��������
 * "a 19333 19334 1 F len=xx\r\n\r\nmessage"	//������ɹر�����
 * time1 �� Unix timestamp 
 * �� timestamp == 0 ʱ�������Զ�������յ���һ֡���ݵ�ʱ��
 * ���������ظ�����������һ���ϴ�����������Ķ�������
 * �ݶ� һ֡����󳤶�Ϊ 256 �ֽ�
 * @author siyuan
 * @version 1.1
 * 1.1�汾������
 * 				�޸�������֪���⡣
 * 				���������޴� 2048 ��Ϊ 256�ֽڡ�
 * 
 */
public class MicroRequest {
	
	
	
	
	/**
	 * ����һ�����ݰ�ֻ����ͬһ��id ͬһ���豸 ������Ҫ�洢 stream data time
	 * ���Ҫ��bean���洢��û���ֱ��һ��list�����������Ϣ������Ч��Ӧ��Ҳ��map��list<bean>����
	 * ���� 1.1�汾�г����޸�����洢��ʽ
	 * ���Խ�bean�����ڲ���
	 */
//	//���ݼ�
//	private Map<String,List<DataBean>> paramMap;
	private List<StreamData> paramList;
	
	
	public long lastPackageTime = 0;
	
	
	public String method;
	private String id = "";
	private String pwd = "";
	private boolean isClient = false;
	public static final String CRLF = "\r\n";
	public static final String BLANK = " ";
	
	//�����Ƿ���Ч
	public boolean isAlive = true;
	
	//һ֡������󳤶� ������֡ͷ ֡β��
	private static final int bufferSize = 256;
	
	//���ջ�����
	private byte[] data ;	
	
	//������ָ��ƫ����
	private int dataOffset = 0;		
	
	//socket ������ 
	private InputStream is;
	
	 //���һ֡�����ı���
	private String requestInfo;
	
	//��֡�������ĵ����ĳ���
	private int Messagelength;
	
	//�����ײ�����
	private int HeardLength;
	
	//�豸��
	private String device;
	
	//���ĳ���ƥ�乤��
	private Pattern LengthPattern = Pattern.compile("(len=)([0-9]{1,})(\r\n\r\n)");

	
	/************************************************/
	/**
	 * 1.1�汾���޸�Ϊֻ���������һ������
	 * 
	 * 1.0�汾ԭ�ƻ��Ƿ���һ�� stream �е���������
	 * 			�������Բ�����
	 * getList ���ɷ���ֵ�洢 list 
	 * 
	 * ClientRequestData : �ͻ��˵����������ݴ� bean  
	 */
	
	//private List<DataForm> getList = null;
	
	private  DataForm ClientRequestData;
	
	public DataForm getClientRequestData() {
		return ClientRequestData;
	}

	/************************************************/	
	
	public boolean isClient() {
		return isClient;
	}
	
	public String getDevice() {
		return device;
	}
	
	public String getId() {
		return id;
	}


	public String getPwd() {
		return pwd;
	}


	public String isAlive() {
		return isAlive == true?"T":"F";
	}


	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}


	public MicroRequest() {
		method = "";
		requestInfo = "";
		device = "";
		data = new byte[bufferSize];
		dataOffset = 0;
		Messagelength = 0;
		
		/*******************************************/
		/**
		 * 1.1�汾�޸�
		 * paramMap ԭ�����洢��ʽ
		 * paramList �ֲ����洢��ʽ
		 */
		//paramMap = new HashMap<String,List<DataBean>>();
		
		paramList = new ArrayList<StreamData>();
		
		/*******************************************/
		
		lastPackageTime = System.currentTimeMillis();
		
	}
	
	
	public MicroRequest(Socket client){
		this();
		try {
			this.is = client.getInputStream();
		} catch (IOException e) {
			client = null;
		}
	}
	
	/**
	 * @author siyuan
	 * 
	 * ������������ socket InputStream �ж���һ֡���������ݱ�
	 * 
	 * @return 
	 * ����� socket ��ȡ����һ֡���������ݱ��ͷ���true
	 * ���򷵻�false
	 */
	public boolean parseMessage() {
		
		/*********************************************************/	
		/**
		 * ���ڶ�ȡ,�ҵ���һ�� apache �ķ���
		 * Commons IO
		 */
//		InputStream is = null;
//		try {
//		    ...
//		    is = p.getInputStream();
//		    String text = IOUtils.toString(is, "GBK");
//		    this.jTextArea1.append(text);
//		} catch (...) {
//		    ...
//		} finally {
//		    IOUtils.closeQuietly(is)
//		}
		/*********************************************************/	
		
		try {
			int len = is.read(data, dataOffset, bufferSize);
			//System.out.println("len="+len);
			if(len > 0){
				dataOffset += len;
				if(HasCompleteMessage()){
					parseRequestInfo();
					return true;
				}
			}
		} catch (IOException e) {
			isAlive = false;
		}
		return false;
	}
	
	
	
	
	private boolean HasCompleteMessage() {
		String temp =new String(data,0,dataOffset);
		
//		System.out.println("���յ������ݣ�"+temp);
		
		Matcher m = LengthPattern.matcher(temp);
		if(m.find()){
			//���ĳ�����group2
			Messagelength = Integer.valueOf(m.group(2));
			//��ȡ�����ĳ����ټ���ͷ������  ����������
			HeardLength = temp.indexOf("\r\n\r\n") + 4;
			if(Messagelength >= 0){
				if(  temp.length() >= HeardLength + Messagelength ){
					//һ֡���ݴ������
					//������ɺ�Ӧ��ȡ�����ݰ��ŵ� requestInfo
					//Ȼ����ջ�����
					requestInfo = temp.substring(0, HeardLength + Messagelength );
					data = new byte[bufferSize];
					dataOffset = 0;
					Messagelength = 0;
					return true;
				}else{
					//��ʱû�������
					return false; 
				}	
			}else{				
				//������
				isAlive = false;
			}
		}
		return false;
	}
	
	
	
	
	/**
	 * "a 19333 19334 1 T len=xx\r\n\r\nmessage"	//��������
	 * "a 19333 19334 1 F len=xx\r\n\r\nmessage"	//������ɹر�����
	 * a ---- ����
	 * b ---- ɾ��
	 * c ---- �޸�
	 * d ---- ��ѯ
	 * 
	 * 19333 id �����16λ��
	 * 19334 pwd �����16λ��
	 * 001	 �豸��
	 * xx ���ĳ���
	 * message ����
	 */
	protected void parseRequestInfo() {
		if(StringUtil.checkStringTrim(requestInfo)){
			return ;
		}else{
			requestInfo = requestInfo.trim();
		}
		Pattern requestPattern = 
				Pattern.compile("([a-z])("+BLANK+")"		//group(1) == method
						+ "([1-9][0-9]{1,15})("+BLANK+")"	//group(3) == id
						+ "([0-9a-zA-Z]{1,16})("+BLANK+")"	//group(5) == pwd
						+ "([0-9]{1,8})("+BLANK+")"			//group(7) == device
						+ "([TF])("+BLANK+")"				//group(9) == isAlive
						+ "(len=)([0-9]{1,4})"				//group(12) == len
				/************************************/		
						
						/* + "(\r\n\r\n)" */);		
					/**
					 * version 1.2 �޸� 
					 * 			��ԭ��ƥ�䷽ʽ��
					 * 			���ͻ��˷��Ϳհ����İ�������ʱ��ƥ��ʧ��
					 */
		
				/************************************/
		Matcher m = requestPattern.matcher(requestInfo);
	//	System.out.println("requestInfo:"+requestInfo);
	//	System.out.println("start pattern");
		if(m.find()){
			
//			System.out.println("requestInfo matcher");
			
			method = m.group(1);
			id = m.group(3);
			pwd = m.group(5);
			device = m.group(7);
			isAlive = m.group(9).equals("T")?true:false;
			
//			System.out.println("method:"+method);
//			System.out.println("id:"+id);
//			System.out.println("pwd:"+pwd);
//			System.out.println("device:"+device);
//			System.out.println("isAlive:"+isAlive);
			//������У�飬���û�ҵ����id pwd �ǾͲ��ý�����
			if(IdFormDao.select(new IdForm(id,pwd)) == 0){
				return ;
			}else{
				//�����ȷ�ǿͻ��˷��������ݰ�
				isClient = true;
				//�������ݰ�ʱ��
				this.lastPackageTime = System.currentTimeMillis();
//				System.out.println("���յ�һ�����ݰ�"+new Date(this.lastPackageTime));
			}
			
			if(Integer.valueOf(m.group(12)) > 0){
				String paramString = requestInfo.substring(requestInfo.indexOf("\r\n\r\n")+4);
				
//				System.out.println("paramString:"+paramString);
				
				
				switch(method){
					case "a":				//����һ����Ϣ
						parseParams(paramString);
						break;
					case "d":
						parseSelect(paramString);
						break;				//��ѯһ����Ϣ
				}
			}	
		}
	}
	
	
	
	
	/**
	 * ��ѯ������ʽ ��
	 * name1
	 * ��ʱ�涨һ��ֻ�ܲ�һ��
	 * @param paramString stream ����
	 * 
	 * 	1.1�汾������ֵ�޸�Ϊ int ��Ӧ select �����ķ���ֵ��
	 * 
	 */
	private int parseSelect(String paramString){
		if(paramString == null || (paramString = paramString.trim()).equals("")){
			//�Ƿ�����
			return 0;
		}
		Pattern paramPattern = Pattern.compile("([a-z0-9_]{1,32})");
		Matcher m = paramPattern.matcher(paramString);
		
		if(m.find()){
			ClientRequestData = new DataForm(id, m.group(1), device);
			return DataFormDao.select(ClientRequestData);
		}
		return 0;
	}
	
	
	
	/**
	 * �������ӵ�����
	 * 
	 */
	private void parseParams(String paramString){
		if(paramString == null || (paramString = paramString.trim()).equals("")){
			return;
		}
		Pattern paramPattern = Pattern.compile("([a-z0-9_]{1,32})(=\\{)([0-9]{1,32})(:)([0-9]{1,})(\\})");
		Matcher m = paramPattern.matcher(paramString);
		
		/*****************************************************************/
		
		
		
		/**
		 * 1.1�汾����ԭ����д��ʽ
		 * ԭ���ݴ洢��ʽ�����ǲ�����
		 * 
		 * 
		 * 	ps:�治Ӧ�ð�ҹд�� zhu һ���Ĵ���
		 */
//		List<DataBean> valueList = null;
//		List<String> paramList = new ArrayList<String>();
//		while(m.find()){
//			String stream = m.group(1);
//			if(!paramList.contains(stream)){
//				paramList.add(stream);
//			}
//			if(!paramMap.containsKey(stream)){
//				paramMap.put(stream, new ArrayList<DataBean>());
//			}
//			valueList = paramMap.get(stream);
//			//һ�� bean ���һ������ ������ݴ����� device
//			long longtime;
//			valueList.add(new DataBean(m.group(3),new Timestamp((longtime=Long.valueOf(m.group(5)))
//					==0?System.currentTimeMillis():longtime)));
//			
//			
//			//System.out.println(m.group(1)+m.group(3)+m.group(5));
//			//������ɺ�Ҫд�뵽���ݿ�
//			////1.ȥ�ʺ������ƥ��id pwd
//			//2.ƥ��Ļ�������д�뵽id����
//		}
//		if(valueList != null){
//			for(String stream:paramList){
//				DataForm dataForm = new DataForm(id,stream,device);			
//				DataFormDao dao = DataFormDao.getInstance();
//				for(DataBean data:valueList){
//					
//					dataForm.setData(data.getValue());
//					dataForm.setTime(data.getTimestamp());
//					dao.insert(dataForm);
//				}				
//			}
//		}
		
		long time = 0;
		while(m.find()){
			time = Long.valueOf(m.group(5));
			paramList.add(new StreamData(m.group(1), 
					m.group(3), 
					time != 0L ? new Timestamp(time) : JDBCUtil.now()));
			
			
//			System.out.println("stream:"+m.group(1));
//			System.out.println("data:"+m.group(3));
//			System.out.println("time:"+(time != 0L ? new Timestamp(time) : JDBCUtil.now()));
			
		}
		
		DataForm dataForm = new DataForm(id,device);	
		for(StreamData data:paramList){
			dataForm.setData(data.getData());
			dataForm.setStream(data.getStream());
			dataForm.setTime(data.getTime());
			DataFormDao.insert(dataForm);
		}
		
		//��¼���ݵ����
		ServerContext.IncremeNtnumberOfData(paramList.size());
		//��¼���ݰ�����
		ServerContext.IncrementNumberOfMessage();
		
		//������д�����ݿ�֮��Ҫ��ʱ��գ���Ȼ��һ�ξͻ����һ���ظ�������
		paramList.clear();
		/*****************************************************************/
	}
	

	/**
	 * 1.1�汾����һ���ڲ� bean ȡ��ԭ�в����洢��ʽ
	 * ��� bean ���ڴ洢��ȡ���� stream data time
	 * ���� DataForm ��洢 id device ����Ȼ�����ܶ��ڴ�
	 * @author Administrator
	 * @version 1.1
	 */
	public class StreamData{
		private String stream = null;
		private String data = null;
		private Timestamp time = null;
		public StreamData(String stream, String data, Timestamp time) {
			super();
			this.stream = stream;
			this.data = data;
			this.time = time;
		}
		public StreamData() {
			super();
		}
		public String getStream() {
			return stream;
		}
		public void setStream(String stream) {
			this.stream = stream;
		}
		public String getData() {
			return data;
		}
		public void setData(String data) {
			this.data = data;
		}
		public Timestamp getTime() {
			return time;
		}
		public void setTime(Timestamp time) {
			this.time = time;
		}
		
		public StreamData(String stream, String data) {
			super();
			this.stream = stream;
			this.data = data;
		}
		
		
	}
	
}

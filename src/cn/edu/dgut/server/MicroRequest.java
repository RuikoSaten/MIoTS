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
 * 这是个解析设备发送过来的数据的类
 * 暂时定义格式为
 * method+blank+id+blank+pwd+blank+device+blank+"alive"+blank+"len="+length+crlf+crlf+message
 * 
 * message 格式：
 * name1={value1:time1}&name2={value2:time2}&name1={value3:time3}
 * 
 * 	方式 帐号 密码 设备 是否保持连接 长度 
 * "a 19333 19334 1 T len=xx\r\n\r\nmessage"	//保持连接
 * "a 19333 19334 1 F len=xx\r\n\r\nmessage"	//接收完成关闭连接
 * time1 用 Unix timestamp 
 * 当 timestamp == 0 时服务器自动插入接收到这一帧数据的时间
 * 允许名字重复，这样可以一次上传多个数据流的多组数据
 * 暂定 一帧的最大长度为 256 字节
 * @author siyuan
 * @version 1.1
 * 1.1版本新增：
 * 				修复部分已知问题。
 * 				将数据上限从 2048 改为 256字节。
 * 
 */
public class MicroRequest {
	
	
	
	
	/**
	 * 由于一个数据包只能是同一个id 同一个设备 所以需要存储 stream data time
	 * 如果要用bean来存储最好还是直接一个list里面存三个信息，这样效率应该也比map存list<bean>更高
	 * 于是 1.1版本中尝试修改这个存储方式
	 * 尝试将bean放在内部类
	 */
//	//数据集
//	private Map<String,List<DataBean>> paramMap;
	private List<StreamData> paramList;
	
	
	public long lastPackageTime = 0;
	
	
	public String method;
	private String id = "";
	private String pwd = "";
	private boolean isClient = false;
	public static final String CRLF = "\r\n";
	public static final String BLANK = " ";
	
	//连接是否有效
	public boolean isAlive = true;
	
	//一帧数据最大长度 （包含帧头 帧尾）
	private static final int bufferSize = 256;
	
	//接收缓冲区
	private byte[] data ;	
	
	//缓冲区指针偏移量
	private int dataOffset = 0;		
	
	//socket 输入流 
	private InputStream is;
	
	 //存放一帧完整的报文
	private String requestInfo;
	
	//这帧完整报文的正文长度
	private int Messagelength;
	
	//报文首部长度
	private int HeardLength;
	
	//设备号
	private String device;
	
	//正文长度匹配工具
	private Pattern LengthPattern = Pattern.compile("(len=)([0-9]{1,})(\r\n\r\n)");

	
	/************************************************/
	/**
	 * 1.1版本中修改为只返回最近的一个参数
	 * 
	 * 1.0版本原计划是返回一个 stream 中的所有数据
	 * 			但这明显不合理
	 * getList ：旧返回值存储 list 
	 * 
	 * ClientRequestData : 客户端的请求数据暂存 bean  
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
		 * 1.1版本修改
		 * paramMap 原参数存储方式
		 * paramList 现参数存储方式
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
	 * 这个方法负责从 socket InputStream 中读出一帧完整的数据报
	 * 
	 * @return 
	 * 如果从 socket 中取出了一帧完整的数据报就返回true
	 * 否则返回false
	 */
	public boolean parseMessage() {
		
		/*********************************************************/	
		/**
		 * 关于读取,找到了一个 apache 的方案
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
		
//		System.out.println("接收到的数据："+temp);
		
		Matcher m = LengthPattern.matcher(temp);
		if(m.find()){
			//正文长度在group2
			Messagelength = Integer.valueOf(m.group(2));
			//获取到正文长度再计算头部长度  不做多余事
			HeardLength = temp.indexOf("\r\n\r\n") + 4;
			if(Messagelength >= 0){
				if(  temp.length() >= HeardLength + Messagelength ){
					//一帧数据传输完成
					//传输完成后应该取出数据包放到 requestInfo
					//然后清空缓冲区
					requestInfo = temp.substring(0, HeardLength + Messagelength );
					data = new byte[bufferSize];
					dataOffset = 0;
					Messagelength = 0;
					return true;
				}else{
					//暂时没传输完成
					return false; 
				}	
			}else{				
				//出错了
				isAlive = false;
			}
		}
		return false;
	}
	
	
	
	
	/**
	 * "a 19333 19334 1 T len=xx\r\n\r\nmessage"	//保持连接
	 * "a 19333 19334 1 F len=xx\r\n\r\nmessage"	//接收完成关闭连接
	 * a ---- 增加
	 * b ---- 删除
	 * c ---- 修改
	 * d ---- 查询
	 * 
	 * 19333 id （最大16位）
	 * 19334 pwd （最大16位）
	 * 001	 设备号
	 * xx 正文长度
	 * message 正文
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
					 * version 1.2 修改 
					 * 			按原有匹配方式，
					 * 			当客户端发送空白正文包过来的时候匹配失败
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
			//完成身份校验，如果没找到这个id pwd 那就不用解析了
			if(IdFormDao.select(new IdForm(id,pwd)) == 0){
				return ;
			}else{
				//这个的确是客户端发来的数据包
				isClient = true;
				//更新数据包时间
				this.lastPackageTime = System.currentTimeMillis();
//				System.out.println("接收到一个数据包"+new Date(this.lastPackageTime));
			}
			
			if(Integer.valueOf(m.group(12)) > 0){
				String paramString = requestInfo.substring(requestInfo.indexOf("\r\n\r\n")+4);
				
//				System.out.println("paramString:"+paramString);
				
				
				switch(method){
					case "a":				//增加一条消息
						parseParams(paramString);
						break;
					case "d":
						parseSelect(paramString);
						break;				//查询一条消息
				}
			}	
		}
	}
	
	
	
	
	/**
	 * 查询参数格式 ：
	 * name1
	 * 暂时规定一次只能查一个
	 * @param paramString stream 名字
	 * 
	 * 	1.1版本将返回值修改为 int 对应 select 方法的返回值。
	 * 
	 */
	private int parseSelect(String paramString){
		if(paramString == null || (paramString = paramString.trim()).equals("")){
			//非法参数
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
	 * 解析增加的数据
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
		 * 1.1版本舍弃原来编写方式
		 * 原数据存储方式是在是不合理
		 * 
		 * 
		 * 	ps:真不应该熬夜写出 zhu 一样的代码
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
//			//一个 bean 存放一个数据 这个数据从属于 device
//			long longtime;
//			valueList.add(new DataBean(m.group(3),new Timestamp((longtime=Long.valueOf(m.group(5)))
//					==0?System.currentTimeMillis():longtime)));
//			
//			
//			//System.out.println(m.group(1)+m.group(3)+m.group(5));
//			//解析完成后要写入到数据库
//			////1.去帐号密码表单匹配id pwd
//			//2.匹配的话将数据写入到id表里
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
		
		//记录数据点个数
		ServerContext.IncremeNtnumberOfData(paramList.size());
		//记录数据包个数
		ServerContext.IncrementNumberOfMessage();
		
		//将数据写到数据库之后要及时清空，不然下一次就会产生一个重复的数据
		paramList.clear();
		/*****************************************************************/
	}
	

	/**
	 * 1.1版本新增一个内部 bean 取代原有参数存储方式
	 * 这个 bean 用于存储获取到的 stream data time
	 * 由于 DataForm 会存储 id device 这显然会多出很多内存
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

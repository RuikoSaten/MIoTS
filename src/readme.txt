



/*********************************/
	@version 1.2		
	修复部分已知bug
	1. 更改为心跳包以及转发功能预留的 clientList 存储的对象
		version 1.0 ： 存储 socket 对象
		version 1.2： 存储 dispatcher 对象
	2. 搭建了一个日志外壳 内容未实现
		运行日志 RunLogsTask
		异常日志 ExceptionLogsTask
		错误日志 ErrorLogsTask
	3. 为另外两种形式请求数据包提供一个 server error 响应
		对应请求 method b 
				method c
/*********************************/


/*********************************/
	@version 1.1
	修复了部分已知 bug
	0.移植到 windows server 2008 系统
	1.改写 method a 对应调用的 parseParams 解包方法
	2.修复了数据多次写入
	3.改写 ServerContext 的 properties  
	
/*********************************/


/*********************************/
	@version 1.0
	搭建了一个基础框架
	目前实现了：
				1.数据接收
				2.数据存储
				3.数据查询
	未实现的功能：
				1.数据更新
				2.数据删除
				3.心跳包
				4.****
	
	基本功能的实现方案：
				1. 在 server 类中阻塞式接收 socket 请求
				2. 将 socket 传给 dispatcher 
				3. dispatcher 实例化 request 和 response
			loop：
				4. request 负责接收 client 传来的数据包并解包、写入数据库
				5. 在 dispatcher 中循环查询 request 是否已完成接收
				6. 在数据接收处理完成后 dispatcher 分发消息给 servlet
				7. servlet 根据传来的数据包消息用 response 组建响应报文
				8. 报文组建完成后 push 到客户端
				9. LJMP loop
	
	
/*********************************/






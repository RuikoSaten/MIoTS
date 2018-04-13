

/*********************************/
	@verson 1.1
	修复了部分已知 bug
	0.移植到 windows server 2008 系统
	1.改写 method a 对应调用的 parseParams 解包方法
	2.修复了数据多次写入
	3.改写 ServerContext 的 properties  
	
/*********************************/


/*********************************/
	@verson 1.0
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






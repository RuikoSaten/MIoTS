



/*********************************/
	@version 1.2		
	�޸�������֪bug
	1. ����Ϊ�������Լ�ת������Ԥ���� clientList �洢�Ķ���
		version 1.0 �� �洢 socket ����
		version 1.2�� �洢 dispatcher ����
	2. ���һ����־��� ����δʵ��
		������־ RunLogsTask
		�쳣��־ ExceptionLogsTask
		������־ ErrorLogsTask
	3. Ϊ����������ʽ�������ݰ��ṩһ�� server error ��Ӧ
		��Ӧ���� method b 
				method c
/*********************************/


/*********************************/
	@version 1.1
	�޸��˲�����֪ bug
	0.��ֲ�� windows server 2008 ϵͳ
	1.��д method a ��Ӧ���õ� parseParams �������
	2.�޸������ݶ��д��
	3.��д ServerContext �� properties  
	
/*********************************/


/*********************************/
	@version 1.0
	���һ���������
	Ŀǰʵ���ˣ�
				1.���ݽ���
				2.���ݴ洢
				3.���ݲ�ѯ
	δʵ�ֵĹ��ܣ�
				1.���ݸ���
				2.����ɾ��
				3.������
				4.****
	
	�������ܵ�ʵ�ַ�����
				1. �� server ��������ʽ���� socket ����
				2. �� socket ���� dispatcher 
				3. dispatcher ʵ���� request �� response
			loop��
				4. request ������� client ���������ݰ��������д�����ݿ�
				5. �� dispatcher ��ѭ����ѯ request �Ƿ�����ɽ���
				6. �����ݽ��մ�����ɺ� dispatcher �ַ���Ϣ�� servlet
				7. servlet ���ݴ��������ݰ���Ϣ�� response �齨��Ӧ����
				8. �����齨��ɺ� push ���ͻ���
				9. LJMP loop
	
	
/*********************************/






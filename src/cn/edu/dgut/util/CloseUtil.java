package cn.edu.dgut.util;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class CloseUtil {
	
	/**
	 *  close input/output stream
	 * @param io stream
	 */
	public static void closeIO(Closeable... io){
		for(Closeable temp:io){
			try {
				if(temp != null){
					temp.close();
				}
			} catch (IOException e) {
			
			}
		}
	}
	
	
	
	/**
	 * close ServerSocket
	 * @param socket
	 */
	public static void closeSocket(ServerSocket socket){
		try {
			if(socket != null){
				socket.close();
			}
		} catch (IOException e) {
			
		}
	}
	
	
	/**
	 * close Socket
	 * @param socket
	 */
	public static void closeSocket(Socket socket){
		try {
			if(socket != null){
				//System.out.println("close "+socket);
				socket.close();
			}
		} catch (IOException e) {
			
		}
	}
	
	
	/**
	 * close DatagramSocket
	 * @param socket
	 */
	public static void closeSocket(DatagramSocket socket){
		if(socket != null){
			socket.close();
		}
	}
	
	
	
}

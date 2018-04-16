package cn.edu.dgut.server;

import java.util.Date;

/**
 * 
 * @author Administrator
 *
 */
public class Main {
	public static void main(String[] args) {
		//new MicroServer().start();
		
		System.out.println("MicroServer start");
		System.out.println("@author RuikoSaten");
		System.out.println(new Date());
		/**
		 * verson 1.2
		 * @author Administrator
		 */ 
		MicroServer.getInstance().start();
		
	}
}

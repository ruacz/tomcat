package com.yc.tomcat.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 启动类
 * company ycinfo
 * @author cz
 * @date2021年1月2日
 * email:2286726810@qq.com
 */
public class StartTomcat {
	public static void main(String[] args) throws IOException {
		
			new StartTomcat().start();
		
	}

	private void start() throws IOException {
		//解析配置文件web.xml
		int port = Integer.parseInt(ReadConfig.getInstance().getProperty("port"));
		
		//启用一个ServletSocket
		
		ServerSocket ssk = new ServerSocket(port);
		System.out.println("服务器已启动，占用端口：" + port);
		
		new ParseUrlPattern();
		new ParseWebXml();
		
		//启动一个线程或使用线程池处理用户发过来的请求  -> Socket
		ExecutorService servierThread = Executors.newFixedThreadPool(20);
		
		Socket sk = null;
		while(true) {
			sk = ssk.accept();
			servierThread.submit(new ServerService(sk));
		}
	}
	
	
	
	
	
	
	
}

package com.yc.web.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import com.yc.tomcat.core.ConstantInfo;
import com.yc.tomcat.core.ParseWebXml;
import com.yc.tomcat.util.StringUtil;

public class HttpServletResponse implements ServletResponse {
	private OutputStream os = null;
	private String basePath = ConstantInfo.BASE_PATH;
	private String projectName;
	
	public  HttpServletResponse(OutputStream os, String projectName) {
		this.os = os;
		this.projectName = "/" + projectName;
	}
	
	@Override
	public void sendRedirect(String url) {
		if(StringUtil.checkNull(url)) {
			//报错404
			error404(url);
			return;
		}
		
		//	/anjuke/index
		if(!url.startsWith(projectName)) {
			send302(projectName + "/" + url);
			return;
		}
		
		if(url.startsWith("/") && url.indexOf("/")  == url.lastIndexOf("/")) {
			send302(url);
			return;
		}else {
			if(url.endsWith("/")) {
				String defaultPath = ConstantInfo.DEFAULT_RESOURCE;
				
				//读取指定资源文件
				File fl = new File(basePath, url.substring(1).replace("/", "\\") + defaultPath);
				if(!fl.exists() || !fl.isFile()) {
					//报错
					error404(url);
					return;
				}
				send200(readFile(fl), url.substring(url.lastIndexOf(".") + 1).toLowerCase());
				return;
			}
			
			File fl = new File(basePath, url.substring(1).replace("/", "\\"));
			System.out.println(fl.getAbsolutePath());
			
			if(!fl.exists() || !fl.isFile()) {
				error404(url);
				return;
			}
			
			send200(readFile(fl), url.substring(url.lastIndexOf(".") + 1).toLowerCase());
		}
		

				
		/*try {
			FileInputStream fis = new FileInputStream(fl);
			byte[] bt = new byte[fis.available()];
			fis.read(bt);
			
			String ext = url.substring(url.lastIndexOf(".")+1).toLowerCase();
			String contentType = ParseWebXml.getContentType(ext);
			if(StringUtil.checkNull(contentType)) {
				contentType = "text/html;charset=utf-8";
			}
 			

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}*/
		
	}

	private void send200(byte[] bt, String extension) {

		String contentType = "text/html;charset=utf-8";
		String type = ParseWebXml.getContentType(extension);
		if(!StringUtil.checkNull(type)) {
			contentType = type;
		}
		
		try {
			String responseHeader = "HTTP/1.1 200 OK\r\nControl-Type: text/html:charset=utf-8\r\nContent-Length: " + bt.length + "\r\n\r\n";
			os.write(responseHeader.getBytes());
			os.write(bt);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private byte[] readFile(File fl) {
		byte[] bt = null;
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(fl);
			bt = new byte[fis.available()];
			fis.read(bt);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fis!=null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return bt;
	}

	/**
	 * 302
	 * @param url
	 */
	private void send302(String url) {
		try {
			String responseHeader = "HTTP/1.1 302 Moved Temporarily\r\nContent-Type: text/html;charset=utf-8\r\nLocation:" + url + "/\r\n\r\n";
			os.write(responseHeader.getBytes());
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	/**
	 * 404
	 * @param url
	 */
	private void error404(String url) {
		try {
			String data = "<h1>HTTP Status 404 - " + url +"</h1>";
			String responseHeader = "HTTP/1.1 404 File Not Found\r\nContent-Type: text/htm;charset=utf-8\r\nContent-Length: " + data.length() + "\r\n\r\n";
			os.write(responseHeader.getBytes());
			os.write(data.getBytes());
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		String responseHeader = "HTTP/1.1 200 OK\r\nContent-Type: text/html;charset=utf-8\r\n\r\n";
		os.write(responseHeader.getBytes());
		os.flush();
		return new PrintWriter(os);
	}

}

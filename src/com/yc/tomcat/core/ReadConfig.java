package com.yc.tomcat.core;

import java.io.InputStream;
import java.util.Properties;

/**
 * 读取配置文件的类
 * 
 * company ycinfo
 * @author cz
 * @date2021年1月2日
 * email:2286726810@qq.com
 */
public class ReadConfig extends Properties{
	private static ReadConfig instance = new ReadConfig();
	
	private ReadConfig() {
		try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("web.properties")){
			load(is);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ReadConfig getInstance() {
		return instance;
	}
}

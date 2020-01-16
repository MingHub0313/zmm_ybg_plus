package com.ybg.weixin.api.domain;
import java.util.Properties;

import com.ybg.base.util.SpringContextUtils;
import com.ybg.weixin.api.service.WeixinApiService;
/**
 * @author Deament
 * @date 2017年10月1日
 * **/
public class WeixinOpenAuthorizationConfig {
	
	public static final String	APPID	= "appId";
	public static final String	SECRET	= "secret";
	
	public WeixinOpenAuthorizationConfig() {
	}
	
	private static Properties props = new Properties();
	static {
		reflushProperties();
	}
	
	public static String getValue(String key) {
		return props.getProperty(key);
	}
	
	public static void updateProperties(String key, String value) {
		props.setProperty(key, value);
	}
	
	public static void reflushProperties() {
		WeixinApiService service = (WeixinApiService) SpringContextUtils.getBean(WeixinApiService.class);
		props.putAll(service.getSetting());
	}
}

/**
 * 
 */
package com.ybg.social.weixin.config;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.UserIdSource;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.web.servlet.View;
import com.ybg.social.ImoocConnectView;
import com.ybg.social.core.properties.WeixinProperties;
import com.ybg.social.weixin.api.Weixin;
import com.ybg.social.weixin.connect.WeixinConnectionFactory;
import com.ybg.weixin.api.service.WeixinApiService;

/** 微信登录配置
 * 
 * @author zhailiang */
@Configuration
public class WeixinAutoConfiguration extends SocialAutoConfigurerAdapter {
	
	@Autowired
	WeixinApiService apiService;
	
	@Override
	protected ConnectionFactory<Weixin> createConnectionFactory() {
		WeixinProperties weixinConfig = new WeixinProperties();
		Map<String, String> setting = apiService.getSetting();
		weixinConfig.setAppId(setting.get("appId"));
		weixinConfig.setAppSecret(setting.get("secret"));
		return new WeixinConnectionFactory(weixinConfig.getProviderId(), weixinConfig.getAppId(), weixinConfig.getAppSecret());
	}
	
	@Bean(name = { "connect/weixinConnect", "connect/weixinConnected" })
	public View weixinConnectedView() {
		return new ImoocConnectView();
	}

	
	@Override
	public UserIdSource getUserIdSource() {
		return new UserIdSource() {
			
			@Override
			public String getUserId() {
				// TODO Auto-generated method stub
				return "";
			}
		};
	}
}

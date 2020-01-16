/**
 * 
 */
package com.ybg.social.core.properties;

import org.springframework.boot.autoconfigure.social.SocialProperties;

import com.ybg.enums.SocialTypeEnum;

/**
 * 
 * <p>
 * Title: WeixinProperties
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Company: www.88ybg.com
 * </p>
 * 
 * @author 严宇
 * 
 * @date 2018年6月3日
 * 
 */
public class WeixinProperties extends SocialProperties {

	/** 第三方id，用来决定发起第三方登录的url，默认是 weixin。 */
	private String providerId = SocialTypeEnum.WEIXIN.getValue();

	/** @return the providerId */
	public String getProviderId() {
		return providerId;
	}

	/**
	 * @param providerId
	 *            the providerId to set
	 */
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
}

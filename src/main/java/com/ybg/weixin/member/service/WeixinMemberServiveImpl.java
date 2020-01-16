package com.ybg.weixin.member.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ybg.weixin.api.network.WeixinNetWork;
import com.ybg.weixin.api.service.WeixinApiService;
import com.ybg.weixin.member.domain.WeixinUserDTO;


/** @author Deament
 * @date 2017/1/1 */
@Repository
public class WeixinMemberServiveImpl implements WeixinMemberService {
	
	@Autowired
	WeixinNetWork		weixinNW;
	@Autowired
	WeixinApiService	weixinApiService;
	
	private String getAccessToken() {
		return weixinApiService.getAccessToken();
	}
	
	@Override
	public WeixinUserDTO get(String openid) {
		weixinNW.user_info(getAccessToken(), openid);
		return null;
	}
	
	@Override
	public List<WeixinUserDTO> batchget(String openid) {
		// XXX 不打算做。。。。
		return null;
	}
}

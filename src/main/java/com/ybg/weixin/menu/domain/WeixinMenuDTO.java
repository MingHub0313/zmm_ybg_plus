package com.ybg.weixin.menu.domain;
import java.util.Arrays;

/*** @author https://gitee.com/YYDeament/88ybg
 * 
 * @date 2016/10/1 */
public class WeixinMenuDTO {
	
	WeixinButton[] button;
	
	public WeixinButton[] getButton() {
		return button;
	}
	
	public void setButton(WeixinButton[] button) {
		this.button = button;
	}
	
	@Override
	public String toString() {
		return "WeixinMenuVO [button=" + Arrays.toString(button) + "]";
	}
}

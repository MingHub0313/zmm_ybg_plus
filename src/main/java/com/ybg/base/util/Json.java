package com.ybg.base.util;

/**
 * 用户后台向前台返回的JSON对象
 * 
 * <p>
 * Title: Json
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
 * @date 2018年6月4日
 * 
 */
public class Json implements java.io.Serializable {

	private static final long serialVersionUID = -1950834705338436194L;
	/** 是否成功 **/
	private boolean success = false;
	/** 返回的消息 **/
	private String msg = "";
	/** 返回的数据 **/
	private Object obj = null;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	@Override
	public String toString() {
		return "Json [success=" + success + ", msg=" + msg + ", obj=" + obj + "]";
	}

}

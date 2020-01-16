package com.ybg.common;

import java.io.Serializable;

import com.ybg.enums.SystemResultEnum;

/**
 * 返回前端结果集
 * 
 * @author yanyu
 **/
public class ResultVO<T> implements Serializable {

	private int code;
	private T data;
	private String msg;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isSuccess() {
		if (code == SystemResultEnum.SECCESS.getCode()) {
			return true;
		}
		return false;
	}

}

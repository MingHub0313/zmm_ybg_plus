package com.ybg.base.util.webexception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ybg.base.util.Json;

/**
 * 异常处理器
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月27日 下午10:16:19
 */
@RestControllerAdvice
public class YbgExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	/** 自定义异常 */
	@ExceptionHandler(ResultException.class)
	public Json handleRRException(ResultException e) {
		Json r = new Json();
		r.setMsg(e.getMsg());
		r.setSuccess(false);
		return r;
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public Json handleDuplicateKeyException(DuplicateKeyException e) {
		logger.error(e.getMessage(), e);
		// return RepostResult.error("数据库中已存在该记录");
		Json r = new Json();
		r.setMsg("数据库中已存在该记录");
		r.setSuccess(false);
		return r;
	}

	@ExceptionHandler(Exception.class)
	public Json handleException(Exception e) {
		logger.error(e.getMessage(), e);
		Json r = new Json();
		r.setMsg("服务器异常");
		r.setSuccess(false);
		return r;
	}
}

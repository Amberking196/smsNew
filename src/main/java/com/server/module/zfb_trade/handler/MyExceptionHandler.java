package com.server.module.zfb_trade.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.server.module.zfb_trade.util.ResultUtil;
import com.server.module.zfb_trade.util.enumparam.ResultEnum;
import com.server.util.ReturnDataUtil;

@ControllerAdvice
public class MyExceptionHandler {

	public static Logger log = LogManager.getLogger(MyExceptionHandler.class); 
	
	@ExceptionHandler(value = IllegalArgumentException.class)
	@ResponseBody
	public ReturnDataUtil handle(IllegalArgumentException e) {
		e.printStackTrace();
		return ResultUtil.error(ResultEnum.ILLEGAL_PARAMS,e.getMessage());
	}
	
	
	@ExceptionHandler(value = RuntimeException.class)
	@ResponseBody
	public ReturnDataUtil handle(RuntimeException e) {
		e.printStackTrace();
		return ResultUtil.error(ResultEnum.UNKOWN_ERROR,e.getMessage());
	}
	
	@ExceptionHandler
	@ResponseBody
	public ReturnDataUtil handle(Exception e) {
		e.printStackTrace();
		return ResultUtil.error(ResultEnum.UNKOWN_ERROR,e.getMessage());
	}
}

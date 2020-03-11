package com.server.module.trade.exception;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(value =MyException.class)
	@ResponseBody
	public ErrorInfo<String> jsonErrorHandler(HttpServletRequest req,MyException e) throws Exception{
	  ErrorInfo<String> r=new ErrorInfo<String>();
	  r.setCode(e.getCode());
	  r.setMessage(e.getMessage());
	  
      return r;
   }
}

package com.server.module.zfb_trade.util;

import com.server.module.zfb_trade.util.enumparam.ResultEnum;
import com.server.util.ReturnDataUtil;

public class ResultUtil {
	
	public static ReturnDataUtil success(ResultEnum result){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(result.getCode());
		returnData.setMessage(result.getMessage());
		return returnData;
	}

	public static ReturnDataUtil success(ResultEnum result,Object returnObject){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(result.getCode());
		returnData.setMessage(result.getMessage());
		returnData.setReturnObject(returnObject);
		return returnData;
	}
	public static ReturnDataUtil success(ResultEnum result,Object returnObject,boolean willGo){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(result.getCode());
		returnData.setMessage(result.getMessage());
		returnData.setReturnObject(returnObject);
		returnData.setWillGo(willGo);
		return returnData;
	}
	
	public static ReturnDataUtil success(Object returnObject){
		return success(ResultEnum.SUCCESS,returnObject);
	}
	
	public static ReturnDataUtil success(Object returnObject,boolean willGo){
		return success(ResultEnum.SUCCESS,returnObject,willGo);
	}
	
	public static ReturnDataUtil success(){
		return success(ResultEnum.SUCCESS);
	}
	
	public static ReturnDataUtil error(ResultEnum result){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(result.getCode());
		returnData.setMessage(result.getMessage());
		return returnData;
	}
	
	public static ReturnDataUtil error(ResultEnum result,Object returnObject){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(result.getCode());
		returnData.setMessage(result.getMessage());
		returnData.setReturnObject(returnObject);
		return returnData;
	}
	
	public static ReturnDataUtil error(ResultEnum result,Object returnObject,boolean willGo){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(result.getCode());
		returnData.setMessage(result.getMessage());
		returnData.setReturnObject(returnObject);
		returnData.setWillGo(willGo);
		return returnData;
	}
	
	public static ReturnDataUtil error(Object returnObject){
		return error(ResultEnum.ERROR,returnObject);
	} 
	
	public static ReturnDataUtil error(Object returnObject,boolean willGo){
		return error(ResultEnum.ERROR,returnObject,willGo);
	} 
	public static ReturnDataUtil error(){
		return error(ResultEnum.ERROR);
	} 
	
	public static ReturnDataUtil selectError(){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(0);
		returnData.setMessage("查询失败，无数据");
		return returnData;
	} 
	
	public static ReturnDataUtil result(ResultEnum result){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(result.getCode());
		returnData.setMessage(result.getMessage());
		return returnData;
	}
	
	public static ReturnDataUtil result(ResultEnum result,Object returnObject){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(result.getCode());
		returnData.setMessage(result.getMessage());
		returnData.setReturnObject(returnObject);
		return returnData;
	}
	
	public static ReturnDataUtil result(ResultEnum result,Object returnObject,boolean willGo){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(result.getCode());
		returnData.setMessage(result.getMessage());
		returnData.setReturnObject(returnObject);
		returnData.setWillGo(willGo);
		return returnData;
	}
}

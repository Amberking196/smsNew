package com.server.module.trade.auth.validator.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.trade.auth.validator.IReqValidator;
import com.server.module.trade.auth.validator.dto.Credence;

import java.util.List;
import java.util.Map;

/**
 * 账号密码验证
 *
 * @author yjr
 * @date 2017-08-23 12:34
 */
@Service
public class DbValidator implements IReqValidator {

   /// @Autowired
    //UserMapper userMapper;

    @Override
    public boolean validate(Credence credence) {
        /*List<User> users = userMapper.selectList(new EntityWrapper<User>().eq("userName", credence.getCredenceName()));
        if (users != null && users.size() > 0) {
            return true;
        } else {
            return false;
        }*/
    	
    	String openId=credence.getOpenId();
    	
    	if("oORvOw5qlEihRNIJD-vga2xbhkUw".equals(openId)){
    		return true;
    	}
    	
    	return true;
    }
}

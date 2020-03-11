package com.server.module.trade.auth.validator.impl;

import org.springframework.stereotype.Service;

import com.server.module.trade.auth.validator.IReqValidator;
import com.server.module.trade.auth.validator.dto.Credence;

/**
 * 直接验证账号密码是不是admin
 *
 * @author yjr
 * @date 2017-08-23 12:34
 */
@Service
public class SimpleValidator implements IReqValidator {

    private static String USER_NAME = "admin";

    private static String PASSWORD = "admin";

    @Override
    public boolean validate(Credence credence) {

        String userName = credence.getCredenceName();
        String password = credence.getCredenceCode();
        System.out.println("userName=="+userName);
        System.out.println("password=="+password);
        if (USER_NAME.equals(userName) && PASSWORD.equals(password)) {
            return true;
        } else {
            return false;
        }
    }
}

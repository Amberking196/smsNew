package com.server.module.trade.auth.controller;

import com.server.module.trade.auth.controller.dto.AuthRequest;
import com.server.module.trade.auth.controller.dto.AuthResponse;
import com.server.module.trade.auth.util.JwtTokenUtil;
import com.server.module.trade.auth.validator.IReqValidator;
import com.server.module.trade.exception.BizExceptionEnum;
import com.server.module.trade.exception.MyException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 请求验证的
 *
 * @author yjr
 */
@RestController
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Resource(name = "simpleValidator")
    private IReqValidator reqValidator;

    @RequestMapping(value = "${jwt.auth-path}")
    public ResponseEntity<?> createAuthenticationToken(AuthRequest authRequest) {

        boolean validate = reqValidator.validate(authRequest);

        if (validate) {
            final String randomKey = jwtTokenUtil.getRandomKey();
            final String token = jwtTokenUtil.generateToken(authRequest.getUserName(), randomKey);
            return ResponseEntity.ok(new AuthResponse(token, randomKey,0));
        } else {
            throw new MyException(BizExceptionEnum.AUTH_REQUEST_ERROR);
        }
    }
}

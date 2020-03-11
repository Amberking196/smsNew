package com.server.module.trade.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.server.module.trade.exception.MyException;
import com.server.module.trade.exception.SysExceptionEnum;

/**
 * 渲染工具类
 *
 * @author yjr
 */
public class RenderUtil {

    /**
     * 渲染json对象
     */
    public static void renderJson(HttpServletResponse response, Object jsonObject) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(jsonObject));
        } catch (IOException e) {
            throw new MyException(SysExceptionEnum.WRITE_ERROR);
        }
    }
}

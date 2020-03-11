package com.server.module.trade.auth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alibaba.fastjson.JSON;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.auth.util.JwtTokenUtil;
import com.server.module.trade.config.properties.JwtProperties;
import com.server.module.trade.exception.BizExceptionEnum;
import com.server.module.trade.util.ErrorTip;
import com.server.module.trade.util.RenderUtil;
import com.server.module.trade.util.UserUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

/**
 * 对客户端请求的jwt token验证过滤器
 *
 * @author yjr
 */
@WebFilter
public class AuthFilter extends OncePerRequestFilter {

	public static Logger log = LogManager.getLogger(AuthFilter.class);
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private JwtProperties jwtProperties;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.info("请求路径：" + request.getServletPath());
		if (request.getServletPath().contains("shopRegister")
				|| (request.getServletPath().contains("wechat") && !request.getServletPath().contains("register"))
				|| request.getServletPath().contains("sms")
				|| request.getServletPath().contains("machineInfo/getCompanyId")
				|| request.getServletPath().contains("aliUser")
				|| request.getServletPath().contains("huafaTokenLogin")
				|| request.getServletPath().contains("aliInfo")) {// jwtProperties.getAuthPath()
			chain.doFilter(request, response);
			return;
		}
		//log.info(" ===========in  AuthFilter======================= " + jwtProperties.getHeader());
		final String requestHeader = request.getHeader(jwtProperties.getHeader());
		log.info("header=" + requestHeader);
		String authToken = null;
		if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
			try {
				authToken = requestHeader.substring(7);

			} catch (Exception e) {
				e.printStackTrace();
				RenderUtil.renderJson(response, new ErrorTip(-1,
						BizExceptionEnum.TOKEN_ERROR.getMessage()));
				return;
			}
			log.info("authToken==" + authToken);
			if (authToken != null) {
				if (authToken.equals("undefined")) {// 经常有undefined 过来
					log.info("token is undefined =====");
					RenderUtil.renderJson(response, new ErrorTip(-1,
							BizExceptionEnum.TOKEN_ERROR.getMessage()));
					return;
				}

				// 验证token是否过期,包含了验证jwt是否正确
				try {
					boolean flag = jwtTokenUtil.isTokenExpired(authToken);
					if (flag) {
						log.info("已过期===flag=" + flag);
						//RenderUtil.renderJson(response, new ErrorTip(BizExceptionEnum.TOKEN_EXPIRED.getCode(),
								//BizExceptionEnum.TOKEN_EXPIRED.getMessage()));
						RenderUtil.renderJson(response, new ErrorTip(-1,"请清理缓存：设置-----》通用------》清理缓存"));
						return;
					} else {// 把用户id放到当前线程map
						log.info("获取token数据=");
						Claims claims = jwtTokenUtil.getClaimFromToken(authToken);
						if (claims != null) {
							String temp = claims.getSubject();
							log.info("sub===" + temp);
							UserVo vo = JSON.parseObject(temp, UserVo.class);
							UserUtil.setUser(vo);
							log.info("已经设置本地线程--user---");
							log.info("进入controller===");
							chain.doFilter(request, response);
							log.info("==================clear user ===============");
							UserUtil.clearAllUserInfo();
						} else {
							log.info("解析token为null");
							RenderUtil.renderJson(response, new ErrorTip(-1,
									BizExceptionEnum.TOKEN_ERROR.getMessage()));
							return;

						}
					}
				} catch (Exception e) {
					// 有异常就是token解析失败
					e.printStackTrace();
					RenderUtil.renderJson(response, new ErrorTip(-1,
							BizExceptionEnum.TOKEN_ERROR.getMessage()));
					return;
				}

			} else {
				log.info("传的token异常====" + authToken);
				// header没有带Bearer字段
				RenderUtil.renderJson(response, new ErrorTip(-1,
						BizExceptionEnum.TOKEN_ERROR.getMessage()));
				return;
			}

		}else if(requestHeader != null && requestHeader.startsWith("Invite ")){//邀请新用户立即注册携带token
			log.info("invite authToken==" + authToken);
			try {
				authToken = requestHeader.substring(7);

			} catch (Exception e) {
				e.printStackTrace();
				RenderUtil.renderJson(response, new ErrorTip(-1,
						BizExceptionEnum.TOKEN_ERROR.getMessage()));
				return;
			}
			log.info("authToken==" + authToken);
			if (authToken != null) {
				if (authToken.equals("undefined")) {// 经常有undefined 过来
					log.info("token is undefined =====");
					RenderUtil.renderJson(response, new ErrorTip(-1,
							BizExceptionEnum.TOKEN_ERROR.getMessage()));
					return;
				}

				// 验证token是否过期,包含了验证jwt是否正确
				try {
					boolean flag = jwtTokenUtil.isTokenExpired(authToken);
					if (flag) {
						log.info("已过期===flag=" + flag);
						//RenderUtil.renderJson(response, new ErrorTip(BizExceptionEnum.TOKEN_EXPIRED.getCode(),
								//BizExceptionEnum.TOKEN_EXPIRED.getMessage()));
						RenderUtil.renderJson(response, new ErrorTip(-1,"该链接已经失效"));
						return;
					} else {// 把用户id放到当前线程map
						//log.info("获取token数据=");
						Claims claims = jwtTokenUtil.getClaimFromToken(authToken);
						if (claims != null) {
							String temp = claims.getSubject();
							log.info("token数据===" + temp);
							UserVo vo = JSON.parseObject(temp, UserVo.class);
							UserUtil.setUser(vo);
							//log.info("已经设置本地线程--user---");
							//log.info("进入controller===");
							chain.doFilter(request, response);
							log.info("==================clear user ===============");
							UserUtil.clearAllUserInfo();
						} else {
							log.info("解析token为null");
							RenderUtil.renderJson(response, new ErrorTip(-1,
									BizExceptionEnum.TOKEN_ERROR.getMessage()));
							return;

						}
					}
				} catch (Exception e) {
					// 有异常就是token解析失败
					e.printStackTrace();
					RenderUtil.renderJson(response, new ErrorTip(-1,
							BizExceptionEnum.TOKEN_ERROR.getMessage()));
					return;
				}

			} else {
				log.info("传的token异常====" + authToken);
				// header没有带Bearer字段
				RenderUtil.renderJson(response, new ErrorTip(-1,
						BizExceptionEnum.TOKEN_ERROR.getMessage()));
				return;
			}
		} 
		else {
			log.info("进入controller==木有token=");

			chain.doFilter(request, response);

		}
	}

}
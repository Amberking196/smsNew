package com.server.module.trade.config.gzh;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyWXGzhConfig {

	protected volatile String appId;
	protected volatile String secret;
	protected volatile String token;
	
	protected volatile String appAppId;
	protected volatile String appSecret;
//	protected volatile String accessToken;
//	protected volatile long expiresTime;
//
//	protected volatile String jsapiTicket;
//	protected volatile long jsapiTicketExpiresTime;
//
//	protected volatile String cardApiTicket;
//	protected volatile long cardApiTicketExpiresTime;
//
//	protected Lock accessTokenLock = new ReentrantLock();
//	protected Lock jsapiTicketLock = new ReentrantLock();
//	protected Lock cardApiTicketLock = new ReentrantLock();

//	public synchronized void updateAccessToken(String accessToken, int expiresTime) {
//		this.accessToken = accessToken;
//		this.expiresTime = System.currentTimeMillis() + (expiresTime - 200) * 1000L;
//	}
//
//	public synchronized void updateJsapiTicket(String jsapiTicket, int jsapiTicketExpiresTime) {
//		this.jsapiTicket = jsapiTicket;
//		this.jsapiTicketExpiresTime = System.currentTimeMillis() + (jsapiTicketExpiresTime - 200) * 1000L;
//	}
//
//	public synchronized void updateCardApiTicket(String cardApiTicket, int cardApiTicketExpiresTime) {
//		this.cardApiTicket = cardApiTicket;
//		this.cardApiTicketExpiresTime = System.currentTimeMillis() + (cardApiTicketExpiresTime - 200) * 1000L;
//	}

	public String getAppId() {
		return appId;
	}

	public String getAppAppId() {
		return appAppId;
	}

	public void setAppAppId(String appAppId) {
		this.appAppId = appAppId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

//	public String getAccessToken() {
//		return accessToken;
//	}
//
//	public void setAccessToken(String accessToken) {
//		this.accessToken = accessToken;
//	}
//
//	public long getExpiresTime() {
//		return expiresTime;
//	}
//
//	public void setExpiresTime(long expiresTime) {
//		this.expiresTime = expiresTime;
//	}
//
//	public String getJsapiTicket() {
//		return jsapiTicket;
//	}
//
//	public void setJsapiTicket(String jsapiTicket) {
//		this.jsapiTicket = jsapiTicket;
//	}
//
//	public long getJsapiTicketExpiresTime() {
//		return jsapiTicketExpiresTime;
//	}
//
//	public void setJsapiTicketExpiresTime(long jsapiTicketExpiresTime) {
//		this.jsapiTicketExpiresTime = jsapiTicketExpiresTime;
//	}
//
//	public String getCardApiTicket() {
//		return cardApiTicket;
//	}
//
//	public void setCardApiTicket(String cardApiTicket) {
//		this.cardApiTicket = cardApiTicket;
//	}
//
//	public long getCardApiTicketExpiresTime() {
//		return cardApiTicketExpiresTime;
//	}
//
//	public void setCardApiTicketExpiresTime(long cardApiTicketExpiresTime) {
//		this.cardApiTicketExpiresTime = cardApiTicketExpiresTime;
//	}
//
//	public Lock getAccessTokenLock() {
//		return accessTokenLock;
//	}
//
//	public void setAccessTokenLock(Lock accessTokenLock) {
//		this.accessTokenLock = accessTokenLock;
//	}
//
//	public Lock getJsapiTicketLock() {
//		return jsapiTicketLock;
//	}
//
//	public void setJsapiTicketLock(Lock jsapiTicketLock) {
//		this.jsapiTicketLock = jsapiTicketLock;
//	}
//
//	public Lock getCardApiTicketLock() {
//		return cardApiTicketLock;
//	}
//
//	public void setCardApiTicketLock(Lock cardApiTicketLock) {
//		this.cardApiTicketLock = cardApiTicketLock;
//	}

}

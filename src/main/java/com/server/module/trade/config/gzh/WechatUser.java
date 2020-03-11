package com.server.module.trade.config.gzh;

import java.util.List;

import lombok.Data;
@Data
public class WechatUser {
	private Integer  subscribe;
	private String   openid;
	private String   nickname;
	private Integer  sex;
	private String   language;
	private String   city;
	private String   province;
	private String   country;
	private String   headimgurl;
	private Integer  subscribe_time;
	private String   unionid;
	private String   remark;
	private Integer   groupid;
	private List<Integer>  tagid_list;
	private String   subscribe_scene;
	private Integer  qr_scene;
	private String  qr_scene_str;

	private List<String>  privilege;
	
}

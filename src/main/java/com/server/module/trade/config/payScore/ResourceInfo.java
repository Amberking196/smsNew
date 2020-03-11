package com.server.module.trade.config.payScore;

import lombok.Data;

@Data
public class ResourceInfo {

	private String algorithm;//AEAD_AES_256_GCM
	private String ciphertext;//Base64编码后的开启结果数据密文
	private String associated_data;// 附加数据
	private String nonce;//加密用的随机字符串
	
}

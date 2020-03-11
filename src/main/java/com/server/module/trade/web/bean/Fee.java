package com.server.module.trade.web.bean;

import lombok.Data;

@Data
public class Fee {

	public Fee() {
		// TODO Auto-generated constructor stub
	}
	private String  fee_name;//付费项目名称
	private Integer  fee_count;//付费数目
	private Integer  fee_amount;//此付费项目总金额，大于等于0，单位为分，等于0时代表不需要扣费
	private String   fee_desc;//描述计费规则，不超过30个字符，超出报错处理
}

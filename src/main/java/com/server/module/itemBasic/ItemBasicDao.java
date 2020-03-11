package com.server.module.itemBasic;

import java.util.List;
import java.util.Map;

import com.server.common.bean.KeyValueBean;
import com.server.module.commonBean.ItemBasicBean;
import com.server.util.ReturnDataUtil;
/**
 * author name: why
 * create time: 2018-04-10 14:22:54
 */ 
public interface  ItemBasicDao{


	/**
	 *  通过商品ID 得到商品信息 
	 * @param id
	 * @return
	 */
	public ItemBasicBean getItemBasic(Object id);

	public StateInfoBean getStateInfoByState(Long state);
	
	/**
	 * 通过id查询商品名称，图片
	 * @author hebiting
	 * @date 2018年12月5日上午10:09:34
	 * @param basicItemIds
	 * @return
	 */
	public List<KeyValueBean<String,String>> getItemByIds(String basicItemIds);
}


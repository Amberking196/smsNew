package com.server.module.itemBasic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.server.common.bean.KeyValueBean;
import com.server.module.commonBean.ItemBasicBean;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
/**
 * author name: why
 * create time: 2018-04-10 14:22:54
 */ 
@Service
public class  ItemBasicServiceImpl  implements ItemBasicService{


	public static Logger log = LogManager.getLogger(ItemBasicServiceImpl.class); 		
	@SuppressWarnings("unused")
	@Autowired
	private ItemBasicDao ItemBasicDaoImpl ;
    @Autowired
    private ReturnDataUtil returnDataUtil;

	
	/**
	 * 通过商品ID 得到商品信息 
	 */
	@Override
	public ItemBasicBean getItemBasic(Object id) {
		log.info("<ItemBasicServiceImpl>--<getItemBasic>--start"); 
		ItemBasicBean re= ItemBasicDaoImpl.getItemBasic(id);
		log.info("<ItemBasicServiceImpl>--<getItemBasic>--end");
		return re; 

	}


	@Override
	public List<KeyValueBean<String, String>> getItemByIds(String basicItemIds) {
		log.info("<ItemBasicServiceImpl>--<getItemByIds>--start"); 
		List<KeyValueBean<String,String>> itemByIds = ItemBasicDaoImpl.getItemByIds(basicItemIds);
		log.info("<ItemBasicServiceImpl>--<getItemByIds>--end"); 
		return itemByIds;
	}


}


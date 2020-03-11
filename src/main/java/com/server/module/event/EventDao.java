package com.server.module.event;

import java.math.BigDecimal;
import java.util.List;


public interface EventDao {

	/**
	 * 事件列表
	 * @author hjc
	 * @date 2019年4月22日下午2:54:34
	 * @return list
	 */
	public List<EventBean> eventList();
	

	/**
	 * 更新事件
	 * @author hjc
	 * @date 2019年4月22日下午2:54:34
	 * @return list
	 */
	public boolean updateEvent(EventBean eventBean);
	
	/**
	 * 删除事件
	 * @author hjc
	 * @date 2019年4月22日下午2:54:34
	 * @return list
	 */
	public boolean insertEvent(EventBean eventBean);

	/**
	 * 删除事件
	 * @author hjc
	 * @date 2019年4月22日下午2:54:34
	 * @return list
	 */
	public boolean delEvent(EventBean eventBean);

}

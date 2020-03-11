package com.server.module.product;

import java.util.List;

import com.server.module.machine.VendingMachinesInfoBean;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-06-29 11:17:55
 */
public interface ShoppingGoodsDao {


	/**
	 * 通过商品id 获取对象信息
	 * @param id
	 * @return
	 */
	public ShoppingGoodsBean get(Object id);

	/**
	 * 查询拼团商品信息  结算页显示
	 * @param shoppingGoodsForm
	 * @return
	 */
	public ReturnDataUtil list(ShoppingGoodsForm shoppingGoodsForm, VendingMachinesInfoBean vmi);
	
	/**
	 * 查询拼团活动商品信息
	 * @param shoppingGoodsForm
	 * @return
	 */
	public List<ShoppingGoodsBean> listPage(VendingMachinesInfoBean vmi);

}

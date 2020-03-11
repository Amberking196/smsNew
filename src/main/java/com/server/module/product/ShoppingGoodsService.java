package com.server.module.product;

import java.util.List;

import com.server.module.machine.VendingMachinesInfoBean;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-06-29 11:17:55
 */
public interface ShoppingGoodsService {

	/**
	 * 查询商品信息
	 * @author why
	 * @date 2019年3月4日 下午3:11:45 
	 * @param goodsId
	 * @return
	 */
	public ShoppingGoodsBean get(Long goodsId);
	
	/**
	 * 查询拼团商品信息  结算页显示
	 * @param shoppingGoodsForm
	 * @return
	 */
	public ReturnDataUtil list(ShoppingGoodsForm shoppingGoodsForm);
	
	/**
	 * 查询拼团活动商品信息
	 * @author why
	 * @date 2019年1月29日 下午5:08:51 
	 * @param vmi
	 * @return
	 */
	public List<ShoppingGoodsBean> listPage( );
}

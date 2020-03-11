package com.server.module.product;

import com.server.module.commonBean.PageAssist;
import lombok.Data;

/**
 * table name: shopping_goods author name: why create time: 2018-06-29 11:17:55
 */
@Data
public class ShoppingGoodsForm extends PageAssist {

	//商品名称
	String name;
	
	//类型  1 首页商品  2 广告商品  3 全部商品
	Integer type;
	
	//商品类型  24 其他日用品     11水饮品
	Integer  itemType;
	
	//价格 排序
	Integer price;
	//新品 排序
	Integer newProduct;
	
	//商品类型
	Integer typeId;
	
	//商品id
	Long	id;
	
	//销售状态  5100 正常销售  5101 暂停销售
	Long state;

}

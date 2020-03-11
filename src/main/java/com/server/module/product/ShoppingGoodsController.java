package com.server.module.product;

import com.server.util.ReturnDataUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author name: why create time: 2018-06-29 11:17:55
 */
@Api(value = "ShoppingGoodsController", description = "商城商品")
@RestController
@RequestMapping("/shoppingGoods")
public class ShoppingGoodsController {

	private static Logger log = LogManager.getLogger(ShoppingGoodsController.class);
	@Autowired
	private ShoppingGoodsService shoppingGoodsServiceImpl;

	/**
	 * 查询拼团商品信息  结算页显示
	 * @param shoppingGoodsForm
	 * @return
	 */
	@ApiOperation(value = "查询拼团商品信息  结算页显示", notes = "list", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil list(@RequestBody(required = false) ShoppingGoodsForm shoppingGoodsForm) {
		log.info("<ShoppingGoodsController>----<list>----start");
		ReturnDataUtil returnDataUtil = new  ReturnDataUtil();
		if (shoppingGoodsForm == null) {
			shoppingGoodsForm = new ShoppingGoodsForm();
		}
		returnDataUtil = shoppingGoodsServiceImpl.list(shoppingGoodsForm);
		log.info("<ShoppingGoodsController>----<list>----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "查询拼团活动商品信息", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage() {
		log.info("<ShoppingGoodsController>----<list>----start");
		ReturnDataUtil returnDataUtil = new  ReturnDataUtil();
		returnDataUtil.setReturnObject(shoppingGoodsServiceImpl.listPage());
		log.info("<ShoppingGoodsController>----<listPage>----end");
		return returnDataUtil;
	}

}

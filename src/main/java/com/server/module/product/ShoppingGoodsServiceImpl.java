package com.server.module.product;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.machine.MachineInfoService;
import com.server.module.machine.VendingMachinesInfoBean;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.customer.TblCustomerBean;
import com.server.module.trade.customer.TblCustomerService;
import com.server.module.trade.util.UserUtil;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-06-29 11:17:55
 */
@Service
public class ShoppingGoodsServiceImpl implements ShoppingGoodsService {

	private static Logger log = LogManager.getLogger(ShoppingGoodsServiceImpl.class);
	@Autowired
	private ShoppingGoodsDao shoppingGoodsDaoImpl;
    @Autowired
    private TblCustomerService tblCustomerService;
    @Autowired
    private MachineInfoService machineInfoServiceImpl;
     

    @Override
	public ShoppingGoodsBean get(Long goodsId) {
    	log.info("<ShoppingGoodsServiceImpl>----<get>----start");
    	ShoppingGoodsBean bean = shoppingGoodsDaoImpl.get(goodsId);
    	log.info("<ShoppingGoodsServiceImpl>----<get>----end");
    	return bean;
	} 
    
	/**
	 * 查询商品信息 给手机端 显示
	 */
	@Override
	public ReturnDataUtil list(ShoppingGoodsForm shoppingGoodsForm) {
		log.info("<ShoppingGoodsServiceImpl>----<list>----start");
		UserVo u=UserUtil.getUser();
		VendingMachinesInfoBean vmi=null;
		if(u!=null) {
			Long userId=u.getId();
			TblCustomerBean customer=tblCustomerService.getCustomerById(userId);
			if(customer!=null) {
				String vmCode = customer.getVmCode();
				vmi=machineInfoServiceImpl.findVendingMachinesByCode(vmCode);
			}
		}
		ReturnDataUtil list = shoppingGoodsDaoImpl.list(shoppingGoodsForm,vmi);
		log.info("<ShoppingGoodsServiceImpl>----<list>----end");
		return list;
	}


	@Override
	public List<ShoppingGoodsBean> listPage() {
		log.info("<ShoppingGoodsServiceImpl>----<listPage>----start");
		UserVo u=UserUtil.getUser();
		VendingMachinesInfoBean vmi=null;
		if(u!=null) {
			Long userId=u.getId();
			TblCustomerBean customer=tblCustomerService.getCustomerById(userId);
			if(customer!=null) {
				String vmCode = customer.getVmCode();
				vmi=machineInfoServiceImpl.findVendingMachinesByCode(vmCode);
			}
		}
		 List<ShoppingGoodsBean> list = shoppingGoodsDaoImpl.listPage(vmi);
		log.info("<ShoppingGoodsServiceImpl>----<listPage>----end");
		return list;
	}


	


}

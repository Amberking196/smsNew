package com.server.module.coupon;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.trade.customer.TblCustomerDao;
import com.server.module.zfb_trade.module.customer.CustomerBean;
import com.server.util.stateEnum.CouponEnum;

@Service
public class CouponServiceImpl implements CouponService{

	private static final Logger log = LogManager.getLogger(CouponServiceImpl.class);

	@Autowired
	private CouponDao couponDao;
	@Autowired
	private TblCustomerDao tblCustomerDao;
	
	@Override
	public List<CouponBean> getCoupon(Long customerId, String vmCode,Double money,Long productId) {
		//log.info("<CouponServiceImpl--getCoupon--start>");
		MachinesLAC machinesLAC = couponDao.getMachinesLAC(vmCode);
		if(machinesLAC == null){
			log.info("<CouponServiceImpl--getCoupon--end>");
			return null;
		}
		List<CouponBean> coupon = couponDao.getCoupon(customerId, machinesLAC,money,productId);
		//log.info("<CouponServiceImpl--getCoupon--end>");
		return coupon;
	}

	@Override
	public boolean updateCouponCustomer(Long couponCustomerId) {
		//log.info("<CouponServiceImpl--updateCouponCustomer--start>");
		boolean updateCouponCustomer = couponDao.updateCouponCustomer(couponCustomerId);
		//log.info("<CouponServiceImpl--updateCouponCustomer--end>");
		return updateCouponCustomer;
	}

	@Override
	public boolean updateCouponCustomer(Long couponId, Long customer,Integer quantity) {
		//log.info("<CouponServiceImpl--updateCouponCustomer--start>");
		boolean updateCouponCustomer = couponDao.updateCouponCustomer(couponId, customer,quantity);
		//log.info("<CouponServiceImpl--updateCouponCustomer--end>");
		return updateCouponCustomer;
	}
	
	@Override
	public Integer insertCouponCustomer(CouponCustomerBean couCusBean) {
		//log.info("<CouponServiceImpl--insertCouponCustomer--start>");
		Integer insertCouponCustomer = couponDao.insertCouponCustomer(couCusBean);
		//log.info("<CouponServiceImpl--insertCouponCustomer--start>");
		return insertCouponCustomer;
	}

	@Override
	public Integer insertCouponCustomerWithSumQuantity(CouponCustomerBean couCusBean) {
		//log.info("<CouponServiceImpl--insertCouponCustomerWithSumQuantity--start>");
		Integer insertCouponCustomer = couponDao.insertCouponCustomerWithSumQuantity(couCusBean);
		//log.info("<CouponServiceImpl--insertCouponCustomerWithSumQuantity--start>");
		return insertCouponCustomer;
	}
	
	@Override
	public MachinesLAC getMachinesLAC(String vmCode) {
		//log.info("<CouponServiceImpl--getMachinesLAC--start>");
		MachinesLAC machinesLAC = couponDao.getMachinesLAC(vmCode);
		//log.info("<CouponServiceImpl--getMachinesLAC--end>");
		return machinesLAC;
	}

	@Override
	public List<CouponBean> getPresentCoupon(CouponForm couponForm) {
		//log.info("<CouponServiceImpl--getPresentCoupon--start>");
		List<CouponBean> presentCoupon = couponDao.getPresentCoupon(couponForm);
		//log.info("<CouponServiceImpl--getPresentCoupon--end>");
		return presentCoupon;
	}

	@Override
	public boolean isReceive(Long customerId, Long couponId) {
		//log.info("<CouponServiceImpl--receiveNum--start>");
		boolean receiveNum = couponDao.isReceive(customerId, couponId);
		//log.info("<CouponServiceImpl--receiveNum--end>");
		return receiveNum;
	}

	
	@Override
	public boolean updateIntegral(Long customerId,Long num) {
		//log.info("<CouponServiceImpl--updateIntegral--start>");
		boolean updateIntegral = couponDao.updateIntegral(customerId,num);
		//log.info("<CouponServiceImpl--updateIntegral--end>");
		return updateIntegral;
	}


	@Override
	public List<CouponDto> getAvailableCoupon(Long customerId, MachinesLAC mlac) {
		//log.info("<CouponServiceImpl--getAvailableCoupon--start>");
		List<CouponDto> availableCoupon = couponDao.getAvailableCoupon(customerId, mlac);
		//log.info("<CouponServiceImpl--getAvailableCoupon--end>");
		return availableCoupon;
	}

	@Override
	public CouponBean getCouponInfo(Long couponId) {
		//log.info("<CouponServiceImpl--getCouponInfo--start>");
		CouponBean couponInfo = couponDao.getCouponInfo(couponId);
		//log.info("<CouponServiceImpl--getCouponInfo--end>");
		return couponInfo;
	}

	@Override
	public List<CouponBean> getCouponInfos(String couponIds) {
		//log.info("<CouponServiceImpl--getCouponInfos--start>");
		List<CouponBean> couponInfos = couponDao.getCouponInfos(couponIds);
		//log.info("<CouponServiceImpl--getCouponInfos--end>");
		return couponInfos;
	}

	@Override
	public MoreGoodsCouponDto getMoreGoodsCoupon(Long customerId, MachinesLAC mlac, Double money, String productIds) {
		//log.info("<CouponServiceImpl--getMoreGoodsCoupon--start>");
		MoreGoodsCouponDto moreGoodsCoupon = couponDao.getMoreGoodsCoupon(customerId, mlac, money, productIds);
		//log.info("<CouponServiceImpl--getMoreGoodsCoupon--end>");
		return moreGoodsCoupon;
	}
	

	@Override
	public List<CouponBean> myCoupon(CouponForm couponForm){
		//log.info("<CouponServiceImpl>-----<myCoupon>-----start>");
		List<CouponBean> list = couponDao.myCoupon(couponForm);
		//log.info("<CouponServiceImpl>-----<myCoupon>-----end>");
		return list;
	}
	
	public void sendInviteCoupon(CustomerBean customer) {
		if(customer.getInviterId()>0 && tblCustomerDao.isFirstBuy(customer.getId()).equals(1) && tblCustomerDao.isStoreFirstBuy(customer.getId()).equals(0)){
			CouponForm couponForm = new CouponForm();
			couponForm.setWay(CouponEnum.INVITE_COUPON.getState());
			couponForm.setLimitRange(false);
			List<CouponBean> presentCoupon = couponDao.getPresentCoupon(couponForm);
			if(presentCoupon != null && presentCoupon.size()>0) {
				for(CouponBean coupon:presentCoupon) {
					CouponCustomerBean couCusBean = new CouponCustomerBean();
					couCusBean.setQuantity(coupon.getSendMax());
					couCusBean.setCouponId(coupon.getId());
					couCusBean.setCustomerId(customer.getInviterId());
					couCusBean.setStartTime(coupon.getLogicStartTime());
					couCusBean.setEndTime(coupon.getLogicEndTime());
					couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());//状态：0：新建，1：领取，2：已使用
					couponDao.insertCouponCustomer(couCusBean);
				}
			}
		}
	}

}

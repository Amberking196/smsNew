package com.server.module.trade.customer;

import com.server.module.carryWaterVouchersManage.carryWaterVouchersCustomer.CarryWaterVouchersCustomerDto;
import com.server.module.carryWaterVouchersManage.carryWaterVouchersCustomer.CarryWaterVouchersCustomerForm;
import com.server.module.carryWaterVouchersManage.carryWaterVouchersCustomer.CarryWaterVouchersCustomerService;
import com.server.module.coupon.CouponBean;
import com.server.module.coupon.CouponForm;
import com.server.module.coupon.CouponService;
import com.server.module.trade.customer.complain.TblCustomerComplainBean;
import com.server.module.trade.customer.complain.TblCustomerComplainForm;
import com.server.module.trade.customer.complain.TblCustomerComplainService;
import com.server.module.trade.memberOrder.MemberOrderBean;
import com.server.module.trade.memberOrder.MemberOrderService;
import com.server.module.trade.util.UserUtil;
import com.server.util.ReturnDataUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * author name: yjr create time: 2018-04-16 15:06:05
 */
@Api(value = "TblCustomerController", description = "客户")
@RestController
@RequestMapping("/tblCustomer")
public class TblCustomerController {

    private static Logger log= LogManager.getLogger(TblCustomerController.class);
    @Autowired
    private TblCustomerService tblCustomerServiceImpl;
    @Autowired
    private ReturnDataUtil returnDataUtil;
    @Autowired
    private CouponService couponServiceImpl;
    @Autowired
    private CarryWaterVouchersCustomerService carryWaterVouchersCustomerServiceImpl;
    @Autowired
    private TblCustomerComplainService tblCustomerComplainServiceImpl;
    @Autowired
    private MemberOrderService memberOrderServiceImpl;


    @ApiOperation(value = "我的信息", notes = "myInfo", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/myInfo", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil myInfo() {
        log.info("<TblCustomerController>-----<myInfo>-----start");
        Long customerId= UserUtil.getUser().getId();
        TblCustomerBean bean= tblCustomerServiceImpl.getCustomerBeanById(customerId);
        if(bean!=null){
            returnDataUtil.setStatus(1);
            returnDataUtil.setMessage("我的信息查看成功");
            returnDataUtil.setReturnObject(bean);
            if(bean.getCompanyId()==0) {
            	 returnDataUtil.setStatus(-66);
                 returnDataUtil.setMessage("非优水用户，目前不能进行余额充值！");
            }
        }else{
            returnDataUtil.setStatus(0);
            returnDataUtil.setMessage("我的信息查看失败");
            returnDataUtil.setReturnObject(bean);
        }
        log.info("<TblCustomerController>-----<myInfo>-----end");
        return returnDataUtil;
    }


    @ApiOperation(value = "我的优惠券", notes = "myCoupon", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/myCoupon", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil myCoupon(@RequestBody  CouponForm couponForm) {
        log.info("<TblCustomerController>-----<myCoupon>-----start");
        List<CouponBean> list=couponServiceImpl.myCoupon(couponForm);
        if(list.size()>0 && list!=null){
            returnDataUtil.setStatus(1);
            returnDataUtil.setMessage("我的优惠券查看成功");
            returnDataUtil.setReturnObject(list);
        }else{
            returnDataUtil.setStatus(0);
            returnDataUtil.setMessage("我的优惠券查看失败");
            returnDataUtil.setReturnObject(list);
        }
        log.info("<TblCustomerController>-----<myCoupon>-----end");
        return returnDataUtil;
    }

    @ApiOperation(value = "我的提水券", notes = "myCarryWaterVouchers", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/myCarryWaterVouchers", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil myCarryWaterVouchers(@RequestBody CarryWaterVouchersCustomerForm carryWaterVouchersCustomerForm) {
        log.info("<TblCustomerController>-----<myCarryWaterVouchers>-----start");
         List<CarryWaterVouchersCustomerDto> list = carryWaterVouchersCustomerServiceImpl.myCarryWaterVouchers(carryWaterVouchersCustomerForm);
        if(list.size()>0 && list!=null){
            returnDataUtil.setStatus(1);
            returnDataUtil.setMessage("我的提水券查看成功");
            returnDataUtil.setReturnObject(list);
        }else{
            returnDataUtil.setStatus(0);
            returnDataUtil.setMessage("我的提水券查看失败");
            returnDataUtil.setReturnObject(list);
        }
        log.info("<TblCustomerController>-----<myCarryWaterVouchers>-----end");
        return returnDataUtil;
    }


    @ApiOperation(value = "我的故障申报", notes = "myDeclaration", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/myDeclaration", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil myDeclaration(@RequestBody(required = false) TblCustomerComplainForm tblCustomerComplainForm)  {
        log.info("<TblCustomerController>-----<myInviteRewards>-----start");
        if(tblCustomerComplainForm==null){
            tblCustomerComplainForm=new TblCustomerComplainForm();
        }
        List<TblCustomerComplainBean> list = tblCustomerComplainServiceImpl.myDeclaration(tblCustomerComplainForm);
        if(list.size()>0 && list!=null){
            returnDataUtil.setStatus(1);
            returnDataUtil.setMessage("我的故障申报查看成功");
            returnDataUtil.setReturnObject(list);
        }else{
            returnDataUtil.setStatus(0);
            returnDataUtil.setMessage("我的故障申报查看失败");
            returnDataUtil.setReturnObject(list);
        }
        log.info("<TblCustomerController>-----<myInviteRewards>-----end");
        return returnDataUtil;
    }

    @ApiOperation(value = "我的邀请奖励", notes = "myInviteRewards", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/myInviteRewards", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil myInviteRewards() {
        log.info("<TblCustomerController>-----<myInviteRewards>-----start");
        List<TblCustomerBean> list=tblCustomerServiceImpl.myInviteRewards();
        if(list.size()>0 && list!=null){
            returnDataUtil.setStatus(1);
            returnDataUtil.setMessage("我的邀请奖励查看成功");
            returnDataUtil.setReturnObject(list);
        }else{
            returnDataUtil.setStatus(0);
            returnDataUtil.setMessage("我的邀请奖励查看失败");
            returnDataUtil.setReturnObject(list);
        }
        log.info("<TblCustomerController>-----<myInviteRewards>-----end");
        return returnDataUtil;
    }

    @ApiOperation(value = "新增用户充值订单", notes = "addOrder", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/addOrder", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil addOrder(@RequestBody MemberOrderBean entity) {
        log.info("<TblCustomerController>-----<addOrder>------start");
        returnDataUtil = memberOrderServiceImpl.add(entity);
        log.info("<TblCustomerController>-----<addOrder>------end");
        return returnDataUtil;
    }



}

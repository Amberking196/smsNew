package com.server.module.trade.customer.address;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.trade.util.UserUtil;
import com.server.module.zfb_trade.util.ResultUtil;
import com.server.util.ReturnDataUtil;

@RestController
@RequestMapping("/address")
public class AddressController {

	@Autowired
	private AddressService addressService;
	
	/**
	 * 新增地址
	 * @author hebiting
	 * @date 2018年7月6日上午10:11:53
	 * @return
	 */
	@PostMapping("/add")
	public ReturnDataUtil addAddress(@RequestBody AddressBean address,HttpServletRequest request){
		if(address.getCustomerId()==null){
			address.setCustomerId(UserUtil.getUser().getId());
		}
		if(address.getDefaultFlag()==1){
			addressService.updateDefaultFlag(address.getCustomerId());
		}
		AddressBean insert = addressService.insert(address);
		if(insert != null){
			return ResultUtil.success();
		}
		return ResultUtil.error();
	}
	
	/**
	 * 修改地址信息
	 * @author hebiting
	 * @date 2018年7月6日上午10:15:58
	 * @param address
	 * @return
	 */
	@PostMapping("/update")
	public ReturnDataUtil updateAddress(@RequestBody AddressBean address,HttpServletRequest request){
		address.setUpdateTime(new Date());
		if(address.getCustomerId()==null){
			address.setCustomerId(UserUtil.getUser().getId());
		}
		if(address.getDefaultFlag()==1 && addressService.updateDefaultFlag(address.getCustomerId())){
			boolean update = addressService.update(address);
			if(update){
				return ResultUtil.success();
			}
		}else{
			boolean update = addressService.update(address);
			if(update){
				return ResultUtil.success();
			}
		}
		return ResultUtil.error();
	}
	
	/**
	 * 根据用户id查询地址信息
	 * @author hebiting
	 * @date 2018年7月6日上午10:20:12
	 * @param customerId
	 * @return
	 */
	@PostMapping("/select")
	public ReturnDataUtil selectAddress(@RequestParam(required=false)Long customerId,HttpServletRequest request){
		if(customerId==null){
			customerId = UserUtil.getUser().getId();
		}
		List<AddressBean> addressList = addressService.select(customerId);
		if(addressList!=null && addressList.size()>0){
			return ResultUtil.success(addressList);
		}
		return ResultUtil.selectError();
	}
	
	/**
	 * 根据用户id查询地址信息
	 * @author hebiting
	 * @date 2018年7月6日上午10:20:12
	 * @param customerId
	 * @return
	 */
	@PostMapping("/selectById")
	public ReturnDataUtil selectAddressById(@RequestParam(required=true)Integer id,HttpServletRequest request){
		Long customerId = UserUtil.getUser().getId();
		AddressBean address = addressService.selectById(customerId,id);
		if(address!=null){
			return ResultUtil.success(address);
		}
		return ResultUtil.selectError();
	}
	
	/**
	 * 真正删除用户地址
	 * @author hebiting
	 * @date 2018年7月6日上午11:18:30
	 * @param addressId
	 * @return
	 */
	@PostMapping("/delete")
	public ReturnDataUtil deleteAddress(Long addressId){
		boolean delete = addressService.delete(addressId);
		if(delete){
			return ResultUtil.success();
		}
		return ResultUtil.error();
	}
}

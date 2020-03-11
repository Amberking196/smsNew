package com.server.module.trade.customer.complain;

import com.server.module.trade.customer.TblCustomerDao;
import com.server.module.trade.util.UserUtil;
import com.server.util.EmojiUtil;
import com.server.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author name: yjr create time: 2018-08-17 08:48:16
 */
@Service
public class TblCustomerComplainServiceImpl implements TblCustomerComplainService {

	private static Logger log = LogManager.getLogger(TblCustomerComplainServiceImpl.class);
	@Autowired
	private TblCustomerComplainDao tblCustomerComplainDaoImpl;
	@Autowired
	private TblCustomerDao tblCustomerDaoImpl;


	public TblCustomerComplainBean add(TblCustomerComplainBean entity) {
		log.info("<TblCustomerComplainServiceImpl>----<add>------start");
		// 得到当前 投诉的用户 的手机号
		Long customerId = UserUtil.getUser().getId();
		if(!(StringUtil.isNotBlank(entity.getPhone()))){
			String phone = tblCustomerDaoImpl.getPhoneByCustomerId(customerId);
			entity.setPhone(phone);
		}
		if(!(StringUtil.isNotBlank(entity.getContent()))){
			entity.setContent(EmojiUtil.getString(entity.getContent()));
		}
		if(StringUtil.isBlank(entity.getVmCode()) || entity.getVmCode().equals("1988000080") || entity.getVmCode().equals("undefined")){
			//售货机编号异常时查询用户最近一次购买售货机的编号
			String vmCode = tblCustomerDaoImpl.getCustomerLastVmcode(customerId);
			if(vmCode!=null) {
				entity.setVmCode(vmCode);
			}
		}
		entity.setCustomerId(customerId);
		entity.setCreateUser(customerId);
		TblCustomerComplainBean tblCustomerComplainBean = tblCustomerComplainDaoImpl.insert(entity);
		log.info("<TblCustomerComplainServiceImpl>----<add>------end");
		return tblCustomerComplainBean;
	}


	public List<TblCustomerComplainBean> myDeclaration(TblCustomerComplainForm tblCustomerComplainForm){
		log.info("<CustomerMessageServiceImpl>----<myDeclaration>------start");
		List<TblCustomerComplainBean> list = tblCustomerComplainDaoImpl.myDeclaration(tblCustomerComplainForm);
		log.info("<CustomerMessageServiceImpl>----<myDeclaration>------end");
		return  list;
	}

	public boolean update(Long  id){
		log.info("<CustomerMessageServiceImpl>----<update>------start");
		boolean update = tblCustomerComplainDaoImpl.update(id);
		log.info("<CustomerMessageServiceImpl>----<update>------end");
		return  update;
	}

	public  Integer findComplaintsNumberById(){
		log.info("<CustomerMessageServiceImpl>----<findComplaintsNumberById>------start");
		Integer count = tblCustomerComplainDaoImpl.findComplaintsNumberById();
		log.info("<CustomerMessageServiceImpl>----<findComplaintsNumberById>------end");
		return count;
	}
}

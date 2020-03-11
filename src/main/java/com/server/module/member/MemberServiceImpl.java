package com.server.module.member;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService{

	
	private final static Logger log = LogManager.getLogger(MemberServiceImpl.class);
	@Autowired
	private MemberDao memberDao;
	
	@Override
	public MemberBean getMemberInfo(Long customerId) {
		log.info("<MemberServiceImpl--getMemberInfo--start>");
		MemberBean memberInfo = memberDao.getMemberInfo(customerId);
		log.info("<MemberServiceImpl--getMemberInfo--end>");
		return memberInfo;
	}

	@Override
	public BigDecimal getMemberMoney(Long customerId) {
		log.info("<MemberServiceImpl--getMemberInfo--start>");
		BigDecimal memberMoney = memberDao.getMemberMoney(customerId);
		log.info("<MemberServiceImpl--getMemberInfo--end>");
		return memberMoney;
	}

	@Override
	public boolean updateMemberMoney(Long customerId, BigDecimal money) {
		log.info("<MemberServiceImpl--getMemberInfo--start>");
		boolean updateMemberMoney = memberDao.updateMemberMoney(customerId, money);
		log.info("<MemberServiceImpl--getMemberInfo--end>");
		return updateMemberMoney;
	}

	@Override
	public Long insertMemberUseLog(MemberUseLog memberLog) {
		log.info("<MemberServiceImpl--insertMemberUseLog--start>");
		Long id = memberDao.insertMemberUseLog(memberLog);
		log.info("<MemberServiceImpl--insertMemberUseLog--end>");
		return id;
	}

}

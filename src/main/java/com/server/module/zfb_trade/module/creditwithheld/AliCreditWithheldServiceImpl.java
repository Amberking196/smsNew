package com.server.module.zfb_trade.module.creditwithheld;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("aliCreditWithheldService")
public class AliCreditWithheldServiceImpl implements AliCreditWithheldService{

	public static Logger log = LogManager.getLogger(AliCreditWithheldServiceImpl.class); 
	@Autowired
	@Qualifier("aliCreditWithheldDao")
	private AliCreditWithheldDao creditWithheldDao;
	@Override
	public CreditWithheldBean queryByAliId(CreditWithheldBean creditWithheld) {
		log.info("<AliCreditWithheldServiceImpl--queryByAliId--start>");
		CreditWithheldBean queryByAliId = creditWithheldDao.queryByAliId(creditWithheld);
		log.info("<AliCreditWithheldServiceImpl--queryByAliId--end>");
		return queryByAliId;
	}

	@Override
	public boolean save(CreditWithheldBean creditWithheld) {
		log.info("<AliCreditWithheldServiceImpl--save--start>");
		boolean save = creditWithheldDao.save(creditWithheld);
		log.info("<AliCreditWithheldServiceImpl--save--end>");
		return save;
	}

	@Override
	public boolean update(CreditWithheldBean creditWithheld) {
		log.info("<AliCreditWithheldServiceImpl--update--start>");
		boolean update = creditWithheldDao.update(creditWithheld);
		log.info("<AliCreditWithheldServiceImpl--update--end>");
		return update;
	}

}

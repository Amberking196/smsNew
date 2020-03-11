package com.server.module.trade.customer.complainReply;

import com.server.util.EmojiUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author name: yjr
 * create time: 2018-12-04 11:17:52
 */
@Service
public class TblCustomerComplainReplyServiceImpl implements TblCustomerComplainReplyService {

    private static Logger log = LogManager.getLogger(TblCustomerComplainReplyServiceImpl.class);
    @Autowired
    private TblCustomerComplainReplyDao tblCustomerComplainReplyDaoImpl;

    public ReturnDataUtil listPage(TblCustomerComplainReplyForm condition) {
        return tblCustomerComplainReplyDaoImpl.listPage(condition);
    }

    public TblCustomerComplainReplyBean add(TblCustomerComplainReplyBean entity) {
        if(!(StringUtil.isNotBlank(entity.getContent()))){
            entity.setContent(EmojiUtil.getString(entity.getContent()));
        }
        return tblCustomerComplainReplyDaoImpl.insert(entity);
    }

    public boolean update(TblCustomerComplainReplyBean entity) {
        return tblCustomerComplainReplyDaoImpl.update(entity);
    }

    public boolean del(Object id) {
        return tblCustomerComplainReplyDaoImpl.delete(id);
    }

    public List<TblCustomerComplainReplyBean> list(Long complainId) {
        log.info("<TblCustomerComplainReplyServiceImpl>------<list>-----start");
        List<TblCustomerComplainReplyBean> list = tblCustomerComplainReplyDaoImpl.list(complainId);
        log.info("<TblCustomerComplainReplyServiceImpl>------<list>-----end");
        return list;
    }

    public TblCustomerComplainReplyBean get(Object id) {
        return tblCustomerComplainReplyDaoImpl.get(id);
    }
}


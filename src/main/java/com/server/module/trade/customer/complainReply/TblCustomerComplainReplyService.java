package com.server.module.trade.customer.complainReply;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: why
 * create time: 2018-12-04 11:17:52
 */
public interface TblCustomerComplainReplyService {


    public ReturnDataUtil listPage(TblCustomerComplainReplyForm condition);

    /**
     * 根据故障申报id 查询回复信息
     * @param:complainId
     * @return:
     * @auther: why
     * @date: 2018/12/4 11:48
     */
    public List<TblCustomerComplainReplyBean> list(Long complainId);


    public boolean update(TblCustomerComplainReplyBean entity);

    public boolean del(Object id);

    public TblCustomerComplainReplyBean get(Object id);

    public TblCustomerComplainReplyBean add(TblCustomerComplainReplyBean entity);
}


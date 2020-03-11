package com.server.module.trade.customer.complainReply;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;
import lombok.Data;

import java.util.Date;

/**
 * table name:  tbl_customer_complain_reply
 * author name: why
 * create time: 2018-12-04 11:17:52
 */
@Data
@Entity(tableName = "tbl_customer_complain_reply", id = "id", idGenerate = "auto")
public class TblCustomerComplainReplyBean {

    //主键id
    private Long id;
    //故障申报id
    private Long complainId;
    //
    private Long pid;
    //回复源  0.用户  1.客服
    private Integer src;
    //回复内容
    private String content;
    //创建时间
    private Date createTime;
    //创建用户
    private Long createUser;
    //用户名'
    private String createName;

    @NotField
    private  String createTimes;



}


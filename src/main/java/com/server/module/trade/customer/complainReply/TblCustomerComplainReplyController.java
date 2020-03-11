package com.server.module.trade.customer.complainReply;

import com.server.module.trade.customer.complain.TblCustomerComplainService;
import com.server.module.trade.util.UserUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;

import java.util.*;

/**
 * author name: why
 * create time: 2018-12-04 11:17:52
 */
@Api(value = "TblCustomerComplainReplyController", description = "用户故障申报回复")
@RestController
@RequestMapping("/tblCustomerComplainReply")
public class TblCustomerComplainReplyController {

    private static Logger log= LogManager.getLogger(TblCustomerComplainReplyController.class);

    @Autowired
    private TblCustomerComplainReplyService tblCustomerComplainReplyServiceImpl;
    @Autowired
    private ReturnDataUtil returnDataUtil;
    @Autowired
    private TblCustomerComplainService tblCustomerComplainServiceImpl;


    @ApiOperation(value = "用户故障申报回复添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil add(@RequestBody TblCustomerComplainReplyBean entity) {
        log.info("<TblCustomerComplainReplyController>------<add>-----start");
        entity.setSrc(0);
        entity.setCreateUser(UserUtil.getUser().getId());
        TblCustomerComplainReplyBean bean = tblCustomerComplainReplyServiceImpl.add(entity);
        if(bean!=null){
            //更新状态
            boolean update = tblCustomerComplainServiceImpl.update(entity.getComplainId());
            if(update){
                returnDataUtil.setStatus(1);
                returnDataUtil.setMessage("提问成功！");
            }else {
                returnDataUtil.setStatus(0);
                returnDataUtil.setMessage("提问失败！");
            }
        }else{
            returnDataUtil.setStatus(0);
            returnDataUtil.setMessage("提问失败！");
        }
        log.info("<TblCustomerComplainReplyController>------<add>-----end");
        return returnDataUtil;
    }

    @ApiOperation(value = "用户故障申报回复次数判断", notes = "isReply", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/isReply", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil isReply(Long complainId) {
        log.info("<TblCustomerComplainReplyController>------<isReply>-----start");
        List<TblCustomerComplainReplyBean> list = tblCustomerComplainReplyServiceImpl.list(complainId);
        if(list!=null && list.size()>0){
            int count=0;
            for (TblCustomerComplainReplyBean bean : list) {
                    if(bean.getSrc()==0){
                        count++;
                    }
            }
            if(count>=3){
                returnDataUtil.setStatus(0);
                returnDataUtil.setMessage("亲，提问已经三次了，不可以继续提问了哟！亲，如还有问题，请电话联系客服，谢谢！");
            }else{
                returnDataUtil.setStatus(1);
                returnDataUtil.setMessage("可以继续提问！");
            }
        }else{
            returnDataUtil.setStatus(1);
            returnDataUtil.setMessage("可以继续提问！");
        }
        log.info("<TblCustomerComplainReplyController>------<isReply>-----end");
        return returnDataUtil;
    }

    @ApiOperation(value = "定时器使用查询接口", notes = "replyDetails", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/replyDetails", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil replyDetails(Long[]  complainIds) {
        log.info("<TblCustomerComplainReplyController>------<replyDetails>-----start");
        List<List<TblCustomerComplainReplyBean>> listAll=new ArrayList<List<TblCustomerComplainReplyBean>>();
        //进行排序
        Arrays.sort(complainIds);
        for (int i=complainIds.length-1;i>=0 ;i-- )
        {
            log.info("============complainId==========="+complainIds[i]);
            List<TblCustomerComplainReplyBean>   list= tblCustomerComplainReplyServiceImpl.list(complainIds[i]);
            listAll.add(list);
        }
        if(listAll!=null&&listAll.size()>0){
            returnDataUtil.setStatus(1);
            returnDataUtil.setReturnObject(listAll);
        }else{
            returnDataUtil.setStatus(0);
            returnDataUtil.setReturnObject(listAll);
        }
        log.info("<TblCustomerComplainReplyController>------<replyDetails>-----end");
        return returnDataUtil;
    }
}


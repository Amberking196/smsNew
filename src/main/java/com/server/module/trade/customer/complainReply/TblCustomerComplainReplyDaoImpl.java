package com.server.module.trade.customer.complainReply;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.util.EmojiUtil;
import com.server.util.ReturnDataUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * author name: yjr
 * create time: 2018-12-04 11:17:52
 */
@Repository
public class TblCustomerComplainReplyDaoImpl extends BaseDao<TblCustomerComplainReplyBean> implements TblCustomerComplainReplyDao {

    private static Logger log = LogManager.getLogger(TblCustomerComplainReplyDaoImpl.class);

    public ReturnDataUtil listPage(TblCustomerComplainReplyForm condition) {
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append("select id,complainId,pid,src,content,createTime,createUser,createName from tbl_customer_complain_reply where 1=1 ");
        List<Object> plist = Lists.newArrayList();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(super.countSql(sql.toString()));
            if (plist != null && plist.size() > 0)
                for (int i = 0; i < plist.size(); i++) {
                    pst.setObject(i + 1, plist.get(i));
                }
            rs = pst.executeQuery();
            long count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
            pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());

            if (plist != null && plist.size() > 0)
                for (int i = 0; i < plist.size(); i++) {
                    pst.setObject(i + 1, plist.get(i));
                }
            rs = pst.executeQuery();
            List<TblCustomerComplainReplyBean> list = Lists.newArrayList();
            while (rs.next()) {
                TblCustomerComplainReplyBean bean = new TblCustomerComplainReplyBean();
                bean.setId(rs.getLong("id"));
                bean.setComplainId(rs.getLong("complainId"));
                bean.setPid(rs.getLong("pid"));
                bean.setSrc(rs.getInt("src"));
                bean.setContent(EmojiUtil.getString(rs.getString("content")));
                bean.setCreateTime(rs.getDate("createTime"));
                bean.setCreateUser(rs.getLong("createUser"));
                bean.setCreateName(rs.getString("createName"));
                list.add(bean);
            }
            if (showSql) {
                log.info(sql);
                log.info(plist.toString());
            }
            data.setCurrentPage(condition.getCurrentPage());
            data.setTotal(count);
            data.setReturnObject(list);
            data.setStatus(1);
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return data;
        } finally {
            try {
                rs.close();
                pst.close();
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public TblCustomerComplainReplyBean get(Object id) {
        return super.get(id);
    }

    public boolean delete(Object id) {
        TblCustomerComplainReplyBean entity = new TblCustomerComplainReplyBean();
        return super.del(entity);
    }

    public boolean update(TblCustomerComplainReplyBean entity) {
        return super.update(entity);
    }

    public TblCustomerComplainReplyBean insert(TblCustomerComplainReplyBean entity) {
        return super.insert(entity);
    }

    public List<TblCustomerComplainReplyBean> list(Long complainId) {
        log.info("<TblCustomerComplainReplyDaoImpl>-------<list>------start");
        StringBuilder sql = new StringBuilder();
        sql.append("select id,complainId,pid,src,content,createTime,createUser,createName ");
        sql.append(" from tbl_customer_complain_reply where 1=1 and complainId='"+complainId+"' ");
        List<TblCustomerComplainReplyBean> list = Lists.newArrayList();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        log.info("根据故障申报id 查询回复信息 sql语句:" + sql.toString());
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                TblCustomerComplainReplyBean bean = new TblCustomerComplainReplyBean();
                bean.setId(rs.getLong("id"));
                bean.setComplainId(rs.getLong("complainId"));
                bean.setPid(rs.getLong("pid"));
                bean.setSrc(rs.getInt("src"));
                bean.setContent(EmojiUtil.getString(rs.getString("content")));
                bean.setCreateTimes(rs.getString("createTime").substring(0, rs.getString("createTime").indexOf(".")));
                bean.setCreateUser(rs.getLong("createUser"));
                bean.setCreateName(rs.getString("createName"));
                list.add(bean);
            }
            log.info("<TblCustomerComplainReplyDaoImpl>-------<list>------end");
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            log.info("<TblCustomerComplainReplyDaoImpl>-------<list>------end");
            return list;
        } finally {
            this.closeConnection(rs, pst, conn);
        }
    }
}


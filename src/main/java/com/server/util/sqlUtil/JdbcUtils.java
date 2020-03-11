package com.server.util.sqlUtil;
 

import java.lang.reflect.Field;
import java.sql.*;
import java.util.List;

import com.server.dbpool.BaseDB;

/**
 * @author  
 * 2017/11/29
 */
public class JdbcUtils extends BaseDB {

    /**
     * 增删改
     *
     * @param sql
     * @param params
     * @throws SQLException
     */
    public void updateByPreparedStatement(String sql, List<Object> params) throws SQLException {
        Connection conn = null;
        PreparedStatement ps;
        int index = 1;
        try {
            conn = openConnection();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    ps.setObject(index++, params.get(i));
                }
            }
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
        }
    }

    public <T> T findSimpleRefResult(String sql, List<Object> params, Class<T> cls) throws Exception {
        T resultObject = null;
        int index = 1;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        conn = openConnection();
        ps = conn.prepareStatement(sql);
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(index++, params.get(i));
            }
        }
        rs = ps.executeQuery();
        ResultSetMetaData metaData = rs.getMetaData();
        int colsLen = metaData.getColumnCount();
        while (rs.next()) {
            resultObject = cls.newInstance();
        }
        for (int i = 0; i < colsLen; i++) {
            String colsName = metaData.getColumnName(i + 1);
            Object colsValue = rs.getObject(colsName);
            if (colsValue == null) {
                colsValue = "";
            }
            Field field = cls.getDeclaredField(colsName);
            field.setAccessible(true);
            field.set(resultObject, colsValue);
        }
        return resultObject;
    }

}

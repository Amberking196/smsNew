package com.server.module.carryWaterVouchersManage.carryWaterVouchersProduct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * author name: why create time: 2018-11-03 16:25:36
 */
@Repository
public class CarryWaterVouchersProductDaoImpl extends BaseDao<CarryWaterVouchersProductBean>
		implements CarryWaterVouchersProductDao {

	private static Logger log = LogManager.getLogger(CarryWaterVouchersProductDaoImpl.class);

	
}

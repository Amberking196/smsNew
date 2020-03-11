package com.server.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.server.common.persistence.BaseDao;

public class BaseService<E> extends BaseDao<E> {
	
	protected Logger log = LoggerFactory.getLogger(getClass());


}

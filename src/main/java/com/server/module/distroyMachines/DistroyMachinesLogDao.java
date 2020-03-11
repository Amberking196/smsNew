package com.server.module.distroyMachines;

public interface DistroyMachinesLogDao {

	public boolean insert(String vmCode,String factoryNumber,String info,String status);
}

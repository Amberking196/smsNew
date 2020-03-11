package com.server.module.zfb_trade.module.vmbase;

public class VendingMachinesBaseBean {

	private Long id; // bigint(20) NOT NULL AUTO_INCREMENT,
	private Integer machinesTypeId; // int(11) DEFAULT NULL COMMENT '售货机类型',
	private String aisleConfiguration; // varchar(50) DEFAULT NULL COMMENT '货道配置',
	private String factoryNumber; // varchar(30) DEFAULT NULL COMMENT '出厂编号',
	private String mainProgramVersion; // varchar(30) DEFAULT NULL COMMENT '主控程序版本',
	private String ipcNumber; // varchar(30) DEFAULT NULL COMMENT '工控一体机编号',
	private String liftingGearNumber; // varchar(30) DEFAULT NULL COMMENT '提升机构编号'
	private String electricCabinetNumber; // varchar(30) DEFAULT NULL COMMENT  '电控箱编号'
	private String caseNumber; // varchar(30) DEFAULT NULL COMMENT '箱体编号',
	private String doorNumber; // varchar(30) DEFAULT NULL COMMENT '门体编号',
	private String airCompressorNumber; // varchar(30) DEFAULT NULL COMMENT '空压机编号',
	private String keyStr; // varchar(30) DEFAULT NULL COMMENT '钥匙编号',
	private String remark; // varchar(300) DEFAULT NULL COMMENT '备注信息',
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getMachinesTypeId() {
		return machinesTypeId;
	}
	public void setMachinesTypeId(Integer machinesTypeId) {
		this.machinesTypeId = machinesTypeId;
	}
	public String getAisleConfiguration() {
		return aisleConfiguration;
	}
	public void setAisleConfiguration(String aisleConfiguration) {
		this.aisleConfiguration = aisleConfiguration;
	}
	public String getFactoryNumber() {
		return factoryNumber;
	}
	public void setFactoryNumber(String factoryNumber) {
		this.factoryNumber = factoryNumber;
	}
	public String getMainProgramVersion() {
		return mainProgramVersion;
	}
	public void setMainProgramVersion(String mainProgramVersion) {
		this.mainProgramVersion = mainProgramVersion;
	}
	public String getIpcNumber() {
		return ipcNumber;
	}
	public void setIpcNumber(String ipcNumber) {
		this.ipcNumber = ipcNumber;
	}
	public String getLiftingGearNumber() {
		return liftingGearNumber;
	}
	public void setLiftingGearNumber(String liftingGearNumber) {
		this.liftingGearNumber = liftingGearNumber;
	}
	public String getElectricCabinetNumber() {
		return electricCabinetNumber;
	}
	public void setElectricCabinetNumber(String electricCabinetNumber) {
		this.electricCabinetNumber = electricCabinetNumber;
	}
	public String getCaseNumber() {
		return caseNumber;
	}
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	public String getDoorNumber() {
		return doorNumber;
	}
	public void setDoorNumber(String doorNumber) {
		this.doorNumber = doorNumber;
	}
	public String getAirCompressorNumber() {
		return airCompressorNumber;
	}
	public void setAirCompressorNumber(String airCompressorNumber) {
		this.airCompressorNumber = airCompressorNumber;
	}
	public String getKeyStr() {
		return keyStr;
	}
	public void setKeyStr(String keyStr) {
		this.keyStr = keyStr;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}

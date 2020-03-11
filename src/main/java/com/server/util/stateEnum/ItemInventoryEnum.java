package com.server.util.stateEnum;

public enum ItemInventoryEnum {

	PUTIN(1,"商品入库"),
	RETURN(2,"商品归还"),
	OUTPUT(3,"商品出库"),
	BORROW(4,"借用出库"),
	BREAKAGE(5,"破损出库");
	Integer index;
	String name;
	
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	ItemInventoryEnum(Integer index,String name){
		this.index = index;
		this.name = name;
	}
	
	public String getName(Integer index){
		for (ItemInventoryEnum itemEnum : ItemInventoryEnum.values()) {
			if(index.equals(itemEnum.getIndex())){
				return itemEnum.getName();
			}
		}
		return null;
	}
}

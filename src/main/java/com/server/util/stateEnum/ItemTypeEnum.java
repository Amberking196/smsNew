package com.server.util.stateEnum;

public enum ItemTypeEnum {
	
	EAT(1,"吃 EAT"),DRINK(2,"喝 DRINK"),USE(3,"USE");
	
	private int index;
	private String itemType;
	
	
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	ItemTypeEnum(int index,String itemType){
		this.index = index;
		this.itemType = itemType;
	}
	
	public static String getItemTypeInfo(int index){
		for (ItemTypeEnum itemType : ItemTypeEnum.values()) {
			if(itemType.getIndex() == index){
				return itemType.getItemType();
			}
		}
		return null;
	}
}

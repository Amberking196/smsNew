package com.server.common.bean;

public class KeyValueBean<K,T> {
	
	private K key;
	private T value;
	public K getKey() {
		return key;
	}
	public void setKey(K key) {
		this.key = key;
	}
	public T getValue() {
		return value;
	}
	public void setValue(T value) {
		this.value = value;
	}

	public KeyValueBean(){}
	public KeyValueBean(K key,T value){
		this.key = key;
		this.value = value;
	}
}

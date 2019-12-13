package com.scholanova.projectstore.models;

public class Stock {
	private Integer id;
	private String name;
    private String type;
    private Integer value;
    private Integer store_id;
    
    public Stock() {
    }
    public Stock(Integer id, String name, String type, Integer value, Integer store_id) {
    	this.id = id;
        this.name = name;
        this.type = type;
        this.value = value;
        this.store_id = store_id;
	}
    public Stock(String name, String type, Integer value) {
        this.name = name;
        this.type = type;
        this.value = value;
	}
    
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public Integer getStore_id() {
		return store_id;
	}
	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
}

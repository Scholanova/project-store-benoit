package com.scholanova.projectstore.models;

public class Store {

    private Integer id;
    private String name;
    private Integer value;
    
    public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
    
    public Store() {
    }

    public Store(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.value = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

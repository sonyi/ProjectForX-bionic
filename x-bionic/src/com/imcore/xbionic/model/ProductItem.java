package com.imcore.xbionic.model;

public class ProductItem {
	public long id;
	public long code;
	public String imageUrl;
	public long status;
	public String name;
	
	@Override
	public String toString() {
		return "ProductItem [id=" + id + ", code=" + code + ", imageUrl="
				+ imageUrl + ", status=" + status + ", name=" + name + "]";
	}
	
	
}

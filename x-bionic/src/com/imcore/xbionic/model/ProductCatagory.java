package com.imcore.xbionic.model;

public class ProductCatagory {
	public long id;
	public long code;
	public String imageUrl;
	public long status;
	public String name;
	
	@Override
	public String toString() {
		return "ProductCatagory [id=" + id + ", code=" + code + ", name="
				+ name + ", imageUrl=" + imageUrl + ", status=" + status + "]";
	}
}

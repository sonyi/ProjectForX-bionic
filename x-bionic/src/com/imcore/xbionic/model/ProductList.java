package com.imcore.xbionic.model;

public class ProductList {
	public long id;
	public String name;
	public String price;
	public String imageUrl;
	
	@Override
	public String toString() {
		return "ProductList [id=" + id + ", name=" + name + ", price=" + price
				+ ", imageUrl=" + imageUrl + "]";
	}
}

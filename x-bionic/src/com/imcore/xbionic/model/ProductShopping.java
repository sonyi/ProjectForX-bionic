package com.imcore.xbionic.model;

public class ProductShopping {
	public String id;
	public String imageUrl;
	public String name;
	public String color;
	public String size;
	public String price;
	public String qty;
	public String productQuantityId;
	@Override
	public String toString() {
		return "ProductShopping [id=" + id + ", imageUrl=" + imageUrl
				+ ", name=" + name + ", color=" + color + ", size=" + size
				+ ", price=" + price + ", qty=" + qty + ", productQuantityId="
				+ productQuantityId + "]";
	}
}

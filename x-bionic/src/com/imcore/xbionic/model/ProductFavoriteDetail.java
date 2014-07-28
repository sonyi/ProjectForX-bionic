package com.imcore.xbionic.model;

public class ProductFavoriteDetail {
	public long id;
	public String name;
	public String price;
	public String imageUrl;
	@Override
	public String toString() {
		return "ProductFavoriteDetail [id=" + id + ", name=" + name
				+ ", price=" + price + ", imageUrl=" + imageUrl + "]";
	}
}

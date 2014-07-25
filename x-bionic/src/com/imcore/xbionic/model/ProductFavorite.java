package com.imcore.xbionic.model;

public class ProductFavorite {
	public long catalogId;
	public long id;
	public String addDate;
	public String comment;
	public long userId;
	public long productId;
	@Override
	public String toString() {
		return "ProductFavorite [catalogId=" + catalogId + ", id=" + id
				+ ", addDate=" + addDate + ", comment=" + comment + ", userId="
				+ userId + ", productId=" + productId + "]";
	}
}

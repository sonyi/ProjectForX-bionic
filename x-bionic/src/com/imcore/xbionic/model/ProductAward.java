package com.imcore.xbionic.model;

public class ProductAward {
	public long id;
	public String title;
	public String imageUrl;
	public String createDate;
	@Override
	public String toString() {
		return "ProductNews [id=" + id + ", title=" + title + ", imageUrl="
				+ imageUrl + ", newsDate=" + createDate + "]";
	}
	
}

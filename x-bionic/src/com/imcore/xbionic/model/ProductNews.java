package com.imcore.xbionic.model;

public class ProductNews {
	public long id;
	public String title;
	public String imageUrl;
	public String newsDate;
	@Override
	public String toString() {
		return "ProductNews [id=" + id + ", title=" + title + ", imageUrl="
				+ imageUrl + ", newsDate=" + newsDate + "]";
	}
	
}

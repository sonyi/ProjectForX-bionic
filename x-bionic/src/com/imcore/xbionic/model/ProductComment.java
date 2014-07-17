package com.imcore.xbionic.model;

public class ProductComment {
	public long id;
	public String comment;
	public String commentDate;
	public boolean isShown;
	public long orderId;
	public long productId;
	public int productQuantityId;
	public long star;
	public String title;
	public int userId;
	public String userName;
	public String firstName;
	public String lastName;
	public String displayName;
	@Override
	public String toString() {
		return "ProductComment [id=" + id + ", comment=" + comment
				+ ", commentDate=" + commentDate + ", isShown=" + isShown
				+ ", orderId=" + orderId + ", productId=" + productId
				+ ", productQuantityId=" + productQuantityId + ", star=" + star
				+ ", title=" + title + ", userId=" + userId + ", userName="
				+ userName + ", firstName=" + firstName + ", lastName="
				+ lastName + ", displayName=" + displayName + "]";
	}
}

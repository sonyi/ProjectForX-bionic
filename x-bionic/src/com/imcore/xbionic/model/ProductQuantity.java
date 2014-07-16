package com.imcore.xbionic.model;

public class ProductQuantity {
	public long id;
	public long productColorId;
	public long qty;
	public long sizeId;
	@Override
	public String toString() {
		return "ProductQuantity [id=" + id + ", productColorId="
				+ productColorId + ", qty=" + qty + ", sizeId=" + sizeId + "]";
	}
	
}

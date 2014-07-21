package com.imcore.xbionic.model;

public class ExperStoryList {
	 public long id;
	 public long createBy;
     public String createDate;
     public String currentStatus;
     public String descrition;
     public String phoneUrl;
     public String productIdStr;
     public long readCount;
     public String simpleDescrition;
     public String testDate;
     public String title;
     public long updateBy;
     public String updateDate;
     public long userId;
	@Override
	public String toString() {
		return "ExperStoryList [id=" + id + ", createBy=" + createBy
				+ ", createDate=" + createDate + ", currentStatus="
				+ currentStatus + ", descrition=" + descrition + ", phoneUrl="
				+ phoneUrl + ", productIdStr=" + productIdStr + ", readCount="
				+ readCount + ", simpleDescrition=" + simpleDescrition
				+ ", testDate=" + testDate + ", title=" + title + ", updateBy="
				+ updateBy + ", updateDate=" + updateDate + ", userId="
				+ userId + "]";
	}
}

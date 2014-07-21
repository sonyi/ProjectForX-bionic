package com.imcore.xbionic.model;

import android.os.Parcel;
import android.os.Parcelable;


public class XActivityMain implements Parcelable{
	public String address;
	public String beginTime;
	public String content;
	public String createDate;
	public String endTime;
	public String organizer;
	public long provinceId;
	public String signUpDeadLine;
	public String title;
	public String titleImageUrl;
	@Override
	public String toString() {
		return "XActivityMain [address=" + address + ", beginTime=" + beginTime
				+ ", content=" + content + ", createDate=" + createDate
				+ ", endTime=" + endTime + ", organizer=" + organizer
				+ ", provinceId=" + provinceId + ", signUpDeadLine="
				+ signUpDeadLine + ", title=" + title + ", titleImageUrl="
				+ titleImageUrl + "]";
	}

	
	public static final Parcelable.Creator<XActivityMain> CREATOR = new Creator<XActivityMain>() {

		@Override
		public XActivityMain createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			XActivityMain x = new XActivityMain();
			x.address = source.readString();
			x.beginTime = source.readString();
			x.content = source.readString();
			x.createDate = source.readString();
			x.endTime = source.readString();
			x.organizer = source.readString();
			x.provinceId = source.readLong();
			x.signUpDeadLine = source.readString();
			x.title = source.readString();
			x.titleImageUrl = source.readString();
			return x;
		}

		@Override
		public XActivityMain[] newArray(int size) {
			// TODO Auto-generated method stub
			return new XActivityMain[size];
		}  
		
	};
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(address);
		dest.writeString(beginTime);
		dest.writeString(content);
		dest.writeString(createDate);
		dest.writeString(endTime);
		dest.writeString(organizer);
		dest.writeLong(provinceId);
		dest.writeString(signUpDeadLine);
		dest.writeString(title);
		dest.writeString(titleImageUrl);
	}
}

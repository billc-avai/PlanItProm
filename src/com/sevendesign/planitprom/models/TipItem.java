package com.sevendesign.planitprom.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TipItem implements Parcelable {
	private String title;
	private String content;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Creator<TipItem> CREATOR = new Creator<TipItem>() {
		public TipItem createFromParcel(Parcel parcel) {
			TipItem tipItem = new TipItem();
			tipItem.setTitle(parcel.readString());
			tipItem.setContent(parcel.readString());
			return tipItem;
		}
		
		@Override
		public TipItem[] newArray(int size) {
			return new TipItem[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(getTitle());
		dest.writeString(getContent());
	}
	
}

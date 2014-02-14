package com.sevendesign.planitprom.models;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mib on 20.08.13.
 */
public class TipCategory implements Parcelable {
	private int background;
	private String name;
	private List<TipItem> tipItems;

	public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }
	
	public TipCategory(String name) {
		this.name = name;
		tipItems = new ArrayList<TipItem>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
	
	public void addTipItem(TipItem tipItem) {
		if (tipItems == null) {
			tipItems = new ArrayList<TipItem>();
		}
		
		tipItems.add(tipItem);
	}

	public List<TipItem> getTipItems() {
        return tipItems;
    }

	public void setTipItems(List<TipItem> tipItems) {
        this.tipItems = tipItems;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TipCategory> CREATOR = new Creator<TipCategory>() {
        public TipCategory createFromParcel(Parcel parcel) {
			TipCategory tipCategory = new TipCategory(parcel.readString());
			List<TipItem> tipDetails = new ArrayList<TipItem>();
			tipCategory.setBackground(parcel.readInt());
			parcel.readList(tipDetails, TipItem.class.getClassLoader());

			return tipCategory;
        }

        public TipCategory[] newArray(int i) {
            return new TipCategory[i];
        }
    };

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getBackground());
        parcel.writeString(getName());
        parcel.writeList(getTipItems());
    }
}
